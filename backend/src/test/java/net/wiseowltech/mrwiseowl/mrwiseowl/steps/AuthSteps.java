package net.wiseowltech.mrwiseowl.mrwiseowl.steps;

import net.wiseowltech.mrwiseowl.mrwiseowl.model.User;
import net.wiseowltech.mrwiseowl.mrwiseowl.repository.UserRepository;
import net.wiseowltech.mrwiseowl.mrwiseowl.service.AuthService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class AuthSteps {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    private String token;
    private Exception error;

    @When("I register with email {string} and password {string}")
    public void registerUser(String email, String password) {
        try {
            token = authService.register(email, password, "Test User");
        } catch (Exception e) {
            error = e;
        }
    }

    @Given("a user exists with email {string} and password {string}")
    public void userExists(String email, String password) {
        if (!userRepository.findByEmail(email).isPresent()) {
            authService.register(email, password, "Test User");
        }
    }

    @When("I login with email {string} and password {string}")
    public void loginUser(String email, String password) {
        try {
            token = authService.login(email, password);
        } catch (Exception e) {
            error = e;
        }
    }

    @Then("I should receive a JWT token")
    public void shouldReceiveToken() {
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Then("the token should be valid")
    public void tokenShouldBeValid() {
        assertNotNull(token);
        Long userId = authService.getUserIdFromToken(token);
        assertNotNull(userId);
    }

    @Then("I should receive an authentication error")
    public void shouldReceiveAuthError() {
        assertNotNull(error);
    }
}
