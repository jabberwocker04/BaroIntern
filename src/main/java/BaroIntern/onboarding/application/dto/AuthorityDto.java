package BaroIntern.onboarding.application.dto;

import BaroIntern.onboarding.domain.entity.Authorities;

public record AuthorityDto(
        String authorityName
) {
    public static AuthorityDto from(Authorities authority) {
        return new AuthorityDto (authority.getAuthorityName()); // Enum의 authorityName을 DTO로 변환
    }
}