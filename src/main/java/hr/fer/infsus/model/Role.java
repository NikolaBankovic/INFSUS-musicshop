package hr.fer.infsus.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN;

    public String getAuthority() {
        return name();
    }
}
