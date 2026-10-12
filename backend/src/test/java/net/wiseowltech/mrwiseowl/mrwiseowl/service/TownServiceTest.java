package net.wiseowltech.mrwiseowl.mrwiseowl.service;

import net.wiseowltech.mrwiseowl.mrwiseowl.dto.TownDto;
import net.wiseowltech.mrwiseowl.mrwiseowl.exception.ResourceNotFoundException;
import net.wiseowltech.mrwiseowl.mrwiseowl.model.Town;
import net.wiseowltech.mrwiseowl.mrwiseowl.repository.TownRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TownServiceTest {

    @Mock
    private TownRepository townRepository;

    @InjectMocks
    private TownService townService;

    private Town testTown;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory();
        Point location = geometryFactory.createPoint(new Coordinate(-82.5515, 35.5951));
        
        testTown = new Town();
        testTown.setId(1L);
        testTown.setName("Asheville");
        testTown.setState("North Carolina");
        testTown.setCountry("USA");
        testTown.setLocation(location);
        testTown.setPopulation(94589);
    }

    @Test
    void shouldGetTownById() {
        when(townRepository.findById(1L)).thenReturn(Optional.of(testTown));

        TownDto result = townService.getTownById(1L);

        assertNotNull(result);
        assertEquals("Asheville", result.getName());
        assertEquals("North Carolina", result.getState());
        verify(townRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTownNotFound() {
        when(townRepository.findById(99999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            townService.getTownById(99999L);
        });

        verify(townRepository, times(1)).findById(99999L);
    }
}
