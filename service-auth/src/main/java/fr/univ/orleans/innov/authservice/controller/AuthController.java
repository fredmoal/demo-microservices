package fr.univ.orleans.innov.authservice.controller;

import fr.univ.orleans.innov.authservice.config.JwtTokens;
import fr.univ.orleans.innov.authservice.config.KeyStore;
import fr.univ.orleans.innov.authservice.model.User;
import fr.univ.orleans.innov.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;

import javax.validation.Valid;
import java.util.Base64;
import java.util.Optional;

@RestController
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokens jwtTokens;
    @Autowired
    private KeyStore keyStore;

    record AuthDTO(String username, String password) {};

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody AuthDTO auth) {
        if (auth.username==null||auth.password==null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userRepository.findById(auth.username);
        if (user.isPresent()&&passwordEncoder.matches(auth.password, user.get().getPassword())) {
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, JwtTokens.TOKEN_PREFIX+jwtTokens.genereToken(user.get())).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        if (userRepository.existsById(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        };
        User savedUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), false);
        User registerUser = userRepository.save(savedUser);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, JwtTokens.TOKEN_PREFIX+jwtTokens.genereToken(registerUser))
                .body(registerUser);
    }

}