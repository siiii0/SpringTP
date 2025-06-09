package com.dita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @GetMapping
    public String mypage(Model model) {
        model.addAttribute("username", "user01");
        return "mypage/my";
    }
    
    @GetMapping("/submission")
    public String showSubmission() {
        return "mypage/submission";
    }

    @GetMapping("/created")
    public String showCreated() {
        return "mypage/created";
    }

    @GetMapping("/wrong")
    public String showWrongProblems() {
        return "mypage/wrong";
    }

    @GetMapping("/correct")
    public String showCorrectProblems() {
        return "mypage/correct";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("username", "user01");
        return "mypage/edit";
    }
    
    @GetMapping("/edit/social")
    public String editSocialPage() {
        return "mypage/edit_social";
    }

}
