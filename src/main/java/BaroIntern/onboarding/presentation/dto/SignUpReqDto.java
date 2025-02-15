package BaroIntern.onboarding.presentation.dto;

import BaroIntern.onboarding.application.dto.SignUpDto;
import jakarta.validation.constraints.*;

public record SignUpReqDto(

        @NotBlank(message = "user name은 비워둘 수 없습니다.")
        @Size(min = 4, max = 15, message = "user name는 최소 4자 이상, 최대 15자 이하입니다.")
        @Pattern(regexp = "^[a-z0-9\\s]+$", message = "user name은 알파벳 소문자(a~z), 숫자(0~9) 및 공백만 포함할 수 있습니다.")
        String username,

        @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
        @Size(min = 4, max = 15, message = "비밀번호는 최소 4자 이상, 최대 15자 이하입니다.")
        String password,

        @NotBlank(message = "닉네임은 비워둘 수 없습니다.")
        @Size(min = 2, max = 15, message = "닉네임은 최소 2자 이상, 최대 15자 이하입니다.")
        String nickname
) {

    public SignUpDto toServiceDto () {
        return new SignUpDto(
                username,
                password,
                nickname
        );
    }
}
