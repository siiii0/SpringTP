package com.dita.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionListener;
import jakarta.servlet.http.HttpSessionEvent;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
    // 강도를 낮추어 암호화된 문자열의 길이를 줄임
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 기본값은 10이나, 4로 낮추어 암호화된 비밀번호의 길이를 줄임
        return new BCryptPasswordEncoder(4);
    }
    
    // 인증 성공 핸들러
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                
                // 인증 성공 정보 로깅
                logger.info("로그인 성공: {}", authentication.getName());
                logger.debug("인증 객체 정보: {}", authentication.getDetails());
                logger.debug("인증 주체 타입: {}", authentication.getPrincipal().getClass().getName());
                
                // 세션에 사용자 정보 저장
                HttpSession session = request.getSession();
                session.setAttribute("username", authentication.getName());
                session.setMaxInactiveInterval(1800); // 30분
                
                // 세션 유지를 위한 쿠키 설정
                Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
                sessionCookie.setMaxAge(1800); // 30분
                sessionCookie.setPath("/");
                sessionCookie.setHttpOnly(true);
                response.addCookie(sessionCookie);
                
                // 메인 페이지로 리다이렉트
                logger.info("로그인 성공 후 메인 페이지로 리다이렉트");
                response.sendRedirect("/main");
            }
        };
    }

    // Spring Security 보안 설정 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 개발 중이므로 CSRF 보호 명시적으로 비활성화
            .csrf(csrf -> csrf.disable())
            
            // URL 접근 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 누구나 접근 가능한 경로
                .requestMatchers(
                    "/", "/main", 
                    "/signup", "/signup/**",
                    "/login", "/login-process", "/logout",
                    "/css/**", "/js/**", "/images/**",
                    "/api/user/current", // 사용자 API 접근 허용
                    "/find-id", "/reset-password",
                    "/fragments/**", "/error"
                ).permitAll()
                // 그 외의 경로는 인증 필요
                .anyRequest().authenticated()
            )
            
            // 로그인 설정
            .formLogin(form -> form
                .loginPage("/login") // 커스텀 로그인 페이지 경로
                .loginProcessingUrl("/login-process") // 로그인 처리 URL (form의 action과 일치해야 함)
                .defaultSuccessUrl("/main", true) // 로그인 성공 시 항상 이동 경로
                .successHandler(authenticationSuccessHandler()) // 로그인 성공 핸들러
                .failureUrl("/login?error=true") // 로그인 실패 시 이동 경로
                .usernameParameter("username") // 아이디 파라미터명
                .passwordParameter("password") // 비밀번호 파라미터명
                .permitAll()
            )
            
            // 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // 로그아웃 성공 시 이동 경로
                .invalidateHttpSession(true) // 세션 무효화
                .clearAuthentication(true) // 인증 정보 제거
                .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                .permitAll()
            )
            
            // 세션 관리
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요할 때만 세션 생성
                .sessionFixation().changeSessionId() // 세션 고정 공격 방지
                .maximumSessions(1) // 동시 접속 제한
                .expiredUrl("/login?expired") // 세션 만료 시 이동 경로
            );

        // 필터 체인 구성 로깅
        logger.info("Spring Security 필터 체인 구성 완료");
        
        return http.build();
    }
    
    // 세션 리스너 등록
    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                HttpSession session = se.getSession();
                logger.info("세션 생성됨: {}", session.getId());
                session.setMaxInactiveInterval(1800); // 30분 = 1800초
            }
            
            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                logger.info("세션 소멸됨: {}", se.getSession().getId());
            }
        };
    }
}
