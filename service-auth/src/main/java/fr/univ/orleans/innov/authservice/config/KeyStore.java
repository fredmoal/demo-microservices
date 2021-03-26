package fr.univ.orleans.innov.authservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
public class KeyStore {
    private static final Logger log = LoggerFactory.getLogger(KeyStore.class);

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public KeyStore() {
        // génération des cles RSA privées et publiques
        KeyPairGenerator keyGenerator = null;
        try {
            keyGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            log.error("RSA algorithm not found");
        }
        keyGenerator.initialize(2048);

        KeyPair kp = keyGenerator.genKeyPair();
        publicKey = (RSAPublicKey)kp.getPublic();
        privateKey = (RSAPrivateKey)kp.getPrivate();

    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }
}
