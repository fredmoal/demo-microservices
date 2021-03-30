package fr.univ.orleans.innov.authservice;

import fr.univ.orleans.innov.authservice.config.KeyStore;
import fr.univ.orleans.innov.authservice.model.User;
import fr.univ.orleans.innov.authservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Base64;
import java.util.Optional;

@SpringBootApplication
@EnableWebFlux
public class AuthServiceApplication {
    @Value("${spring.cloud.consul.host}")
    private String consulHost;
    @Value("${spring.cloud.consul.port}")
    private String consulPort;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder, KeyStore keyStore) {
        return args -> {
            // init database with 2 Users
            userRepository.save(new User("admin",passwordEncoder.encode("admin"), true));
            userRepository.save(new User("user",passwordEncoder.encode("user"), false));

            // register public key to Key/Value store of Consul
            var encoded = keyStore.getPublicKey().getEncoded();
            String publicKey = Base64.getEncoder().encodeToString(encoded);

            final WebClient client = WebClient.create("http://"+consulHost+":"+consulPort);

            var response = client
                    .put()
                    .uri("/v1/kv/publicKey")
                    .contentType(MediaType.TEXT_PLAIN)
                    .bodyValue(publicKey)
                    .exchange().block();
            logger.info("registering auth public key "+response.statusCode());
                    /*.retrieve()
                    .onStatus(HttpStatus::isError, response -> {
                        logger.error("error registering public key to consul");
                        throw new RuntimeException("error registering public key to consul");
                    });

                     */
        };
    }
}
