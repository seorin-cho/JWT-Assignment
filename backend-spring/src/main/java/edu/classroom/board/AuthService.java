package edu.classroom.board;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final Users users;

  public AuthService(Users users) {
    this.users = users;
  }

  @Value("${app.jwt.secret}")
  private String jwtSecret;

  @Value("${app.jwt.issuer}")
  private String jwtIssuer;

  @Value("${app.jwt.audience}")
  private String jwtAudience;

  @Value("${app.jwt.ttl}")
  private long jwtTtl;

  // BE-01: 로그인 성공 시 HS256 JWT 발급
  public String issueToken(Users.PublicUser user) {
    if (jwtSecret == null || !jwtSecret.matches("[0-9a-fA-F]{64}")) {
      throw new IllegalStateException("JWT_SECRET은 64자리 16진수여야 합니다.");
    }

    byte[] keyBytes = new byte[32];
    for (int i = 0; i < 32; i++) {
      keyBytes[i] = (byte) Integer.parseInt(jwtSecret.substring(i * 2, i * 2 + 2), 16);
    }

    SecretKey key = Keys.hmacShaKeyFor(keyBytes);

    Instant now = Instant.now();

    return Jwts.builder()
        .subject(String.valueOf(user.id()))
        .issuer(jwtIssuer)
        .audience().add(jwtAudience).and()
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(jwtTtl)))
        .signWith(key, Jwts.SIG.HS256)
        .compact();
  }

  // BE-02: 다음 단계에서 구현
  
public long currentUserId(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");

    // 1. Authorization 헤더 확인
    if (authorization == null || authorization.isBlank()) {
        throw new ApiError(401, "TOKEN_REQUIRED", "로그인 토큰이 필요합니다.");
    }

    // 2. Bearer 형식 확인
    if (!authorization.startsWith("Bearer ")
            || authorization.substring(7).isBlank()) {
        throw new ApiError(401, "INVALID_TOKEN", "Bearer 토큰 형식이 올바르지 않습니다.");
    }

    String token = authorization.substring(7).trim();

    // 3. JWT 비밀 키 확인 및 변환
    if (jwtSecret == null || !jwtSecret.matches("[0-9a-fA-F]{64}")) {
        throw new IllegalStateException("JWT_SECRET 설정이 올바르지 않습니다.");
    }

    byte[] keyBytes = new byte[32];
    for (int i = 0; i < 32; i++) {
        keyBytes[i] = (byte) Integer.parseInt(
            jwtSecret.substring(i * 2, i * 2 + 2), 16
        );
    }

    SecretKey key = Keys.hmacShaKeyFor(keyBytes);

    try {
        // 4. JWT 서명과 만료 시간 검증
        var jwt = Jwts.parser()
            .verifyWith(key)
            .requireIssuer(jwtIssuer)
            .requireAudience(jwtAudience)
            .build()
            .parseSignedClaims(token);

        // 5. HS256 알고리즘 확인
        if (!"HS256".equals(jwt.getHeader().getAlgorithm())) {
            throw new ApiError(401, "INVALID_TOKEN", "JWT 알고리즘이 올바르지 않습니다.");
        }

        Claims claims = jwt.getPayload();

        // 6. 필수 클레임 확인
        String subject = claims.getSubject();

        if (subject == null || subject.isBlank()
                || claims.getExpiration() == null) {
            throw new ApiError(401, "INVALID_TOKEN", "JWT 필수 정보가 없습니다.");
        }

        // 7. 사용자 ID 추출
        long userId;
        try {
            userId = Long.parseLong(subject);
        } catch (NumberFormatException e) {
            throw new ApiError(401, "INVALID_TOKEN", "사용자 ID가 올바르지 않습니다.");
        }

        // 8. 실제 사용자 존재 여부 확인
        if (users.find(userId) == null) {
            throw new ApiError(401, "INVALID_TOKEN", "존재하지 않는 사용자입니다.");
        }

        return userId;

    } catch (ExpiredJwtException e) {
        throw new ApiError(401, "TOKEN_EXPIRED", "로그인 토큰이 만료되었습니다.");
    } catch (JwtException | IllegalArgumentException e) {
        throw new ApiError(401, "INVALID_TOKEN", "유효하지 않은 로그인 토큰입니다.");
    }
}


  // BE-03: 다음 단계에서 구현
  
public void assertOwner(long userId, long authorId) {
    if (userId != authorId) {
        throw new ApiError(
            403,
            "FORBIDDEN",
            "본인이 작성한 게시글만 수정하거나 삭제할 수 있습니다."
        );
    }
}
}