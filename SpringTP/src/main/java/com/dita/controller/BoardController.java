package com.dita.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dita.domain.Board;
import com.dita.domain.BoardCmt;
import com.dita.repository.BoardCMTRepository;
import com.dita.repository.BoardRepository;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private BoardCMTRepository boardCMTRepository;
	
	// 자유게시판
	@GetMapping("/free")
	public String showFreeBoard(Model model) {
		List<Board> freeBoard = boardRepository.findBybType("자유");
		model.addAttribute("free", freeBoard);
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
		List<Board> noticesP = boardRepository.findBybTypeAndBIsPinned("공지", "Y");
		List<Board> notices = boardRepository.findBybTypeAndBIsPinned("공지", "N");
		model.addAttribute("noticesP", noticesP);
		model.addAttribute("notices", notices);
		return "board/notice";
	}

	// 공지사항 상세 페이지
	@GetMapping("/notice/post")
	public String showNoticePost(@RequestParam("id")int id, Model model) {
		Board oneNotice = boardRepository.findById(id);

	    model.addAttribute("post", oneNotice);
	    return "board/notice_post";
	}


	// 문제 오류 제보 게시판
	@GetMapping("/error")
	public String showErrorBoard(Model model) {
		return "board/error";
	}

	// 오류 제보 작성 페이지
	@GetMapping("/error/write")
	public String showErrorWritePage() {
		return "board/error_write";
	}

	@GetMapping("/write")
	public String showWritePage(Model model) {
		return "board/write";
	}

	// 문제 Q&A 작성 폼
	@GetMapping("/qna/write")
	public String showQnaWritePage(Model model) {
		return "board/qna_write";
	}

	// 문제 Q&A 작성 처리
	@PostMapping("/qna/write")
	public String submitQnaWrite(@RequestParam String category, @RequestParam String title,
			@RequestParam String content, Model model) {
		System.out.println("Q&A 저장됨: " + title);
		return "redirect:/board/qna";
	}

	@GetMapping("post")
	public String showPost(@RequestParam("id")int id, Model model) {
		Board oneBoard = boardRepository.findById(id);
		List<BoardCmt> oneBoardCMT = boardCMTRepository.findBybId(oneBoard);
		model.addAttribute("oneBoard", oneBoard);
		model.addAttribute("oneBoardCMT", oneBoardCMT);
		return "board/post";
	}
	
	// Q&A 게시판 게시글 상세보기
	@GetMapping("/qna/view")
	public String showQnaPost(@RequestParam("id") int id, Model model) {
		System.out.println("view 진입");
	    Board oneBoard = boardRepository.findById(id);
	    List<BoardCmt> oneBoardCMT = boardCMTRepository.findBybId(oneBoard);
	    model.addAttribute("oneBoard", oneBoard);
	    model.addAttribute("oneBoardCMT", oneBoardCMT);
	    return "board/post_qna"; // templates/board/post_qna.html
	}


	// 오류 제보 게시판 게시글 상세보기
	@GetMapping("/error/view")
	public String showErrorPost(@RequestParam("id") int id, Model model) {
	    Board oneBoard = boardRepository.findById(id);
	    List<BoardCmt> oneBoardCMT = boardCMTRepository.findBybId(oneBoard);
	    model.addAttribute("oneBoard", oneBoard);
	    model.addAttribute("oneBoardCMT", oneBoardCMT);
	    return "board/post_error";
	}



}
