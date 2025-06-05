package com.dita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicQuestionController {

    @GetMapping("/basic_Q")
    public String showBasicQuestions() {
        return "basic_Q"; 
    }
}
