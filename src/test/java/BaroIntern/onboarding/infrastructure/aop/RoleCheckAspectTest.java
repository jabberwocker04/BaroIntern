package BaroIntern.onboarding.infrastructure.aop;

import BaroIntern.onboarding.application.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
public class RoleCheckAspectTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private RoleCheckAspect roleCheckAspect;

    @BeforeEach
    void setUp(){
        roleCheckAspect = new RoleCheckAspect(request, jwtUtil);
    }

    @Test
    @DisplayName("옳바른 JWT와 옳바른 역할이 match 될 경우 RoleCheck 성공")
    void checkRole_Success() {
        // Given
        String token = "Bearer validToken";
        String userRole = "ROLE_ADMIN";
        RequireRole mockAnnotation = Mockito.mock(RequireRole.class);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserAuthoritiesFromToken("validToken")).thenReturn(userRole);
        when(mockAnnotation.value()).thenReturn("ROLE_ADMIN");

        // When & Then
        assertThatCode(() -> roleCheckAspect.checkRole(mockAnnotation)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("JWT에서 추출한 역할이 허용된 역할과 다를 경우 SecurityException 발생")
    void checkRole_Fail_InvalidRole() {
        // Given
        String token = "Bearer validToken";
        String userRole = "ROLE_USER"; // ROLE_ADMIN이 필요하지만, JWT는 ROLE_USER임
        RequireRole mockAnnotation = Mockito.mock(RequireRole.class);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserAuthoritiesFromToken("validToken")).thenReturn(userRole);
        when(mockAnnotation.value()).thenReturn("ROLE_ADMIN");

        // When & Then
        assertThatThrownBy(() -> roleCheckAspect.checkRole(mockAnnotation))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("권한이 없습니다.");
    }

}
