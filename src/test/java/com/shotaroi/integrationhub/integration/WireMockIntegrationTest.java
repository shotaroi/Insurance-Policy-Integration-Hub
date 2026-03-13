package com.shotaroi.integrationhub.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

/**
 * Demonstrates WireMock for mocking external dependencies.
 * In production, this would mock external partner APIs that the integration hub calls.
 */
class WireMockIntegrationTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Test
    void wireMock_StubExternalApi_ReturnsMockedResponse() {
        wireMock.stubFor(get("/external/partner/status")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"available\",\"partner\":\"legacy-system\"}")));

        given()
                .baseUri(wireMock.baseUrl())
                .when()
                .get("/external/partner/status")
                .then()
                .statusCode(200)
                .body(containsString("available"))
                .body(containsString("legacy-system"));
    }
}
