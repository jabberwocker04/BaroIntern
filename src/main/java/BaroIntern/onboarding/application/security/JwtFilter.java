package BaroIntern.onboarding.application.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = req.getHeader("Authorization");

        if(tokenValue != null && tokenValue.startsWith("Bearer ")) {
            tokenValue = tokenValue.substring(7);

            if(jwtUtil.validateToken(tokenValue)) {
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                req.setAttribute("username", info.get("username"));
                req.setAttribute("Authorities", info.get("authorities"));
            }
        }

        filterChain.doFilter(req, res);
    }
}
