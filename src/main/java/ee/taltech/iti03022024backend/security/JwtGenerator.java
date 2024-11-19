package ee.taltech.iti03022024backend.security;

import ee.taltech.iti03022024backend.entities.user.UserEntity;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtGenerator {
    private final SecretKey key;

    public JwtGenerator(SecretKey key) {
        this.key = key; // injected by spring automatically
    }

    public String generateToken(UserEntity userEntity) {
        return Jwts.builder()
                .subject(userEntity.getUsername())
                .claims(Map.of(
                        "userId", userEntity.getUserId() // Can add information here
                ))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // 24 hours
                .signWith(key)
                .compact();
    }
}
