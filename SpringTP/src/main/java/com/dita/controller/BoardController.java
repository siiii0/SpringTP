package com.dita.controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/board")
public class BoardController {

    // 자유게시판
    @GetMapping("/free")
    public String showFreeBoard(Model model) {
        return "board/free";
    }

    // 문제 Q&A 게시판
    @GetMapping("/qna")
    public String showQnaBoard(Model model) {
        return "board/qna";
    }

    // 공지사항
    @GetMapping("/notice")
    public String showNoticeBoard(Model model) {
        return "board/notice";
    }

    // 문제 오류 제보 게시판
    @GetMapping("/error")
    public String showErrorBoard(Model model) {
        return "board/error";
    }
    
    @GetMapping("/write")
    public String showWritePage(Model model) {
        return "board/write";
    }
    
    @GetMapping("post")
    public String showPost(@RequestParam Long id, Model model) {
        // 임시 데이터 – 나중에 DB에서 조회하도록 변경
        model.addAttribute("post", Map.of(
            "title", "2번 틀리시는 분 보세요",
            "author", "ckdrjs2",
            "createdAt", "2025-06-05",
            "viewCount", 123,
            "content", "이분석 최대 크기 설정이 잘못됐을 겁니다. 대충 끝점을 4 * 10^14로 맞추세요"
        ));
        return "board/post";
    }


}
