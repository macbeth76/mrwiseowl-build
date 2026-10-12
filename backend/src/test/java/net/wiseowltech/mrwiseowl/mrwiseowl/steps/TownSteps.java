package net.wiseowltech.mrwiseowl.mrwiseowl.steps;

import net.wiseowltech.mrwiseowl.mrwiseowl.dto.TownDto;
import net.wiseowltech.mrwiseowl.mrwiseowl.service.TownService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TownSteps {

    @Autowired
    private TownService townService;

    private List<TownDto> towns;
    private TownDto town;

    @When("I search for towns with no query")
    public void searchTownsNoQuery() {
        towns = townService.searchTowns("");
    }

    @When("I search for towns with query {string}")
    public void searchTownsWithQuery(String query) {
        towns = townService.searchTowns(query);
    }

    @Then("I should see at least {int} town")
    public void shouldSeeAtLeastTowns(int count) {
        assertNotNull(towns);
        assertTrue(towns.size() >= count);
    }

    @Then("each town should have a name and state")
    public void eachTownShouldHaveNameAndState() {
        for (TownDto t : towns) {
            assertNotNull(t.getName());
            assertNotNull(t.getState());
        }
    }

    @Then("the first town name should contain {string}")
    public void firstTownNameShouldContain(String name) {
        assertTrue(towns.get(0).getName().contains(name));
    }

    @Given("a town exists with ID {long}")
    public void townExistsWithId(Long id) {
        // Assume town exists in test data
    }

    @When("I get town by ID {long}")
    public void getTownById(Long id) {
        town = townService.getTownById(id);
    }

    @Then("I should receive the town details")
    public void shouldReceiveTownDetails() {
        assertNotNull(town);
        assertNotNull(town.getName());
    }

    @Then("the town should have coordinates")
    public void townShouldHaveCoordinates() {
        assertNotNull(town.getLatitude());
        assertNotNull(town.getLongitude());
    }
}
