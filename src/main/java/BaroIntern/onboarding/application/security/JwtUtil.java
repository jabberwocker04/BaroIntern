package BaroIntern.onboarding.application.security;

import BaroIntern.onboarding.domain.entity.Authorities;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtUtil {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Value("${service.jwt.access-expiration}")
    private long expirationTime;

    private SecretKey key;

    private static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String generateToken(String username, String authorities) {
        return Jwts.builder()
                .claim("username", username)
                .claim("authorities", authorities)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, ALGORITHM)
                .compact();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                log.error("토큰이 없습니다.");
                return false;
            }
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("잘못된 또는 만료된 토큰입니다: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 사용자 정보 추출
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    // 토큰에서 authorities 추출
    public String getUserAuthoritiesFromToken(String token){
        return getUserInfoFromToken(token).get("authorities").toString();
    }

}
