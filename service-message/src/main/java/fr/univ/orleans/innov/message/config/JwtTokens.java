package fr.univ.orleans.innov.message.config;

import fr.univ.orleans.innov.message.config.erreurs.MauvaisTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokens {
    public static final String TOKEN_PREFIX = "Bearer ";

    // récupération de la clé publique
    @Autowired
    private Key publicKey;

    public UsernamePasswordAuthenticationToken decodeToken(String token) throws MauvaisTokenException {
        // le token a un entete ?
        if (token.startsWith(TOKEN_PREFIX)) {
            token = token.replaceFirst(TOKEN_PREFIX, "");
        }
        try {
            Jws<Claims> jwsClaims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            // Signature vérifiée : le token est fiable
            String login = jwsClaims.getBody().getSubject();
            List<String> roles = jwsClaims.getBody().get("roles",List.class);
            List<SimpleGrantedAuthority> authorities =
                    roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(login,null,authorities);

            return authentication;
        } catch (JwtException e) {
            // mauvais format de token !
            throw new MauvaisTokenException(e.getMessage());
        }
    }
}
