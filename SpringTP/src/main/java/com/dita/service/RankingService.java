package com.dita.service;

import com.dita.domain.Question;
import com.dita.domain.Submissions;
import com.dita.domain.User;
import com.dita.repository.SubmissionsRepository;
import com.dita.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class RankingService {
    private static final Logger logger = Logger.getLogger(RankingService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubmissionsRepository submissionsRepository;

    // 문제 난이도별 점수 설정
    private static final Map<String, Integer> DIFFICULTY_SCORES = new HashMap<>();
    
    static {
        DIFFICULTY_SCORES.put("LV. 0", 20);
        DIFFICULTY_SCORES.put("LV. 1", 100);
        DIFFICULTY_SCORES.put("LV. 2", 500);
        DIFFICULTY_SCORES.put("LV. 3", 1000);
    }

    // 사용자 점수 업데이트 - 맞은 문제 기준으로 계산
    @Transactional
    public void updateUserScore(User user) {
        try {
            // 기존 방식: 전체 객체를 가져옴
            List<Question> solvedQuestions = submissionsRepository.findDistinctCorrectQuestionsByUser(user);
            
            // 복합키 방식: ID와 Type으로 직접 접근
            if (solvedQuestions.isEmpty()) {
                logger.info("복합키 방식으로 문제 조회 시도: " + user.getIdType().getUserId() + ", " + user.getIdType().getUserType());
                solvedQuestions = submissionsRepository.findDistinctCorrectQuestionsByUserIdAndType(
                    user.getIdType().getUserId(), 
                    user.getIdType().getUserType()
                );
            }
            
            int totalScore = 0;
            for (Question question : solvedQuestions) {
                String difficulty = question.getQ_difficulty();
                if (DIFFICULTY_SCORES.containsKey(difficulty)) {
                    totalScore += DIFFICULTY_SCORES.get(difficulty);
                }
            }
            
            user.setUserScore(totalScore);
            userRepository.save(user);
            
            logger.info("사용자 " + user.getIdType().getUserId() + " 점수 업데이트 완료: " + totalScore + " (문제 수: " + solvedQuestions.size() + ")");
        } catch (Exception e) {
            logger.severe("사용자 점수 업데이트 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 모든 사용자의 점수 일괄 업데이트
    @Transactional
    public void updateAllUsersScore() {
        List<User> users = userRepository.findAll();
        logger.info("전체 사용자 수: " + users.size());
        
        for (User user : users) {
            updateUserScore(user);
        }
    }

    // 개인 랭킹 조회 - 전체 (총 점수 높은 순)
    public Page<User> getUserRanking(Pageable pageable) {
        logUserGrades(); // 디버깅용
        logger.info("전체 사용자 랭킹 조회 - 총 점수 높은 순으로 정렬");
        return userRepository.findAllUsersByScoreDesc(pageable);
    }

    // 특정 등급의 사용자 랭킹 조회 (총 점수 높은 순)
    public Page<User> getUserRankingByGrade(String grade, Pageable pageable) {
        logUserGrades(); // 디버깅용
        logger.info("특정 등급 사용자 랭킹 조회: " + grade + " - 총 점수 높은 순으로 정렬");
        return userRepository.findUsersByGrade(grade, pageable);
    }
    
    // 디버깅: 모든 사용자의 등급 정보 출력
    private void logUserGrades() {
        List<Object[]> userGrades = userRepository.findAllUsersWithGrades();
        logger.info("===== 사용자 등급 정보 =====");
        for (Object[] userInfo : userGrades) {
            String userId = (String) userInfo[0];
            String grade = (String) userInfo[1];
            logger.info("사용자 ID: " + userId + ", 등급: " + (grade != null ? grade : "null"));
        }
        logger.info("=========================");
    }

    // 소속별 랭킹 조회
    public Page<Object[]> getOrganizationRanking(Pageable pageable) {
        return userRepository.findUsersByOrganizationRanking(pageable);
    }

    // 사용자가 푼 문제 수 조회
    public int countSolvedProblems(User user) {
        try {
            // 복합키를 사용하는 카운트 쿼리
            long count = submissionsRepository.countDistinctCorrectQuestionsByUserIdAndType(
                user.getIdType().getUserId(), 
                user.getIdType().getUserType()
            );
            
            logger.info("사용자 " + user.getIdType().getUserId() + " 푼 문제 수: " + count);
            return (int) count;
        } catch (Exception e) {
            logger.severe("푼 문제 수 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // 사용자의 정답 비율 계산
    public double calculateUserCorrectRate(User user) {
        try {
            // 복합키를 사용하는 카운트 쿼리
            long totalSubmissions = submissionsRepository.countSubmissionsByUserIdAndType(
                user.getIdType().getUserId(), 
                user.getIdType().getUserType()
            );
            
            if (totalSubmissions == 0) {
                return 0.0;
            }
            
            long correctSubmissions = submissionsRepository.countCorrectSubmissionsByUserIdAndType(
                user.getIdType().getUserId(), 
                user.getIdType().getUserType()
            );
            
            double rate = (double) correctSubmissions / totalSubmissions * 100;
            logger.info("사용자 " + user.getIdType().getUserId() + " 정답 비율: " + rate + "% (" + correctSubmissions + "/" + totalSubmissions + ")");
            return rate;
        } catch (Exception e) {
            logger.severe("정답 비율 계산 오류: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }
    
    // 특정 소속(학교/회사)이 맞은 문제 수 조회
    public long countSolvedProblemsByOrg(String orgName) {
        try {
            long count = submissionsRepository.countDistinctCorrectQuestionsByOrg(orgName);
            logger.info("소속 " + orgName + " 맞은 문제 수: " + count);
            return count;
        } catch (Exception e) {
            logger.severe("소속 맞은 문제 수 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    // 특정 소속(학교/회사)의 정답 비율 계산
    public double calculateOrgCorrectRate(String orgName) {
        try {
            long totalSubmissions = submissionsRepository.countSubmissionsByOrg(orgName);
            
            if (totalSubmissions == 0) {
                return 0.0;
            }
            
            long correctSubmissions = submissionsRepository.countCorrectSubmissionsByOrg(orgName);
            
            double rate = (double) correctSubmissions / totalSubmissions * 100;
            logger.info("소속 " + orgName + " 정답 비율: " + rate + "% (" + correctSubmissions + "/" + totalSubmissions + ")");
            return rate;
        } catch (Exception e) {
            logger.severe("소속 정답 비율 계산 오류: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }
    
    // 소속 랭킹 정보를 상세 정보와 함께 조회
    public Map<String, Object> getOrganizationDetails(String orgName, long memberCount, long totalScore) {
        Map<String, Object> details = new HashMap<>();
        
        details.put("orgName", orgName);
        details.put("memberCount", memberCount);
        details.put("totalScore", totalScore);
        
        // 실제 소속별 맞은 문제 수 및 정답 비율을 조회
        long solvedCount = countSolvedProblemsByOrg(orgName);
        double correctRate = calculateOrgCorrectRate(orgName);
        
        details.put("solvedCount", solvedCount);
        details.put("correctRate", String.format("%.1f%%", correctRate));
        
        return details;
    }
} 