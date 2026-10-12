package net.wiseowltech.mrwiseowl.mrwiseowl.controller;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.SavedItinerary;
import net.wiseowltech.mrwiseowl.mrwiseowl.service.AuthService;
import net.wiseowltech.mrwiseowl.mrwiseowl.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<SavedItinerary> save(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> request) {
        
        Long userId = getUserIdFromAuth(authHeader);
        
        String name = (String) request.get("name");
        Long townId = ((Number) request.get("townId")).longValue();
        @SuppressWarnings("unchecked")
        List<Long> poiIds = (List<Long>) request.get("poiIds");
        
        SavedItinerary saved = itineraryService.save(userId, name, townId, poiIds);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<SavedItinerary>> list(
            @RequestHeader("Authorization") String authHeader) {
        
        Long userId = getUserIdFromAuth(authHeader);
        List<SavedItinerary> itineraries = itineraryService.getUserItineraries(userId);
        return ResponseEntity.ok(itineraries);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        
        Long userId = getUserIdFromAuth(authHeader);
        itineraryService.delete(id, userId);
        return ResponseEntity.ok().build();
    }

    private Long getUserIdFromAuth(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return authService.getUserIdFromToken(token);
    }
}
