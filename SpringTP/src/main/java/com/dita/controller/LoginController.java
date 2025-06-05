package com.dita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String loginPage() {
	    return "login"; // templates/login.html 렌더링
	}
    
}

