package net.wiseowltech.mrwiseowl.mrwiseowl.repository;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.SavedItinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedItineraryRepository extends JpaRepository<SavedItinerary, Long> {
    List<SavedItinerary> findByUserIdOrderByUpdatedAtDesc(Long userId);
}
