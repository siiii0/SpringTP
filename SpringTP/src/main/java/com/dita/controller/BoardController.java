package com.dita.controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	// 공지사항 상세 페이지
	@GetMapping("/notice/post")
	public String showNoticePost(@RequestParam Long id, Model model) {
	    String content = """
	        안녕하세요, everyCODE 운영팀입니다.

	        오는 2025년 8월 10일(일), 개발자 여러분의 실력을 겨루는
	        「2025 everyCODE 알고리즘 경진대회」가 개최됩니다.

	        이번 대회는 전국의 예비 개발자와 프로그래머들이 자유롭게 참여할 수 있으며,
	        실시간 온라인 방식으로 진행되어 장소에 구애받지 않고 도전할 수 있습니다.

	        총 3개의 레벨로 구성된 문제들이 출제되며,
	        참가자들의 문제 해결 능력과 알고리즘 이해도를 기반으로 순위가 결정됩니다.

	        또한 상위 수상자에게는 상금 및 부상,
	        그리고 everyCODE 공식 인증서가 수여될 예정입니다.
	        우수 참가자에게는 추후 진행될 개발자 채용 연계 프로그램의
	        우선 선발 혜택도 제공됩니다.

	        자세한 일정 및 접수 방법은 추후 공지될 예정이며,
	        대회와 관련된 모든 안내는 본 공지사항을 통해 확인하실 수 있습니다.

	        많은 관심과 참여 부탁드립니다.

	        감사합니다.
	        everyCODE 운영팀 드림
	        """;

	    model.addAttribute("post", Map.of(
	        "title", "📌 [공지] 2025 everyCODE 알고리즘 경진대회 개최 안내 📌",
	        "writer", "관리자",
	        "createdAt", "2025-06-06",
	        "viewCount", 123,
	        "content", content
	    ));
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
	public String showPost(@RequestParam Long id, Model model) {
		// 임시 데이터 – 나중에 DB에서 조회하도록 변경
		model.addAttribute("post", Map.of("title", "2번 틀리시는 분 보세요", "author", "ckdrjs2", "createdAt", "2025-06-05",
				"viewCount", 123, "content", "이분석 최대 크기 설정이 잘못됐을 겁니다. 대충 끝점을 4 * 10^14로 맞추세요"));
		return "board/post";
	}

}
