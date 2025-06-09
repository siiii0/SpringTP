
package com.dita.controller;

import com.dita.domain.Question;
import com.dita.repository.QuestionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/questions")
    public String listQuestions(Model model) {
        List<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);
        return "question/list"; // → /WEB-INF/views/question/list.jsp
    }
    
    @GetMapping("/questions/{id}")
    public String detailQuestion(@PathVariable("id") int id, Model model) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()) {
            model.addAttribute("question", question.get());
            return "question/detail";
        } else {
            return "error/404";
        }
    }
    
    @GetMapping("/questions/create")
    public String showCreateForm(Model model) {
        model.addAttribute("question", new Question());
        return "question/create";
    }


}

