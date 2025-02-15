package BaroIntern.onboarding.application.service;

import BaroIntern.onboarding.application.dto.AuthorityDto;
import BaroIntern.onboarding.application.dto.SignUpResDto;
import BaroIntern.onboarding.domain.entity.Authorities;
import BaroIntern.onboarding.domain.entity.User;
import BaroIntern.onboarding.domain.repository.UserRepository;
import BaroIntern.onboarding.presentation.dto.SignUpReqDto;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public SignUpResDto signUp(SignUpReqDto singUpDto) {



        User user = User.create(
                singUpDto.username(),
                singUpDto.password(),
                singUpDto.nickname(),
                Authorities.USER
        );

        userRepository.save(user);

        return new SignUpResDto(user.getUsername(), user.getNickname(), List.of(AuthorityDto.from(user.getRole())));
    }

}
