package com.dita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SolveQuestionController {

    @GetMapping("/solve_Q")
    public String solveQuestion() {
        return "solve_Q"; // templates/solve_Q.html
    }
}
