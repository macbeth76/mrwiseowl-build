package net.wiseowltech.mrwiseowl.mrwiseowl.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column
    private String passwordHash; // Nullable for Google OAuth users
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "google_id")
    private String googleId;
    
    @Column(name = "picture_url")
    private String pictureUrl;
    
    @Column(name = "auth_provider")
    private String authProvider; // 'local' or 'google'
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
