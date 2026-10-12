package net.wiseowltech.mrwiseowl.mrwiseowl.service;

import net.wiseowltech.mrwiseowl.mrwiseowl.dto.*;
import net.wiseowltech.mrwiseowl.mrwiseowl.exception.ResourceNotFoundException;
import net.wiseowltech.mrwiseowl.mrwiseowl.model.*;
import net.wiseowltech.mrwiseowl.mrwiseowl.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FamilyProfileService {
    
    private final FamilyGroupRepository familyGroupRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final MemberPreferenceRepository memberPreferenceRepository;
    private final UserRepository userRepository;
    
    public List<FamilyGroupDto> getFamilyGroupsByUserId(Long userId) {
        return familyGroupRepository.findAllByUserId(userId).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    public FamilyGroupDto getFamilyGroupById(Long groupId, Long userId) {
        FamilyGroup group = familyGroupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Family group not found"));
        
        if (!hasAccess(group, userId)) {
            throw new ResourceNotFoundException("Access denied");
        }
        
        return toDto(group);
    }
    
    public FamilyGroupDto createFamilyGroup(FamilyGroupDto dto, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        FamilyGroup group = FamilyGroup.builder()
            .name(dto.getName())
            .type(dto.getType())
            .description(dto.getDescription())
            .primaryUser(user)
            .build();
        
        group = familyGroupRepository.save(group);
        
        FamilyMember self = FamilyMember.builder()
            .familyGroup(group)
            .user(user)
            .name(user.getName())
            .relationship("self")
            .ageRange("adult")
            .avatarUrl(user.getPictureUrl())
            .build();
        
        familyMemberRepository.save(self);
        
        log.info("Created family group: {}", group.getName());
        return toDto(group);
    }
    
    public FamilyGroupDto updateFamilyGroup(Long groupId, FamilyGroupDto dto, Long userId) {
        FamilyGroup group = familyGroupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Family group not found"));
        
        if (!group.getPrimaryUser().getId().equals(userId)) {
            throw new RuntimeException("Only primary user can update");
        }
        
        group.setName(dto.getName());
        group.setDescription(dto.getDescription());
        
        return toDto(familyGroupRepository.save(group));
    }
    
    public void deleteFamilyGroup(Long groupId, Long userId) {
        FamilyGroup group = familyGroupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Family group not found"));
        
        if (!group.getPrimaryUser().getId().equals(userId)) {
            throw new RuntimeException("Only primary user can delete");
        }
        
        familyGroupRepository.delete(group);
    }
    
    public FamilyMemberDto addFamilyMember(Long groupId, FamilyMemberDto dto, Long userId) {
        FamilyGroup group = familyGroupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Family group not found"));
        
        if (!hasAccess(group, userId)) {
            throw new RuntimeException("Access denied");
        }
        
        FamilyMember member = FamilyMember.builder()
            .familyGroup(group)
            .name(dto.getName())
            .relationship(dto.getRelationship())
            .ageRange(dto.getAgeRange())
            .dateOfBirth(dto.getDateOfBirth())
            .avatarUrl(dto.getAvatarUrl())
            .gender(dto.getGender())
            .build();
        
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            member.setUser(user);
        }
        
        return toMemberDto(familyMemberRepository.save(member));
    }
    
    public FamilyMemberDto updateFamilyMember(Long memberId, FamilyMemberDto dto, Long userId) {
        FamilyMember member = familyMemberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        
        if (!hasAccess(member.getFamilyGroup(), userId)) {
            throw new RuntimeException("Access denied");
        }
        
        member.setName(dto.getName());
        member.setAgeRange(dto.getAgeRange());
        member.setDateOfBirth(dto.getDateOfBirth());
        member.setAvatarUrl(dto.getAvatarUrl());
        member.setGender(dto.getGender());
        
        return toMemberDto(familyMemberRepository.save(member));
    }
    
    public void deleteFamilyMember(Long memberId, Long userId) {
        FamilyMember member = familyMemberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        
        if (!hasAccess(member.getFamilyGroup(), userId)) {
            throw new RuntimeException("Access denied");
        }
        
        if ("self".equals(member.getRelationship())) {
            throw new RuntimeException("Cannot delete self");
        }
        
        familyMemberRepository.delete(member);
    }
    
    public List<MemberPreferenceDto> getMemberPreferences(Long memberId, Long userId) {
        FamilyMember member = familyMemberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        
        if (!hasAccess(member.getFamilyGroup(), userId)) {
            throw new RuntimeException("Access denied");
        }
        
        return memberPreferenceRepository.findByFamilyMemberId(memberId).stream()
            .map(this::toPrefDto)
            .collect(Collectors.toList());
    }
    
    public MemberPreferenceDto savePreference(Long memberId, MemberPreferenceDto dto, Long userId) {
        FamilyMember member = familyMemberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        
        if (!hasAccess(member.getFamilyGroup(), userId)) {
            throw new RuntimeException("Access denied");
        }
        
        MemberPreference pref = memberPreferenceRepository
            .findByMemberIdAndCategoryAndKey(memberId, dto.getCategory(), dto.getPreferenceKey())
            .orElse(MemberPreference.builder()
                .familyMember(member)
                .category(dto.getCategory())
                .preferenceKey(dto.getPreferenceKey())
                .build());
        
        pref.setPreferenceValue(dto.getPreferenceValue());
        pref.setImportance(dto.getImportance());
        pref.setNotes(dto.getNotes());
        
        return toPrefDto(memberPreferenceRepository.save(pref));
    }
    
    public void deletePreference(Long preferenceId, Long userId) {
        MemberPreference pref = memberPreferenceRepository.findById(preferenceId)
            .orElseThrow(() -> new ResourceNotFoundException("Preference not found"));
        
        if (!hasAccess(pref.getFamilyMember().getFamilyGroup(), userId)) {
            throw new RuntimeException("Access denied");
        }
        
        memberPreferenceRepository.delete(pref);
    }
    
    private boolean hasAccess(FamilyGroup group, Long userId) {
        if (group.getPrimaryUser().getId().equals(userId)) {
            return true;
        }
        return group.getMembers().stream()
            .anyMatch(m -> m.getUser() != null && m.getUser().getId().equals(userId));
    }
    
    private FamilyGroupDto toDto(FamilyGroup group) {
        List<FamilyMemberDto> members = group.getMembers() != null ?
            group.getMembers().stream().map(this::toMemberDto).collect(Collectors.toList()) : List.of();
        
        long adults = members.stream().filter(m -> "adult".equals(m.getAgeRange())).count();
        long children = members.stream().filter(m -> !"adult".equals(m.getAgeRange())).count();
        
        return FamilyGroupDto.builder()
            .id(group.getId())
            .name(group.getName())
            .type(group.getType())
            .primaryUserId(group.getPrimaryUser().getId())
            .primaryUserName(group.getPrimaryUser().getName())
            .description(group.getDescription())
            .members(members)
            .createdAt(group.getCreatedAt())
            .updatedAt(group.getUpdatedAt())
            .memberCount(members.size())
            .adultCount((int) adults)
            .childCount((int) children)
            .hasPreferences(members.stream().anyMatch(m -> m.getPreferenceCount() != null && m.getPreferenceCount() > 0))
            .build();
    }
    
    private FamilyMemberDto toMemberDto(FamilyMember member) {
        List<MemberPreferenceDto> prefs = member.getPreferences() != null ?
            member.getPreferences().stream().map(this::toPrefDto).collect(Collectors.toList()) : List.of();
        
        Integer age = member.getDateOfBirth() != null ?
            Period.between(member.getDateOfBirth(), LocalDate.now()).getYears() : null;
        
        return FamilyMemberDto.builder()
            .id(member.getId())
            .familyGroupId(member.getFamilyGroup().getId())
            .userId(member.getUser() != null ? member.getUser().getId() : null)
            .name(member.getName())
            .relationship(member.getRelationship())
            .ageRange(member.getAgeRange())
            .dateOfBirth(member.getDateOfBirth())
            .avatarUrl(member.getAvatarUrl())
            .gender(member.getGender())
            .preferences(prefs)
            .createdAt(member.getCreatedAt())
            .age(age)
            .hasAccount(member.getUser() != null)
            .preferenceCount(prefs.size())
            .build();
    }
    
    private MemberPreferenceDto toPrefDto(MemberPreference pref) {
        return MemberPreferenceDto.builder()
            .id(pref.getId())
            .familyMemberId(pref.getFamilyMember().getId())
            .category(pref.getCategory())
            .preferenceKey(pref.getPreferenceKey())
            .preferenceValue(pref.getPreferenceValue())
            .importance(pref.getImportance())
            .notes(pref.getNotes())
            .createdAt(pref.getCreatedAt())
            .displayName(getDisplayName(pref.getPreferenceKey()))
            .icon(getIcon(pref.getPreferenceKey()))
            .build();
    }
    
    private String getDisplayName(String key) {
        return switch (key) {
            case "breweries" -> "Breweries";
            case "museums" -> "Museums";
            case "hiking" -> "Hiking";
            case "playgrounds" -> "Playgrounds";
            case "gluten_free" -> "Gluten-Free";
            case "vegan" -> "Vegan";
            default -> key.replace("_", " ");
        };
    }
    
    private String getIcon(String key) {
        return switch (key) {
            case "breweries" -> "🍺";
            case "museums" -> "🎨";
            case "hiking" -> "🥾";
            case "playgrounds" -> "🎪";
            case "gluten_free" -> "🌾";
            case "vegan" -> "🌱";
            default -> "⭐";
        };
    }
}
