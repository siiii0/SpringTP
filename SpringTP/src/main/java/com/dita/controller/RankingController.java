package com.dita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RankingController {

    @GetMapping("/ranking/org")
    public String showOrgRanking(Model model) {
        // 추후 DB 연동 시 model.addAttribute("rankingList", ...);
        return "ranking/org";
    }
    
    @GetMapping("/ranking/level")
    public String showLevelRanking(Model model) {
        return "ranking/level";
    }
}
