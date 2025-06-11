package com.dita.controller;

import com.dita.domain.User;
import com.dita.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class RankingController {
    private static final Logger logger = Logger.getLogger(RankingController.class.getName());
    
    @Autowired
    private RankingService rankingService;
    
    // 개인 랭킹 페이지 - 데이터베이스의 userScore 필드를 그대로 사용
    @GetMapping("/ranking/level")
    @Transactional(readOnly = true)
    public String showLevelRanking(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String grade,
            Model model) {
        
        try {
            logger.info("개인 랭킹 조회 요청 - 페이지: " + page + ", 크기: " + size + ", 등급: " + (grade != null ? grade : "전체"));
            
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userRanking;
            
            // 필터링 적용
            if (grade != null && !grade.equals("전체")) {
                // 등급이 '일반'인 경우 null 또는 '일반' 둘 다 필터링
                userRanking = rankingService.getUserRankingByGrade(grade, pageable);
                logger.info("특정 등급으로 필터링: " + grade);
            } else {
                userRanking = rankingService.getUserRanking(pageable);
                logger.info("전체 등급 조회");
            }
            
            logger.info("개인 랭킹 결과 - 총 사용자 수: " + userRanking.getTotalElements());
            
            // 디버깅: 사용자 등급 정보 출력
            logger.info("===== 컨트롤러에서의 사용자 등급 정보 =====");
            for (User user : userRanking.getContent()) {
                String userGrade = user.getUserGrade();
                // null인 경우 '일반'으로 표시
                if (userGrade == null) {
                    userGrade = "일반";
                }
                
                logger.info("사용자 ID: " + user.getIdType().getUserId() + 
                        ", 등급: " + userGrade +
                        ", 점수: " + user.getUserScore());
            }
            logger.info("========================================");
            
            // 각 사용자별 추가 정보 수집
            List<Map<String, Object>> userRankingWithDetails = new ArrayList<>();
            
            for (User user : userRanking.getContent()) {
                Map<String, Object> userDetails = new HashMap<>();
                
                // 등급이 null인 경우 '일반'으로 설정
                if (user.getUserGrade() == null) {
                    user.setUserGrade("일반");
                }
                
                userDetails.put("user", user);
                
                // 사용자가 푼 문제 수와 정답 비율 계산
                int solvedCount = rankingService.countSolvedProblems(user);
                double correctRate = rankingService.calculateUserCorrectRate(user);
                
                userDetails.put("solvedCount", solvedCount);
                userDetails.put("correctRate", String.format("%.1f", correctRate));
                
                userRankingWithDetails.add(userDetails);
                
                logger.info("사용자 랭킹 정보: " + user.getIdType().getUserId() + 
                        ", 등급: " + user.getUserGrade() +
                        ", 점수: " + user.getUserScore() + 
                        ", 푼 문제: " + solvedCount + 
                        ", 정답 비율: " + correctRate);
            }
            
            // 총 점수(userScore) 기준으로 내림차순 정렬
            userRankingWithDetails = userRankingWithDetails.stream()
                .sorted(Comparator.comparing(userDetail -> ((User)userDetail.get("user")).getUserScore(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
            
            // 정렬 후 순위 다시 매기기
            int rank = page * size + 1;
            for (Map<String, Object> userDetail : userRankingWithDetails) {
                userDetail.put("rank", rank++);
            }
            
            model.addAttribute("users", userRankingWithDetails);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userRanking.getTotalPages());
            model.addAttribute("selectedGrade", grade != null ? grade : "전체");
            
            return "ranking/level";
        } catch (Exception e) {
            logger.severe("개인 랭킹 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "랭킹 정보를 불러오는 중 오류가 발생했습니다.");
            return "error/general";
        }
    }
    
    // 소속 랭킹 페이지
    @GetMapping("/ranking/org")
    @Transactional(readOnly = true)
    public String showOrgRanking(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "score") String sort,
            Model model) {
        
        try {
            logger.info("소속 랭킹 조회 요청 - 페이지: " + page + ", 크기: " + size + ", 정렬: " + sort);
            
            Pageable pageable = PageRequest.of(page, size);
            
            Page<Object[]> orgRanking = rankingService.getOrganizationRanking(pageable);
            
            logger.info("소속 랭킹 결과 - 총 소속 수: " + orgRanking.getTotalElements());
            
            List<Map<String, Object>> orgRankingList = new ArrayList<>();
            
            // 모든 소속 정보 수집
            for (Object[] result : orgRanking.getContent()) {
                String orgName = (String) result[0];
                Long memberCount = (Long) result[1];
                Long totalScore = (Long) result[2];
                
                // 상세 정보를 포함한 소속 정보 가져오기
                Map<String, Object> orgDetails = rankingService.getOrganizationDetails(orgName, memberCount, totalScore);
                
                orgRankingList.add(orgDetails);
                
                logger.info("소속 랭킹 정보: " + orgName + 
                        ", 인원: " + memberCount + 
                        ", 총점: " + totalScore + 
                        ", 맞은 문제: " + orgDetails.get("solvedCount") + 
                        ", 정답 비율: " + orgDetails.get("correctRate"));
            }
            
            // 선택된 정렬 기준으로 정렬
            if ("solved".equals(sort)) {
                // 맞은 문제 많은 순으로 정렬
                logger.info("맞은 문제 많은 순으로 정렬");
                orgRankingList = orgRankingList.stream()
                        .sorted(Comparator.comparing(org -> ((Number) org.get("solvedCount")).longValue(), Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else if ("rate".equals(sort)) {
                // 정답 비율 높은 순으로 정렬
                logger.info("정답 비율 높은 순으로 정렬");
                orgRankingList = orgRankingList.stream()
                        .sorted(Comparator.comparing(org -> {
                            String rateStr = (String) org.get("correctRate");
                            return Double.parseDouble(rateStr.replace("%", ""));
                        }, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else {
                // 기본: 총 점수 높은 순으로 정렬
                logger.info("총 점수 높은 순으로 정렬");
                orgRankingList = orgRankingList.stream()
                        .sorted(Comparator.comparing(org -> ((Number) org.get("totalScore")).longValue(), Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
            
            // 정렬 후 순위 다시 매기기
            int rank = page * size + 1;
            for (Map<String, Object> org : orgRankingList) {
                org.put("rank", rank++);
            }
            
            model.addAttribute("organizations", orgRankingList);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", orgRanking.getTotalPages());
            model.addAttribute("selectedSort", sort);
            
            return "ranking/org";
        } catch (Exception e) {
            logger.severe("소속 랭킹 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "랭킹 정보를 불러오는 중 오류가 발생했습니다.");
            return "error/general";
        }
    }
}
