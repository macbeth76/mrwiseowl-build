package net.wiseowltech.mrwiseowl.mrwiseowl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyGroupDto {
    
    private Long id;
    private String name;
    private String type;
    private Long primaryUserId;
    private String primaryUserName;
    private String description;
    private List<FamilyMemberDto> members;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer memberCount;
    private Integer adultCount;
    private Integer childCount;
    private Boolean hasPreferences;
}
