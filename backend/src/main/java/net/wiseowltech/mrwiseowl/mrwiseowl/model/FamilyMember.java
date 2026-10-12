package net.wiseowltech.mrwiseowl.mrwiseowl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "family_members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FamilyMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_group_id")
    private FamilyGroup familyGroup;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String relationship;
    
    @Column(name = "age_range")
    private String ageRange;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    private String gender;
    
    @OneToMany(mappedBy = "familyMember", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberPreference> preferences;
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
