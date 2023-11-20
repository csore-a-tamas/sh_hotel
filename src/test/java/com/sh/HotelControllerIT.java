package com.sh;

import com.sh.dtos.HotelOccupyRequest;
import com.sh.enums.RoomCategory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
                   .statusCode(400);
    }

    private static HotelOccupyRequest getRequest(Integer premiumRoomCount, Integer economyRoomCount){
        return new HotelOccupyRequest()
                .setRoomCounts(Map.of(
                        RoomCategory.PREMIUM, premiumRoomCount,
                        RoomCategory.ECONOMY, economyRoomCount
                ))
                .setPaymentOffers(List.of(
                        23, 45, 155, 374, 22, 99, 100, 101, 115, 209)
                );
    }

    private static HotelOccupyRequest getGoodRequest(){
        return getRequest(1,1);
    }

    private static Stream<Arguments> success_propertyStatesValidations_variations() {
        return Stream.of(
                Arguments.of("Full request - Nothing is missing",
                             getGoodRequest()
                ),

                Arguments.of("Room counts is empty map",
                             getGoodRequest().setRoomCounts(Map.of())
                ),

                Arguments.of("Only one room category - Multiple rooms",
                             getGoodRequest().setRoomCounts(Map.of(
                                     RoomCategory.PREMIUM, 10
                             ))
                ),
                Arguments.of("Only one room category - One room",
                             getGoodRequest().setRoomCounts(Map.of(
                                     RoomCategory.PREMIUM, 10
                             ))
                ),
                Arguments.of("Only one room category - Zero room",
                             getGoodRequest().setRoomCounts(Map.of(
                                     RoomCategory.PREMIUM, 0
                             ))
                ),

                Arguments.of("Payment offers is empty",
                             getGoodRequest().setPaymentOffers(List.of())
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("success_propertyStatesValidations_variations")
    @DisplayName("Success - Property States validations")
    public void success_propertyStatesValidations(String name, HotelOccupyRequest request) {
        RestAssured.with()
                   .contentType(MediaType.APPLICATION_JSON)
                   .body(request)
                   .when()
                   .post(path)
                   .then()
                   .statusCode(200);
    }

    private static Stream<Arguments> failure_badRequest_variations() {
        return Stream.of(
                Arguments.of("Room counts is null",
                             getGoodRequest().setRoomCounts(null)
                ),

                Arguments.of("Only one room category - Negative room",
                             getGoodRequest().setRoomCounts(Map.of(
                                     RoomCategory.PREMIUM, -1
                             ))
                ),
                Arguments.of("Multiple room categories - Negative rooms",
                             getGoodRequest().setRoomCounts(Map.of(
                                     RoomCategory.PREMIUM, -1,
                                     RoomCategory.ECONOMY, -1
                             ))
                ),

                Arguments.of("PaymentOffers is null",
                             getGoodRequest().setPaymentOffers(null)
                ),
                Arguments.of("PaymentOffers is a negative number",
                             getGoodRequest().setPaymentOffers(List.of(-1))
                ),
                Arguments.of("PaymentOffers contains negative number",
                             getGoodRequest().setPaymentOffers(List.of(100, -1, 23))
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("failure_badRequest_variations")
    @DisplayName("Failure - Bad Requests")
    public void failure_badRequests(String name, HotelOccupyRequest request) {
        RestAssured.with()
                   .contentType(MediaType.APPLICATION_JSON)
                   .body(request)
                   .when()
                   .post(path)
                   .then()
                   .statusCode(400);
    }
}
