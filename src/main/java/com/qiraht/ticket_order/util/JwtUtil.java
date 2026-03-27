package com.qiraht.ticket_order.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${app.jwt.secret")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    private Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .withClaim("roles", userDetails.getAuthorities().stream()
                        .map(Object::toString)
                        .toList())
                .sign(algorithm());
    }

    public String extractUsername(String token) {
        return JWT.require(algorithm())
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = userDetails.getUsername();
        Date expiresAt = JWT.require(algorithm())
                .build()
                .verify(token).getExpiresAt();

        return username.equals(userDetails.getUsername()) && expiresAt.after(new Date());
    }
}
