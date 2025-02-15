package BaroIntern.onboarding.infrastructure.aop;

import BaroIntern.onboarding.application.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {

    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    @Before("@annotation(requireRole)")
    public void checkRole(RequireRole requireRole) {
        // 요청 헤더에서 Authorization 추출
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new SecurityException("권한이 없습니다. JWT 토큰이 필요합니다.");
        }

        token = token.substring(7).trim();

        // 🔥 JWT에서 authorities(권한) 추출
        String userRoles = jwtUtil.getUserAuthoritiesFromToken(token);

        String allowedRoles = requireRole.value();
        log.info("Aop 롤 체크 {}", userRoles);
        log.info("Aop 권한 체크 {}", allowedRoles);

        if (!allowedRoles.contains(userRoles)) {
            throw new SecurityException("권한이 없습니다.");
        }
    }
}
