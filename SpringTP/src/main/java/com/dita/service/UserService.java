package com.dita.service;

import com.dita.domain.User;
import com.dita.domain.User_id_type;
import com.dita.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final int MAX_PASSWORD_LENGTH = 100; // 데이터베이스 컬럼 크기에 맞춤

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 처리 메서드
    @Transactional
    public void register(User user) {
        try {
            logger.info("회원 등록 시작: userId={}, userType={}", 
                    user.getIdType().getUserId(), user.getIdType().getUserType());
            
        // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(user.getUserPwd());
            
            // 암호화된 비밀번호가 데이터베이스 컬럼 크기를 초과하는지 확인
            if (encodedPassword.length() > MAX_PASSWORD_LENGTH) {
                logger.warn("암호화된 비밀번호 길이가 너무 깁니다: {} 문자", encodedPassword.length());
                // 비밀번호 길이를 제한하거나 다른 방식으로 암호화 필요
                encodedPassword = encodedPassword.substring(0, MAX_PASSWORD_LENGTH);
            }
            
            user.setUserPwd(encodedPassword);
            logger.debug("비밀번호 암호화 완료: 길이={}", encodedPassword.length());

        // 회원 정보 저장
            User savedUser = userRepository.save(user);
            logger.info("회원 등록 완료: userId={}", savedUser.getIdType().getUserId());
        } catch (DataAccessException e) {
            logger.error("데이터베이스 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("회원 정보 저장 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            logger.error("회원 등록 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("회원 등록 중 오류가 발생했습니다.", e);
        }
    }
    
    // 아이디 중복 체크 메서드
    @Transactional(readOnly = true)
    public boolean isUserIdExists(String userId) {
        try {
            logger.info("아이디 중복 확인: {}", userId);
            boolean exists = userRepository.existsByUserId(userId);
            logger.info("아이디 존재 여부: {}", exists);
            return exists;
        } catch (Exception e) {
            logger.error("아이디 중복 확인 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("아이디 중복 확인 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 이메일로 사용자 아이디 찾기
     */
    @Transactional(readOnly = true)
    public String findUserIdByEmail(String email) {
        try {
            logger.info("이메일로 사용자 아이디 찾기: {}", email);
            return userRepository.findUserIdByEmail(email).orElse(null);
        } catch (Exception e) {
            logger.error("이메일로 사용자 아이디 찾기 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("사용자 아이디 찾기 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 사용자 아이디와 이메일 일치 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean validateUserIdAndEmail(String userId, String email) {
        try {
            logger.info("사용자 아이디와 이메일 검증: userId={}, email={}", userId, email);
            Optional<User> userOpt = userRepository.findByUserId(userId);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                logger.info("사용자 찾음: userId={}, 저장된 이메일={}, 입력 이메일={}", 
                        userId, user.getUserEmail(), email);
                boolean match = email.equals(user.getUserEmail());
                logger.info("이메일 일치 여부: {}", match);
                return match;
            } else {
                logger.warn("사용자 ID를 찾을 수 없음: {}", userId);
            }
            
            return false;
        } catch (Exception e) {
            logger.error("사용자 인증 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("사용자 인증 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 비밀번호 재설정
     */
    @Transactional
    public void updatePassword(String userId, String newPassword) {
        try {
            logger.info("비밀번호 재설정: userId={}", userId);
            
            Optional<User> userOpt = userRepository.findByUserId(userId);
            if (!userOpt.isPresent()) {
                logger.error("사용자 ID를 찾을 수 없음: {}", userId);
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }
            
            User user = userOpt.get();
            
            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(newPassword);
            
            // 암호화된 비밀번호가 데이터베이스 컬럼 크기를 초과하는지 확인
            if (encodedPassword.length() > MAX_PASSWORD_LENGTH) {
                logger.warn("암호화된 비밀번호 길이가 너무 깁니다: {} 문자", encodedPassword.length());
                encodedPassword = encodedPassword.substring(0, MAX_PASSWORD_LENGTH);
            }
            
            user.setUserPwd(encodedPassword);
            userRepository.save(user);
            logger.info("비밀번호 재설정 완료: userId={}", userId);
        } catch (Exception e) {
            logger.error("비밀번호 재설정 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("비밀번호 재설정 중 오류가 발생했습니다.", e);
        }
    }
}
