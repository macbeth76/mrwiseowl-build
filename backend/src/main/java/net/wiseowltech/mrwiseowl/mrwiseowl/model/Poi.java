package net.wiseowltech.mrwiseowl.mrwiseowl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "pois")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "town_id")
    private Long townId;

    @Column(nullable = false)
    private String name;

    private String category; // hotel, activity_outdoor, activity_indoor, campground_rv, campground_tent, historic, brewery, restaurant, bar

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point location;

    private String address;
    private String phone;
    private String website;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_range")
    private Integer priceRange; // 1-4 ($-$$$$)

    private BigDecimal rating;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> photos;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> amenities;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> hours;

    @Column(name = "cuisine_type")
    private String cuisineType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dietary_options")
    private List<String> dietaryOptions;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tags;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "external_source")
    private String externalSource;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Double getLatitude() {
        return location != null ? location.getY() : null;
    }

    public Double getLongitude() {
        return location != null ? location.getX() : null;
    }
}
