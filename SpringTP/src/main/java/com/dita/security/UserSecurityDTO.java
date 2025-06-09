package com.dita.security;

import com.dita.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserSecurityDTO implements UserDetails {

    private final String userId;
    private final String password;
    private final String userType;

    public UserSecurityDTO(User user) {
        this.userId = user.getIdType().getUserId();
        this.userType = user.getIdType().getUserType();
        this.password = user.getUserPwd();  // 실제 필드명 주의!
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();  // 권한 미사용 시 빈 리스트
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; // 탈퇴 여부 등에 따라 추후 false 처리 가능
    }
}
