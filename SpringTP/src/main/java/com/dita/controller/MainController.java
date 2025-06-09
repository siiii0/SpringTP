package com.dita.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dita.security.UserSecurityDTO;

@Controller
public class MainController {
    
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    // 루트 경로 - 메인 페이지로 리다이렉트
    @GetMapping("/")
    public String root() {
        return "redirect:/main";
    }

    // 메인 페이지
    @GetMapping("/main")
    public String main(Model model) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // 인증된 사용자가 있는 경우 로그 출력
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            if (auth.getPrincipal() instanceof UserSecurityDTO) {
                UserSecurityDTO userDetails = (UserSecurityDTO) auth.getPrincipal();
                logger.info("현재 로그인한 사용자: {}", userDetails.getUsername());
                
                // 모델에 추가 정보 전달
                model.addAttribute("userId", userDetails.getUsername());
                model.addAttribute("userType", userDetails.getUserType());
            } else {
                logger.info("인증된 사용자이지만 UserSecurityDTO 타입이 아님: {}", auth.getPrincipal().getClass());
            }
        } else {
            logger.info("인증되지 않은 사용자 접근");
        }
        
        return "main"; // templates/main.html 페이지 반환
    }
}
