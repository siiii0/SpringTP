package com.dita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
    @GetMapping(value = "/login")
    public void loginPage(@RequestParam String id, @RequestParam String pwd, HttpSession session) {
    	
    }
    
}

