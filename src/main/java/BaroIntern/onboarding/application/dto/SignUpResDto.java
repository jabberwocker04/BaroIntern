package BaroIntern.onboarding.application.dto;

import BaroIntern.onboarding.domain.entity.Authorities;
import java.util.List;
import java.util.stream.Collectors;

public record SignUpResDto(
        String username,
        String nickname,
        List<AuthorityDto> authorities
) {
}