package BaroIntern.onboarding.application.service;

import BaroIntern.onboarding.application.dto.LoginResDto;
import BaroIntern.onboarding.application.dto.SignUpResDto;
import BaroIntern.onboarding.application.security.JwtUtil;
import BaroIntern.onboarding.domain.entity.Authorities;
import BaroIntern.onboarding.domain.entity.User;
import BaroIntern.onboarding.domain.repository.UserRepository;
import BaroIntern.onboarding.presentation.dto.LoginReqDto;
import BaroIntern.onboarding.presentation.dto.SignUpReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
public class UserServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_Success() {

        User user = User.create(
                "testUser",
                "12341234",
                "john",
                Authorities.USER
        );


        // Given
        SignUpReqDto signUpReqDto = new SignUpReqDto("testUser", "12341234", "john");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When
        SignUpResDto result = userService.signUp(signUpReqDto);

        // Then
        assertThat(result.username()).isEqualTo("testUser");
        assertThat(result.nickname()).isEqualTo("john");
        assertThat(result.authorities().get(0).authorityName()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("회원가입 실패 -> 중복 유저")
    void signUp_Fail() {

        // Given
        User existingUser = User.create(
                "testUser",
                "12341234",
                "john",
                Authorities.USER
        );

        SignUpReqDto signUpReqDto = new SignUpReqDto("testUser", "12341234", "john");

        when(userRepository.findByUsername(signUpReqDto.username())).thenReturn(Optional.of(existingUser));

        // When
        assertThatThrownBy(() -> userService.signUp(signUpReqDto))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("이미 존재하는 사용자입니다.");

    }

    @Test
    @DisplayName("로그인 성공 Jwt 토큰 발급")
    void login_Success() {
        //given
        LoginReqDto loginReqDto = new LoginReqDto("testUser", "12341234");

        User user = User.create(
                "testUser",
                "12341234",
                "john",
                Authorities.USER
        );

        //when
        when(userRepository.findByUsername(loginReqDto.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginReqDto.password(), user.getPassword())).thenReturn(true); // 패스워드 검증 추가
        when(jwtUtil.generateToken(user.getUsername(), user.getRole().name())).thenReturn("Token");

        LoginResDto resDto = userService.login(loginReqDto);

        //then
        assertNotNull(resDto);
        assertEquals("Token", resDto.token());

    }

}
