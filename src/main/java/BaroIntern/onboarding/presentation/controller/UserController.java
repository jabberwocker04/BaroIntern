package BaroIntern.onboarding.presentation.controller;

import BaroIntern.onboarding.application.dto.LoginResDto;
import BaroIntern.onboarding.application.dto.SignUpResDto;
import BaroIntern.onboarding.application.service.UserService;
import BaroIntern.onboarding.infrastructure.aop.RequireRole;
import BaroIntern.onboarding.presentation.dto.LoginReqDto;
import BaroIntern.onboarding.presentation.dto.SignUpReqDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "회원가입, 로그인, 관리자 페이지 접근 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResDto> signUp(
            @RequestBody @Valid SignUpReqDto request) {

        return ResponseEntity.ok(userService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(
            @RequestBody @Valid LoginReqDto request) {

        return ResponseEntity.ok(userService.login(request));
    }

    //회원 정보 확인
    @RequireRole("ROLE_ADMIN")
    @GetMapping("/admin")
    public ResponseEntity<String> adminCheck(
    ) {
        return ResponseEntity.ok("관리자 페이지 접근");
    }

}
