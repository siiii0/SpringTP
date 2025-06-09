package com.dita.controller;

import com.dita.domain.User;
import com.dita.domain.User_id_type;
import com.dita.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private UserService userService;

    // 회원가입 페이지 로딩
    @GetMapping
    public String signupPage() {
        logger.info("회원가입 페이지 요청");
        return "signup";
    }

    // 회원가입 처리
    @PostMapping
    public String processSignup(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("school") String school,
            Model model,
            RedirectAttributes redirectAttributes) {

        logger.info("회원가입 처리 시작: username={}, email={}", username, email);

        try {
            // 아이디 중복 체크
            if (userService.isUserIdExists(username)) {
                logger.warn("중복된 아이디: {}", username);
                model.addAttribute("error", "이미 사용 중인 아이디입니다.");
                return "signup";
            }

            // User 객체 생성
            User user = new User();
            
            // 복합키 설정
            User_id_type idType = new User_id_type();
            idType.setUserId(username);
            idType.setUserType("일반"); // 기본 타입은 '일반'으로 설정
            user.setIdType(idType);
            
            // 사용자 정보 설정
            user.setUserPwd(password); // 서비스에서 암호화 처리
            user.setUserEmail(email);
            user.setUserSchool(school);
            user.setUserGrade("일반"); // 기본 등급
            user.setRegisteredAt(LocalDateTime.now());
            user.setUserWd("N"); // 탈퇴 여부
            user.setUserScore(0); // 초기 점수

            logger.debug("회원가입 정보: {}", user);

            // 회원가입 처리
            userService.register(user);
            logger.info("회원가입 성공: {}", username);

            // 회원가입 성공 시 완료 페이지로 이동하면서 사용자 정보 전달
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/signup/success";
        } catch (Exception e) {
            // 예외 처리
            logger.error("회원가입 처리 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "signup";
        }
    }
    
    // 회원가입 완료 페이지
    @GetMapping("/success")
    public String signupSuccess() {
        logger.info("회원가입 완료 페이지 요청");
        return "signup-success";
    }
    
    // 아이디 중복 체크 API
    @PostMapping("/check-id")
    @ResponseBody
    public boolean checkId(@RequestParam("username") String username) {
        logger.info("아이디 중복 체크: {}", username);
        boolean result = !userService.isUserIdExists(username);
        logger.info("아이디 사용 가능 여부: {}", result);
        return result; // true면 사용 가능, false면 이미 존재
    }
}