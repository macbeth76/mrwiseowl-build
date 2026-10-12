package net.wiseowltech.mrwiseowl.mrwiseowl.controller;

import net.wiseowltech.mrwiseowl.mrwiseowl.dto.FamilyGroupDto;
import net.wiseowltech.mrwiseowl.mrwiseowl.dto.FamilyMemberDto;
import net.wiseowltech.mrwiseowl.mrwiseowl.dto.MemberPreferenceDto;
import net.wiseowltech.mrwiseowl.mrwiseowl.service.AuthService;
import net.wiseowltech.mrwiseowl.mrwiseowl.service.FamilyProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
@Slf4j
public class FamilyProfileController {
    
    private final FamilyProfileService familyProfileService;
    private final AuthService authService;
    
    @GetMapping("/groups")
    public ResponseEntity<List<FamilyGroupDto>> getFamilyGroups(
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        List<FamilyGroupDto> groups = familyProfileService.getFamilyGroupsByUserId(userId);
        return ResponseEntity.ok(groups);
    }
    
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<FamilyGroupDto> getFamilyGroup(
            @PathVariable Long groupId,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        FamilyGroupDto group = familyProfileService.getFamilyGroupById(groupId, userId);
        return ResponseEntity.ok(group);
    }
    
    @PostMapping("/groups")
    public ResponseEntity<FamilyGroupDto> createFamilyGroup(
            @RequestBody FamilyGroupDto dto,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        FamilyGroupDto group = familyProfileService.createFamilyGroup(dto, userId);
        return ResponseEntity.ok(group);
    }
    
    @PutMapping("/groups/{groupId}")
    public ResponseEntity<FamilyGroupDto> updateFamilyGroup(
            @PathVariable Long groupId,
            @RequestBody FamilyGroupDto dto,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        FamilyGroupDto group = familyProfileService.updateFamilyGroup(groupId, dto, userId);
        return ResponseEntity.ok(group);
    }
    
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<Map<String, String>> deleteFamilyGroup(
            @PathVariable Long groupId,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        familyProfileService.deleteFamilyGroup(groupId, userId);
        return ResponseEntity.ok(Map.of("message", "Family group deleted successfully"));
    }
    
    @PostMapping("/groups/{groupId}/members")
    public ResponseEntity<FamilyMemberDto> addFamilyMember(
            @PathVariable Long groupId,
            @RequestBody FamilyMemberDto dto,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        FamilyMemberDto member = familyProfileService.addFamilyMember(groupId, dto, userId);
        return ResponseEntity.ok(member);
    }
    
    @PutMapping("/members/{memberId}")
    public ResponseEntity<FamilyMemberDto> updateFamilyMember(
            @PathVariable Long memberId,
            @RequestBody FamilyMemberDto dto,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        FamilyMemberDto member = familyProfileService.updateFamilyMember(memberId, dto, userId);
        return ResponseEntity.ok(member);
    }
    
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Map<String, String>> deleteFamilyMember(
            @PathVariable Long memberId,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        familyProfileService.deleteFamilyMember(memberId, userId);
        return ResponseEntity.ok(Map.of("message", "Family member deleted successfully"));
    }
    
    @GetMapping("/members/{memberId}/preferences")
    public ResponseEntity<List<MemberPreferenceDto>> getMemberPreferences(
            @PathVariable Long memberId,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        List<MemberPreferenceDto> preferences = familyProfileService.getMemberPreferences(memberId, userId);
        return ResponseEntity.ok(preferences);
    }
    
    @PostMapping("/members/{memberId}/preferences")
    public ResponseEntity<MemberPreferenceDto> savePreference(
            @PathVariable Long memberId,
            @RequestBody MemberPreferenceDto dto,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        MemberPreferenceDto preference = familyProfileService.savePreference(memberId, dto, userId);
        return ResponseEntity.ok(preference);
    }
    
    @DeleteMapping("/preferences/{preferenceId}")
    public ResponseEntity<Map<String, String>> deletePreference(
            @PathVariable Long preferenceId,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromAuth(authHeader);
        familyProfileService.deletePreference(preferenceId, userId);
        return ResponseEntity.ok(Map.of("message", "Preference deleted successfully"));
    }
    
    private Long getUserIdFromAuth(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return authService.getUserIdFromToken(token);
    }
}
