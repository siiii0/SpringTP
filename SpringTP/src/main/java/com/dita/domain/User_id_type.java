package com.dita.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User_id_type implements Serializable {

    @Column(name = "user_id")   // 실제 DB 컬럼명
    private String userId;

    @Column(name = "user_type") // 실제 DB 컬럼명
    private String userType;

    // equals() & hashCode()는 반드시 오버라이드해야 함
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User_id_type)) return false;
        User_id_type that = (User_id_type) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(userType, that.userType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userType);
    }
}
