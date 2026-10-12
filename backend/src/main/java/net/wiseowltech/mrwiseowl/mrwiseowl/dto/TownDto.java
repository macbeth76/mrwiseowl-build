package net.wiseowltech.mrwiseowl.mrwiseowl.dto;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.Town;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TownDto {
    private Long id;
    private String name;
    private String country;
    private String state;
    private Double latitude;
    private Double longitude;
    private Integer population;

    public static TownDto fromEntity(Town town) {
        return TownDto.builder()
                .id(town.getId())
                .name(town.getName())
                .country(town.getCountry())
                .state(town.getState())
                .latitude(town.getLatitude())
                .longitude(town.getLongitude())
                .population(town.getPopulation())
                .build();
    }
}
