package net.wiseowltech.mrwiseowl.mrwiseowl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPreferenceDto {
    
    private Long id;
    private Long familyMemberId;
    private String category;
    private String preferenceKey;
    private String preferenceValue;
    private String importance;
    private String notes;
    private LocalDateTime createdAt;
    private String displayName;
    private String icon;
    private String description;
}
