package com.dita.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User_id_type implements Serializable{
	
    private String user_id;
    private String user_type;

    // equals() & hashCode()는 반드시 오버라이드해야 함
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User_id_type)) return false;
        User_id_type userId = (User_id_type) o;
        return Objects.equals(user_id, userId.user_id) &&
               Objects.equals(user_type, userId.user_type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, user_type);
    }
}
