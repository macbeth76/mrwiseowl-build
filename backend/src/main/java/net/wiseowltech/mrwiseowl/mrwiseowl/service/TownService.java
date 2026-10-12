package net.wiseowltech.mrwiseowl.mrwiseowl.service;

import net.wiseowltech.mrwiseowl.mrwiseowl.dto.TownDto;
import net.wiseowltech.mrwiseowl.mrwiseowl.exception.ResourceNotFoundException;
import net.wiseowltech.mrwiseowl.mrwiseowl.model.Town;
import net.wiseowltech.mrwiseowl.mrwiseowl.repository.TownRepository;
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
public class TownService {

    private final TownRepository townRepository;

    @Cacheable("towns")
    public List<TownDto> searchTowns(String query) {
        log.debug("Searching towns with query: {}", query);
        
        if (query == null || query.trim().isEmpty()) {
            return townRepository.findAll().stream()
                    .map(TownDto::fromEntity)
                    .collect(Collectors.toList());
        }
        
        return townRepository.searchByName(query).stream()
                .map(TownDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "town", key = "#id")
    public TownDto getTownById(Long id) {
        log.debug("Getting town by id: {}", id);
        return townRepository.findById(id)
                .map(TownDto::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Town not found with id: " + id));
    }

    public List<TownDto> getTownsByState(String state) {
        log.debug("Getting towns by state: {}", state);
        return townRepository.findByState(state).stream()
                .map(TownDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TownDto> findNearbyTowns(double lat, double lng, double radiusKm) {
        log.debug("Finding towns near lat: {}, lng: {}, radius: {} km", lat, lng, radiusKm);
        double radiusMeters = radiusKm * 1000;
        return townRepository.findNearby(lat, lng, radiusMeters).stream()
                .map(TownDto::fromEntity)
                .collect(Collectors.toList());
    }
}
