package net.wiseowltech.mrwiseowl.mrwiseowl.service;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.SavedItinerary;
import net.wiseowltech.mrwiseowl.mrwiseowl.repository.SavedItineraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItineraryService {

    private final SavedItineraryRepository itineraryRepository;

    public SavedItinerary save(Long userId, String name, Long townId, List<Long> poiIds) {
        SavedItinerary itinerary = new SavedItinerary();
        itinerary.setUserId(userId);
        itinerary.setName(name);
        itinerary.setTownId(townId);
        itinerary.setPoiIds(poiIds);
        
        return itineraryRepository.save(itinerary);
    }

    public List<SavedItinerary> getUserItineraries(Long userId) {
        return itineraryRepository.findByUserIdOrderByUpdatedAtDesc(userId);
    }

    public void delete(Long id, Long userId) {
        SavedItinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));
        
        if (!itinerary.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        
        itineraryRepository.delete(itinerary);
    }
}
