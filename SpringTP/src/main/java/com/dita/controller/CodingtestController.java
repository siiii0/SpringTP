package com.dita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/codingtest")
public class CodingtestController {

    // 기본문제 목록
    @GetMapping("/basic_Q")
    public String showBasicQuestions(Model model) {
        return "codingtest/basic_Q";
    }

    // 기출문제 목록
    @GetMapping("/past_Q")
    public String showPastQuestions(Model model) {
        return "codingtest/past_Q";
    }

    // 사용자 출제 문제 목록
    @GetMapping("/user_Q")
    public String showUserQuestions(Model model) {
        return "codingtest/user_Q";
    }

    // 문제 풀이 페이지
    @GetMapping("/solve_Q")
    public String solveQuestion(@RequestParam(required = false) String id, Model model) {
        return "codingtest/solve_Q";
    }
}
