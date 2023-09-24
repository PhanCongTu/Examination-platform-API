package com.example.springboot.security;

import com.example.springboot.dto.TokenDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Sau khi có thông tin về người dùng, chúng ta cần mã hóa thông tin người dùng thành chuỗi JWT.
 * Đó là nhiệm vụ của class này.
 */
@Component
@Slf4j
public class JwtTokenProvider implements Serializable {

    @Value("${jwt.access-token-expiration}")
    private long JWT_TOKEN_VALIDITY;

    // Đoạn secret bí mật, chỉ phía server biết
    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Tạo ra JWT từ thông tin user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }
    public String doGenerateToken(Map<String, Object> claims, String subject) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = Date.from(Instant.now().plus(JWT_TOKEN_VALIDITY, ChronoUnit.MINUTES));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenDetails getTokenDetails(JwtUserDetails userDetails) {
        TokenDetails tokenDetails = new TokenDetails();
        tokenDetails.setDisplayName(userDetails.getDisplayName());
        tokenDetails.setAccessToken(generateToken(userDetails));
        tokenDetails.setEmailAddress(userDetails.getEmailAddress());
        tokenDetails.setExpired(ZonedDateTime.now().plus(JWT_TOKEN_VALIDITY, ChronoUnit.MINUTES));
        tokenDetails.setRoles(userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));
        return tokenDetails;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
