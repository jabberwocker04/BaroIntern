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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResDto signUp(SignUpReqDto singUpDto) {

        if(userRepository.findByUsername(singUpDto.username()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

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
                .orElseThrow(() -> new UsernameNotFoundException(username+"은 존재하지 않습니다."));

        String token = jwtUtil.generateToken(username, user.getRole().toString());

        return new LoginResDto(token);

    }
}
