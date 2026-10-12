package net.wiseowltech.mrwiseowl.mrwiseowl.repository;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

    @Query("SELECT t FROM Town t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Town> searchByName(@Param("query") String query);

    @Query("SELECT t FROM Town t WHERE t.state = :state")
    List<Town> findByState(@Param("state") String state);

    @Query(value = "SELECT * FROM towns t " +
            "WHERE ST_DWithin(t.location::geography, " +
            "ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography, :radiusMeters) " +
            "ORDER BY ST_Distance(t.location::geography, " +
            "ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography)",
            nativeQuery = true)
    List<Town> findNearby(@Param("lat") double lat,
                          @Param("lng") double lng,
                          @Param("radiusMeters") double radiusMeters);
}
