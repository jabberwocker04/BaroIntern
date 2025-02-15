package BaroIntern.onboarding.application.service;

import BaroIntern.onboarding.application.dto.AuthorityDto;
import BaroIntern.onboarding.application.dto.LoginResDto;
import BaroIntern.onboarding.application.dto.SignUpResDto;
import BaroIntern.onboarding.application.security.JwtUtil;
import BaroIntern.onboarding.domain.entity.Authorities;
import BaroIntern.onboarding.domain.entity.User;
import BaroIntern.onboarding.domain.repository.UserRepository;
import BaroIntern.onboarding.presentation.dto.LoginReqDto;
import BaroIntern.onboarding.presentation.dto.SignUpReqDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResDto signUp(SignUpReqDto singUpDto) {

        User user = User.create(
                singUpDto.username(),
                passwordEncoder.encode(singUpDto.password()),
                singUpDto.nickname(),
                Authorities.USER
        );
        userRepository.save(user);

        return new SignUpResDto(user.getUsername(), user.getNickname(), List.of(AuthorityDto.from(user.getRole())));
    }


    public LoginResDto login(@Valid LoginReqDto loginReqDto) {

        String username = loginReqDto.username();
        String password = loginReqDto.password();

        User user = userRepository.findByUsername(loginReqDto.username())
                .filter(userInfo -> passwordEncoder.matches(password, userInfo.getPassword()))
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

        String token = jwtUtil.generateToken(username, user.getRole().toString());

        return new LoginResDto(token);

    }
}
