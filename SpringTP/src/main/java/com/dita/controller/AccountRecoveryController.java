package com.dita.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dita.service.UserService;

@Controller
@SessionAttributes("resetUserId") // 비밀번호 재설정 과정에서 사용할 세션 속성
public class AccountRecoveryController {

    private static final Logger logger = LoggerFactory.getLogger(AccountRecoveryController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * 아이디 찾기 페이지 표시
     */
    @GetMapping("/find-id")
    public String findIdPage() {
        logger.info("아이디 찾기 페이지 요청");
        return "find-id";
    }
    
    /**
     * 아이디 찾기 처리
     */
    @PostMapping("/find-id")
    public String processFindId(
            @RequestParam("email") String email, 
            Model model) {
        
        logger.info("아이디 찾기 요청: email={}", email);
        
        try {
            String userId = userService.findUserIdByEmail(email);
            if (userId != null) {
                model.addAttribute("foundId", userId);
                logger.info("아이디 찾기 성공: {}", userId);
            } else {
                model.addAttribute("error", "해당 이메일로 등록된 아이디를 찾을 수 없습니다.");
                logger.warn("아이디 찾기 실패: 이메일에 해당하는 계정 없음 - {}", email);
            }
        } catch (Exception e) {
            logger.error("아이디 찾기 처리 중 오류 발생", e);
            model.addAttribute("error", "아이디 찾기 처리 중 오류가 발생했습니다.");
        }
        
        return "find-id";
    }
    
    /**
     * 비밀번호 재설정 페이지 표시
     */
    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        logger.info("비밀번호 재설정 페이지 요청");
        return "reset-password";
    }
    
    /**
     * 비밀번호 재설정 요청 처리 (아이디와 이메일 확인)
     */
    @PostMapping("/reset-password/request")
    public String processResetPasswordRequest(
            @RequestParam("userId") String userId,
            @RequestParam("email") String email,
            Model model) {
        
        logger.info("비밀번호 재설정 요청: userId={}, email={}", userId, email);
        
        try {
            // 입력 파라미터 유효성 확인
            if (userId == null || userId.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                logger.warn("비밀번호 재설정 요청 실패: 유효하지 않은 입력값 - userId={}, email={}", userId, email);
                model.addAttribute("error", "아이디와 이메일을 모두 입력해주세요.");
                return "reset-password";
            }
            
            logger.info("사용자 ID/이메일 검증 시작: userId={}, email={}", userId, email);
            boolean isValid = userService.validateUserIdAndEmail(userId, email);
            logger.info("사용자 ID/이메일 검증 결과: {}", isValid);
            
            if (isValid) {
                // 인증 성공 시 세션에 사용자 ID 저장
                model.addAttribute("resetUserId", userId);
                logger.info("비밀번호 재설정 인증 성공: {}. 세션에 사용자 ID 저장", userId);
                return "redirect:/reset-password/new";
            } else {
                model.addAttribute("error", "입력한 아이디와 이메일 정보가 일치하지 않습니다.");
                logger.warn("비밀번호 재설정 인증 실패: 아이디/이메일 불일치 - {}", userId);
            }
        } catch (Exception e) {
            logger.error("비밀번호 재설정 인증 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", "비밀번호 재설정 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return "reset-password";
    }
    
    /**
     * 새 비밀번호 설정 페이지 표시
     */
    @GetMapping("/reset-password/new")
    public String newPasswordPage(Model model) {
        logger.info("새 비밀번호 설정 페이지 요청. 모델 속성: {}", model.asMap().keySet());
        
        if (!model.containsAttribute("resetUserId")) {
            logger.warn("잘못된 비밀번호 재설정 페이지 접근 시도: resetUserId 없음");
            return "redirect:/reset-password";
        }
        
        // 세션에서 모델로 사용자 ID 전달
        String userId = (String) model.asMap().get("resetUserId");
        model.addAttribute("userId", userId);
        
        logger.info("새 비밀번호 설정 페이지 요청 성공: userId={}", userId);
        return "new-password";
    }
    
    /**
     * 새 비밀번호 설정 처리
     */
    @PostMapping("/reset-password/new")
    public String processNewPassword(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes,
            SessionStatus sessionStatus) {
        
        logger.info("새 비밀번호 설정 요청: userId={}", userId);
        
        // 비밀번호 확인
        if (!password.equals(confirmPassword)) {
            model.addAttribute("userId", userId);
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            logger.warn("새 비밀번호 설정 실패: 비밀번호 불일치");
            return "new-password";
        }
        
        try {
            userService.updatePassword(userId, password);
            redirectAttributes.addFlashAttribute("success", "비밀번호가 성공적으로 변경되었습니다.");
            logger.info("비밀번호 재설정 성공: {}", userId);
            
            // 세션 속성 정리
            sessionStatus.setComplete();
            
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("userId", userId);
            model.addAttribute("error", "비밀번호 변경 중 오류가 발생했습니다.");
            logger.error("비밀번호 변경 중 오류 발생", e);
            return "new-password";
        }
    }
} 