package com.shotaroi.integrationhub.integration;

import com.shotaroi.integrationhub.config.TestMessagingConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestMessagingConfig.class)
class PolicyIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    @Test
    void createAndGetPolicy_EndToEnd() {
        Map<String, Object> policyRequest = Map.of(
                "policyNumber", "POL-INT-001",
                "customerId", "CUST-INT-001",
                "policyType", "LIFE",
                "premiumAmount", 250.50,
                "startDate", LocalDate.now().toString(),
                "status", "PENDING"
        );

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(policyRequest)
                .when()
                .post("/api/integration/policies")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("policyNumber", equalTo("POL-INT-001"))
                .body("policyType", equalTo("LIFE"))
                .body("status", equalTo("PENDING"))
                .extract();

        Long policyId = Long.valueOf(createResponse.path("id").toString());

        given()
                .when()
                .get("/api/integration/policies/" + policyId)
                .then()
                .statusCode(200)
                .body("id", equalTo(policyId.intValue()))
                .body("policyNumber", equalTo("POL-INT-001"))
                .body("customerId", equalTo("CUST-INT-001"));
    }

    @Test
    void updatePolicyStatus_EndToEnd() {
        Map<String, Object> policyRequest = Map.of(
                "policyNumber", "POL-INT-002",
                "customerId", "CUST-INT-002",
                "policyType", "HOME",
                "premiumAmount", 500.00,
                "startDate", LocalDate.now().toString(),
                "status", "PENDING"
        );

        Long policyId = Long.valueOf(given()
                .contentType(ContentType.JSON)
                .body(policyRequest)
                .when()
                .post("/api/integration/policies")
                .then()
                .statusCode(201)
                .extract()
                .path("id").toString());

        Map<String, String> statusUpdate = Map.of("status", "ACTIVE");

        given()
                .contentType(ContentType.JSON)
                .body(statusUpdate)
                .when()
                .patch("/api/integration/policies/" + policyId + "/status")
                .then()
                .statusCode(200)
                .body("status", equalTo("ACTIVE"));
    }

    @Test
    void getPolicy_NotFound_Returns404() {
        given()
                .when()
                .get("/api/integration/policies/99999")
                .then()
                .statusCode(404);
    }

    @Test
    void createPolicy_ValidationError_Returns400() {
        Map<String, Object> invalidRequest = Map.of(
                "policyNumber", "",
                "customerId", "CUST-001",
                "policyType", "LIFE",
                "premiumAmount", -100,
                "startDate", LocalDate.now().toString()
        );

        given()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when()
                .post("/api/integration/policies")
                .then()
                .statusCode(400);
    }
}
