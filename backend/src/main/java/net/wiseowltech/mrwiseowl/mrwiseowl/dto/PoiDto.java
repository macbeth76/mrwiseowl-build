package net.wiseowltech.mrwiseowl.mrwiseowl.dto;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.Poi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoiDto {
    private Long id;
    private Long townId;
    private String name;
    private String category;
    private Double latitude;
    private Double longitude;
    private String address;
    private String phone;
    private String website;
    private String description;
    private Integer priceRange;
    private BigDecimal rating;
    private List<String> photos;
    private List<String> amenities;
    private Map<String, Object> hours;
    private String cuisineType;
    private List<String> dietaryOptions;
    private List<String> tags;

    public static PoiDto fromEntity(Poi poi) {
        return PoiDto.builder()
                .id(poi.getId())
                .townId(poi.getTownId())
                .name(poi.getName())
                .category(poi.getCategory())
                .latitude(poi.getLatitude())
                .longitude(poi.getLongitude())
                .address(poi.getAddress())
                .phone(poi.getPhone())
                .website(poi.getWebsite())
                .description(poi.getDescription())
                .priceRange(poi.getPriceRange())
                .rating(poi.getRating())
                .photos(poi.getPhotos())
                .amenities(poi.getAmenities())
                .hours(poi.getHours())
                .cuisineType(poi.getCuisineType())
                .dietaryOptions(poi.getDietaryOptions())
                .tags(poi.getTags())
                .build();
    }
}
