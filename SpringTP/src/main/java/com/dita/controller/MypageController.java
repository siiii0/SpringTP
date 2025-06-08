package com.dita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController {

    // 마이페이지
    @GetMapping("/mypage")
    public String showMypage(Model model) {
        return "mypage/my";
    }

    // 내 정보 수정
    @GetMapping("/mypage/edit")
    public String editMyInfo(Model model) {
        return "mypage/edit";
    }
}
