package net.wiseowltech.mrwiseowl.mrwiseowl.repository;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.Poi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoiRepository extends JpaRepository<Poi, Long> {

    List<Poi> findByTownId(Long townId);

    List<Poi> findByCategory(String category);

    List<Poi> findByTownIdAndCategory(Long townId, String category);

    @Query("SELECT p FROM Poi p WHERE p.townId = :townId AND p.category IN :categories")
    List<Poi> findByTownIdAndCategories(@Param("townId") Long townId, 
                                        @Param("categories") List<String> categories);

    @Query(value = "SELECT * FROM pois p " +
            "WHERE ST_DWithin(p.location::geography, " +
            "ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography, :radiusMeters) " +
            "AND (:category IS NULL OR p.category = :category) " +
            "ORDER BY ST_Distance(p.location::geography, " +
            "ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography)",
            nativeQuery = true)
    List<Poi> findNearby(@Param("lat") double lat,
                        @Param("lng") double lng,
                        @Param("radiusMeters") double radiusMeters,
                        @Param("category") String category);

    @Query(value = "SELECT * FROM pois p " +
            "WHERE p.dietary_options @> CAST(:dietary AS jsonb)",
            nativeQuery = true)
    List<Poi> findByDietaryOption(@Param("dietary") String dietary);
}
