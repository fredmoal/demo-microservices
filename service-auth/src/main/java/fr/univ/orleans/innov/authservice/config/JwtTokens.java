package fr.univ.orleans.innov.authservice.config;

import fr.univ.orleans.innov.authservice.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokens {
    public static final String TOKEN_PREFIX = "Bearer ";
    @Value("${token.expiration_time_millis}")
    private long EXPIRATION_TIME;

    @Autowired
    private KeyStore keyStore;

    public String genereToken(User user) {
        String username = user.getUsername();
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", user.getRoles());
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(keyStore.getPrivateKey())
                .compact();
        return token;
    }
}
