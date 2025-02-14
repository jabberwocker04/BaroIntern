package BaroIntern.onboarding.domain.entity;

import lombok.Getter;

@Getter
public enum Authorities {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String authorityName;

    Authorities(String authorityName) {
        this.authorityName = authorityName;
    }
}