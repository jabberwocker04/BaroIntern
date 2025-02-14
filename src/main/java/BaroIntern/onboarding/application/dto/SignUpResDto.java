package BaroIntern.onboarding.application.dto;

import BaroIntern.onboarding.domain.entity.Authorities;

import java.util.List;

public record SignUpResDto(
        String username,
        String nickname,
        List<Authorities> authorities
) {
}
