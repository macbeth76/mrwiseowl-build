package net.wiseowltech.mrwiseowl.mrwiseowl.service;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.User;
import net.wiseowltech.mrwiseowl.mrwiseowl.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Value("${jwt.secret:mrwiseowl-secret-key-change-in-production}")
    private String jwtSecret;

    public String register(String email, String password, String name) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setName(name);
        user.setAuthProvider("local");
        
        user = userRepository.save(user);
        
        return generateToken(user);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return generateToken(user);
    }
    
    public User findOrCreateGoogleUser(String email, String name, String pictureUrl, String googleId) {
        return userRepository.findByEmail(email)
            .map(existingUser -> {
                // Update existing user with Google info
                existingUser.setGoogleId(googleId);
                existingUser.setPictureUrl(pictureUrl);
                existingUser.setAuthProvider("google");
                existingUser.setLastLogin(LocalDateTime.now());
                return userRepository.save(existingUser);
            })
            .orElseGet(() -> {
                // Create new user
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setGoogleId(googleId);
                newUser.setPictureUrl(pictureUrl);
                newUser.setAuthProvider("google");
                newUser.setPasswordHash(null); // No password for Google users
                return userRepository.save(newUser);
            });
    }

    public String generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("name", user.getName())
                .claim("picture", user.getPictureUrl())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            return Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("userId", Long.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }
}
