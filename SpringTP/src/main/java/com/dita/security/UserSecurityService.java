package com.dita.security;

import com.dita.domain.User;
import com.dita.domain.User_id_type;
import com.dita.security.UserSecurityDTO;
import com.dita.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Spring Security에서 로그인 시 사용자 정보 로드
     * - 입력받은 userId로 사용자 정보 조회
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        // userId는 복합키의 일부이기 때문에, userId와 userType을 함께 조회해야 함
        // 여기선 userType을 "일반"으로 가정 (관리자용 분리는 추후 추가)
        String userType = "일반";

        Optional<User> userOptional = userRepository
                .findByUserIdAndUserType(userId, userType);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + userId);
        }

        // User 엔티티를 UserSecurityDTO로 변환하여 반환
        return new UserSecurityDTO(userOptional.get());
    }
}
