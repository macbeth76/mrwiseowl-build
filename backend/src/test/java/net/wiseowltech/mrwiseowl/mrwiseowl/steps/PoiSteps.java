package net.wiseowltech.mrwiseowl.mrwiseowl.steps;

import net.wiseowltech.mrwiseowl.mrwiseowl.dto.PoiDto;
import net.wiseowltech.mrwiseowl.mrwiseowl.service.PoiService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PoiSteps {

    @Autowired
    private PoiService poiService;

    private List<PoiDto> pois;
    private PoiDto poi;

    @When("I get POIs for town ID {long}")
    public void getPoisForTown(Long townId) {
        pois = poiService.getPoisByTown(townId);
    }

    @When("I get POIs for town ID {long} and category {string}")
    public void getPoisForTownAndCategory(Long townId, String category) {
        pois = poiService.getPoisByTownAndCategory(townId, category);
    }

    @Then("I should see at least {int} POI")
    public void shouldSeeAtLeastPois(int count) {
        assertNotNull(pois);
        assertTrue(pois.size() >= count);
    }

    @Then("each POI should have a name and category")
    public void eachPoiShouldHaveNameAndCategory() {
        for (PoiDto p : pois) {
            assertNotNull(p.getName());
            assertNotNull(p.getCategory());
        }
    }

    @Then("all POIs should have category {string}")
    public void allPoisShouldHaveCategory(String category) {
        for (PoiDto p : pois) {
            assertEquals(category, p.getCategory());
        }
    }

    @Given("a POI exists with ID {long}")
    public void poiExistsWithId(Long id) {
        // Assume POI exists in test data
    }

    @When("I get POI by ID {long}")
    public void getPoiById(Long id) {
        poi = poiService.getPoiById(id);
    }

    @Then("I should receive the POI details")
    public void shouldReceivePoiDetails() {
        assertNotNull(poi);
        assertNotNull(poi.getName());
    }

    @Then("the POI should have coordinates")
    public void poiShouldHaveCoordinates() {
        assertNotNull(poi.getLatitude());
        assertNotNull(poi.getLongitude());
    }
}
