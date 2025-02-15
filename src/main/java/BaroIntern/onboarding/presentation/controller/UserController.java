package BaroIntern.onboarding.presentation.controller;

import BaroIntern.onboarding.application.dto.SignUpResDto;
import BaroIntern.onboarding.application.service.UserService;
import BaroIntern.onboarding.presentation.dto.SignUpReqDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResDto> signUp(
            @RequestBody @Valid SignUpReqDto request) {

        return ResponseEntity.ok(userService.signUp(request));
    }

}
