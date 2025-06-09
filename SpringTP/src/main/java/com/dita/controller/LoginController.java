package com.dita.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@GetMapping("/login")
	public String loginPage(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			Model model) {
		
		// 로그인 페이지 접근 로깅
		logger.info("로그인 페이지 요청");
		
		// 로그인 실패 시 에러 메시지 추가
		if (error != null) {
			logger.warn("로그인 실패");
			model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		// 로그아웃 성공 시 메시지 추가
		if (logout != null) {
			logger.info("로그아웃 성공");
			model.addAttribute("success", "로그아웃 되었습니다.");
		}
		
		return "login"; // templates/login.html 렌더링
	}
}



