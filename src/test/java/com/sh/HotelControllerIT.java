package com.sh;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class HotelControllerIT
{
    private final static String path = "/hotel/occupy";

    @Test
    @DisplayName("Failure - Bad request - Missing body")
    public void failure_badRequest_noBody() {
        RestAssured.with()
                   .contentType(MediaType.APPLICATION_JSON)
                   .when()
                   .post(path)
                   .then()
                   .statusCode(400)
                   .extract()
                   .asString();
    }
}
