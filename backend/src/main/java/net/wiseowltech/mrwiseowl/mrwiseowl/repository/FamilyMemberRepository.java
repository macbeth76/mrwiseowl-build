package net.wiseowltech.mrwiseowl.mrwiseowl.repository;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    
    List<FamilyMember> findByFamilyGroupId(Long familyGroupId);
}
