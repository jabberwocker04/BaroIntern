package BaroIntern.onboarding.infrastructure.config.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {

    private final HttpServletRequest request;

    @Before("@annotation(requireRole)")
    public void checkRole(RequireRole requireRole) {
        // 헤더에서 role 정보 가져오기
        String role = (String) request.getAttribute("role");
        if (role == null || !role.equals(requireRole.value())) {
            throw new SecurityException("권한이 없습니다.");
        }
    }
}
