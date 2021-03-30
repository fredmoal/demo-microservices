package fr.univ.orleans.innov.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@EnableWebFlux
@SpringBootApplication
public class MessageServiceApplication {
    @Value("${spring.cloud.consul.host}")
    private String consulHost;
    @Value("${spring.cloud.consul.port}")
    private String consulPort;

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApplication.class, args);
    }


    @Bean
    PublicKey getPublicKeyFromConsul() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // récupération de la clé publique dans le Key/Value store de Consul
        final WebClient webClientLogin = WebClient.create("http://"+consulHost+":"+consulPort);

        ClientResponse response = webClientLogin
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/kv/publicKey")
                        .queryParam("raw",true)
                        .build())
                .exchange().block();
        String publicKey = response.bodyToMono(String.class).block();

        if (publicKey==null) {
            // no public key : STOP !
            logger.error("public Key not available : shutdown");
            ConfigurableApplicationContext ctx = new SpringApplicationBuilder(MessageServiceApplication.class)
                    .web(WebApplicationType.NONE).run();
            SpringApplication.exit(ctx, () -> 0);
            return null;
        }

        logger.info("public key from consul : "+publicKey);
        // decode public key from String
        byte[] publicBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        return pubKey;
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {

        };
    }
}