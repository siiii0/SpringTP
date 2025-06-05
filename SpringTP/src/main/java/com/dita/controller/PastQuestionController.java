package com.dita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PastQuestionController {

    @GetMapping("/past_Q")
    public String pastQuestionPage() {
        return "past_Q"; // templates/past_Q.html
    }
}
