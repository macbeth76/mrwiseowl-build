package net.wiseowltech.mrwiseowl.mrwiseowl.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import net.wiseowltech.mrwiseowl.mrwiseowl.model.User;
import net.wiseowltech.mrwiseowl.mrwiseowl.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthController {

    private final AuthService authService;
    
    @Value("${google.client-id:}")
    private String googleClientId;

    @PostMapping("/google")
    public ResponseEntity<Map<String, Object>> googleAuth(@RequestBody Map<String, String> request) {
        String googleToken = request.get("token");
        
        if (googleToken == null || googleToken.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Google token is required"));
        }
        
        try {
            // Verify Google token
            GoogleIdToken.Payload payload = verifyGoogleToken(googleToken);
            
            if (payload == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid Google token"));
            }
            
            // Extract user info from Google
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");
            String googleId = payload.getSubject();
            
            log.info("Google login attempt for email: {}", email);
            
            // Create or update user
            User user = authService.findOrCreateGoogleUser(email, name, picture, googleId);
            
            // Generate JWT
            String jwt = authService.generateToken(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("message", "Google login successful");
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("email", user.getEmail());
            userInfo.put("name", user.getName());
            userInfo.put("picture", user.getPictureUrl());
            userInfo.put("authProvider", user.getAuthProvider());
            response.put("user", userInfo);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Google authentication failed", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Google authentication failed: " + e.getMessage()));
        }
    }
    
    private GoogleIdToken.Payload verifyGoogleToken(String token) {
        try {
            if (googleClientId == null || googleClientId.isEmpty()) {
                log.warn("Google Client ID not configured");
                return null;
            }
            
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), 
                new GsonFactory()
            )
            .setAudience(Collections.singletonList(googleClientId))
            .build();
            
            GoogleIdToken idToken = verifier.verify(token);
            return idToken != null ? idToken.getPayload() : null;
        } catch (Exception e) {
            log.error("Failed to verify Google token", e);
            return null;
        }
    }
}
