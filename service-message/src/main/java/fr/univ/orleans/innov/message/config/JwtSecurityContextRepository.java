package fr.univ.orleans.innov.message.config;

import fr.univ.orleans.innov.message.config.erreurs.MauvaisTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {
    private static final Logger logger = LoggerFactory.getLogger(JwtSecurityContextRepository.class);
    @Autowired
    private JwtTokens jwtTokens;

    @Override
    public Mono save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono load(ServerWebExchange serverWebExchange) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith(JwtTokens.TOKEN_PREFIX)) {
            String token = authorization.replace(JwtTokens.TOKEN_PREFIX, "");
            final UsernamePasswordAuthenticationToken authentication;
            try {
                authentication = jwtTokens.decodeToken(token);
                return Mono.just(new SecurityContextImpl(authentication));
            } catch (MauvaisTokenException e) {
                logger.warn("Mauvais token : "+e.getMessage());
            }
        }
        return Mono.empty();
    }

}