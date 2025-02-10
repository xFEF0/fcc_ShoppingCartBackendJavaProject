package com.xfef0.fccshops.security.jwt;

import com.xfef0.fccshops.security.user.ShopUserDetails;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${auth.token.expirationInMillis}")
    private int expirationTime;
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    public String generateTokenForUser(Authentication authentication) {
        ShopUserDetails shopUserDetails = (ShopUserDetails) authentication.getPrincipal();
        List<String> roles = shopUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(shopUserDetails.getEmail())
                .claim("id", shopUserDetails.getId())
                .claim("roles", roles)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(expirationTime, ChronoUnit.MILLIS)))
                .signWith(secretKey)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
