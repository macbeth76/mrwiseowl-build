package net.wiseowltech.mrwiseowl.mrwiseowl.controller;

import net.wiseowltech.mrwiseowl.mrwiseowl.dto.TownDto;
import net.wiseowltech.mrwiseowl.mrwiseowl.service.TownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/towns")
@RequiredArgsConstructor
public class TownController {

    private final TownService townService;

    @GetMapping("/search")
    public ResponseEntity<List<TownDto>> searchTowns(
            @RequestParam(required = false) String q) {
        List<TownDto> towns = townService.searchTowns(q);
        return ResponseEntity.ok(towns);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TownDto> getTownById(@PathVariable Long id) {
        TownDto town = townService.getTownById(id);
        return ResponseEntity.ok(town);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<TownDto>> getTownsByState(@PathVariable String state) {
        List<TownDto> towns = townService.getTownsByState(state);
        return ResponseEntity.ok(towns);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<TownDto>> findNearbyTowns(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "50") double radius) {
        List<TownDto> towns = townService.findNearbyTowns(lat, lng, radius);
        return ResponseEntity.ok(towns);
    }
}
