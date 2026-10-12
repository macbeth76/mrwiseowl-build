package net.wiseowltech.mrwiseowl.mrwiseowl.service;

import net.wiseowltech.mrwiseowl.mrwiseowl.dto.PoiDto;
import net.wiseowltech.mrwiseowl.mrwiseowl.repository.PoiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PoiService {

    private final PoiRepository poiRepository;

    @Cacheable(value = "pois", key = "#townId")
    public List<PoiDto> getPoisByTown(Long townId) {
        log.debug("Getting POIs for town: {}", townId);
        return poiRepository.findByTownId(townId).stream()
                .map(PoiDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PoiDto> getPoisByTownAndCategory(Long townId, String category) {
        log.debug("Getting POIs for town: {} and category: {}", townId, category);
        return poiRepository.findByTownIdAndCategory(townId, category).stream()
                .map(PoiDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PoiDto> getPoisByTownAndCategories(Long townId, List<String> categories) {
        log.debug("Getting POIs for town: {} and categories: {}", townId, categories);
        return poiRepository.findByTownIdAndCategories(townId, categories).stream()
                .map(PoiDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "poi", key = "#id")
    public PoiDto getPoiById(Long id) {
        log.debug("Getting POI by id: {}", id);
        return poiRepository.findById(id)
                .map(PoiDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("POI not found with id: " + id));
    }

    public List<PoiDto> findNearbyPois(double lat, double lng, double radiusKm, String category) {
        log.debug("Finding POIs near lat: {}, lng: {}, radius: {} km, category: {}", 
                  lat, lng, radiusKm, category);
        double radiusMeters = radiusKm * 1000;
        return poiRepository.findNearby(lat, lng, radiusMeters, category).stream()
                .map(PoiDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PoiDto> findByDietaryOption(String dietary) {
        log.debug("Finding POIs with dietary option: {}", dietary);
        String jsonArray = "[\"" + dietary + "\"]";
        return poiRepository.findByDietaryOption(jsonArray).stream()
                .map(PoiDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PoiDto> getPoisByCategory(String category) {
        log.debug("Getting POIs by category: {}", category);
        return poiRepository.findByCategory(category).stream()
                .map(PoiDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PoiDto> getAllPois() {
        log.debug("Getting all POIs");
        return poiRepository.findAll().stream()
                .map(PoiDto::fromEntity)
                .collect(Collectors.toList());
    }
}
