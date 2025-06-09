package com.dita.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dita.security.UserSecurityDTO;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserApiController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);
    
    /**
     * 현재 로그인된 사용자 정보를 JSON으로 반환
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 현재 인증된 사용자 정보 가져오기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.debug("인증 객체: {}", auth);
            
            // 실제 로그인 여부 확인 (anonymousUser가 아닌지)
            boolean isAuthenticated = auth != null && auth.isAuthenticated() && 
                                     !"anonymousUser".equals(auth.getPrincipal());
            
            // 응답 데이터 구성
            result.put("authenticated", isAuthenticated);
            result.put("timestamp", System.currentTimeMillis());
            
            if (isAuthenticated) {
                if (auth.getPrincipal() instanceof UserSecurityDTO) {
                    UserSecurityDTO userDetails = (UserSecurityDTO) auth.getPrincipal();
                    logger.info("API 호출: 현재 로그인한 사용자 정보 요청 - {}", userDetails.getUsername());
                    
                    result.put("username", userDetails.getUsername());
                    result.put("userType", userDetails.getUserType());
                } else {
                    logger.info("API 호출: 인증된 사용자이지만 UserSecurityDTO 타입이 아님");
                    result.put("username", auth.getName());
                }
            } else {
                logger.info("API 호출: 인증되지 않은 사용자 접근");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("사용자 정보 확인 중 오류 발생", e);
            result.put("authenticated", false);
            result.put("error", "사용자 정보 확인 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }
} 