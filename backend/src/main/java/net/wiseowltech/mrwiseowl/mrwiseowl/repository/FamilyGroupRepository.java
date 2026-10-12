package net.wiseowltech.mrwiseowl.mrwiseowl.repository;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.FamilyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyGroupRepository extends JpaRepository<FamilyGroup, Long> {
    
    @Query("SELECT fg FROM FamilyGroup fg WHERE fg.primaryUser.id = :userId")
    List<FamilyGroup> findByPrimaryUserId(@Param("userId") Long userId);
    
    @Query("SELECT fg FROM FamilyGroup fg WHERE fg.primaryUser.id = :userId OR fg.id IN " +
           "(SELECT m.familyGroup.id FROM FamilyMember m WHERE m.user.id = :userId)")
    List<FamilyGroup> findAllByUserId(@Param("userId") Long userId);
}
