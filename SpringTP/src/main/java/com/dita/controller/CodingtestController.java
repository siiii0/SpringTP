package com.dita.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dita.domain.Question;
import com.dita.repository.QuestionRepository;

@Controller
@RequestMapping("/codingtest")
public class CodingtestController {
	
	@Autowired
    private QuestionRepository questionRepository;
	

    // 기본문제 목록
    @GetMapping("/basic_Q")
    public String showBasicQuestions(
        @RequestParam(defaultValue = "1") int page,
        Model model) {

        int pageSize = 20;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("q_id").ascending());

        Page<Question> questionPage = questionRepository.findAll(pageable);

        model.addAttribute("questions", questionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages());

        return "codingtest/basic_Q";
    }

    @GetMapping("/basic_Q/{id}")
    public String detailQuestion(@PathVariable("id") int id, Model model) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()) {
            model.addAttribute("question", question.get());
            return "codingtest/solve_Q";
        } else {
            return "error/404";
        }
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
