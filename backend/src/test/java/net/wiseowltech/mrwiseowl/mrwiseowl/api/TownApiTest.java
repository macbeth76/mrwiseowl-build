package net.wiseowltech.mrwiseowl.mrwiseowl.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TownApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void shouldGetTownById() {
        given()
            .when()
                .get("/api/towns/1")
            .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Asheville"))
                .body("state", equalTo("North Carolina"));
    }

    @Test
    void shouldReturn404ForInvalidTownId() {
        given()
            .when()
                .get("/api/towns/99999")
            .then()
                .statusCode(404);
    }

    @Test
    void shouldSearchTowns() {
        given()
            .queryParam("q", "Asheville")
            .when()
                .get("/api/towns/search")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }
}
