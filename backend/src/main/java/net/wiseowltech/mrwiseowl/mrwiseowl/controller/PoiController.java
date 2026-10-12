package net.wiseowltech.mrwiseowl.mrwiseowl.controller;

import net.wiseowltech.mrwiseowl.mrwiseowl.dto.PoiDto;
import net.wiseowltech.mrwiseowl.mrwiseowl.service.PoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pois")
@RequiredArgsConstructor
public class PoiController {

    private final PoiService poiService;

    @GetMapping
    public ResponseEntity<List<PoiDto>> getPois(
            @RequestParam(required = false) Long townId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> categories) {
        
        if (townId != null) {
            if (categories != null && !categories.isEmpty()) {
                return ResponseEntity.ok(poiService.getPoisByTownAndCategories(townId, categories));
            } else if (category != null) {
                return ResponseEntity.ok(poiService.getPoisByTownAndCategory(townId, category));
            } else {
                return ResponseEntity.ok(poiService.getPoisByTown(townId));
            }
        } else if (category != null) {
            return ResponseEntity.ok(poiService.getPoisByCategory(category));
        } else {
            return ResponseEntity.ok(poiService.getAllPois());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoiDto> getPoiById(@PathVariable Long id) {
        return ResponseEntity.ok(poiService.getPoiById(id));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<PoiDto>> findNearbyPois(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5") double radius,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(poiService.findNearbyPois(lat, lng, radius, category));
    }

    @GetMapping("/dietary")
    public ResponseEntity<List<PoiDto>> findByDietary(@RequestParam String option) {
        return ResponseEntity.ok(poiService.findByDietaryOption(option));
    }
}
