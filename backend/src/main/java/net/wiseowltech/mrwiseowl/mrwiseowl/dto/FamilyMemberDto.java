package net.wiseowltech.mrwiseowl.mrwiseowl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberDto {
    
    private Long id;
    private Long familyGroupId;
    private Long userId;
    private String name;
    private String relationship;
    private String ageRange;
    private LocalDate dateOfBirth;
    private String avatarUrl;
    private String gender;
    private List<MemberPreferenceDto> preferences;
    private LocalDateTime createdAt;
    private Integer age;
    private Boolean hasAccount;
    private Integer preferenceCount;
}
