package net.wiseowltech.mrwiseowl.mrwiseowl.repository;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.MemberPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberPreferenceRepository extends JpaRepository<MemberPreference, Long> {
    
    List<MemberPreference> findByFamilyMemberId(Long familyMemberId);
    
    @Query("SELECT mp FROM MemberPreference mp WHERE mp.familyMember.id = :memberId " +
           "AND mp.category = :category AND mp.preferenceKey = :key")
    Optional<MemberPreference> findByMemberIdAndCategoryAndKey(
        @Param("memberId") Long memberId,
        @Param("category") String category,
        @Param("key") String key
    );
}
