package com.dita.controller;

import java.util.List;
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

import com.dita.domain.Q_Language;
import com.dita.domain.Question;
import com.dita.domain.Submissions;
import com.dita.domain.User;
import com.dita.domain.User_id_type;
import com.dita.repository.QLangRepository;
import com.dita.repository.QuestionRepository;
import com.dita.repository.SubmissionsRepository;
import com.dita.security.UserSecurityDTO;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/codingtest")
public class CodingtestController {
	
	@Autowired
    private QuestionRepository questionRepository;
	
	@Autowired
	private QLangRepository qlangRepository;
	
	@Autowired
	private SubmissionsRepository submissionsRepository;
	
    // 기본문제 목록
    @GetMapping("/basic_Q")
    public String showBasicQuestions(
        @RequestParam(defaultValue = "1") int page,
        Model model) {

        int pageSize = 20;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("qId").ascending());

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
    
 // 사용자 출제 문제 작성 폼 페이지
    @GetMapping("/create_Q")
    public String showCreateUserQuestionForm(Model model) {
        return "codingtest/create_Q";
    }
    
    // 문제 풀이 페이지
    @GetMapping("/solve_Q")
    public String solveQuestion(@RequestParam("id") int id, Model model) {
        Optional<Question> question = questionRepository.findById(id);

        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();  // principal은 로그인된 사용자 정보

        List<Submissions> history = null;

        if (principal instanceof UserSecurityDTO) {
            // UserSecurityDTO에서 필요한 정보를 가져오기
            UserSecurityDTO userDetails = (UserSecurityDTO) principal;

            // UserSecurityDTO에서 정보를 추출하여 User 객체를 생성
            User user = new User();
            User_id_type userIdType = new User_id_type(userDetails.getUsername(), userDetails.getUserType());  // 복합키 설정
            user.setIdType(userIdType);  // User_id_type을 User 객체에 설정
            
            // 이제 User 객체를 사용하여 Submissions 조회
            history = submissionsRepository.findByqIdAndUser(question.get(), user);
        } else {
            // 인증되지 않은 사용자 처리
            System.out.println("No authenticated user.");
        }

        if (question.isPresent()) {
            // 디버깅을 위한 로그 출력
            System.out.println("question q_id: " + question.get().getQId());

            model.addAttribute("oneQuestion", question.get());
            List<Q_Language> languages = qlangRepository.findByqId(question.get());
            model.addAttribute("languages", languages);
            model.addAttribute("history", history);

            return "codingtest/solve_Q";
        } else {
            return "error/404";
        }
    }


}
