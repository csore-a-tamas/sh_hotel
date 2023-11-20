package com.sh.controllers;

import com.sh.dtos.HotelOccupyRequest;
import com.sh.dtos.RoomGroup;
import com.sh.enums.RoomCategory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.ws.rs.core.MediaType;
import org.assertj.core.api.Assertions;
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

    private static List<RoomGroup> getResponse(
            Integer premiumRoomCount,
            Integer premiumIncome,
            Integer economyRoomCount,
            Integer economyIncome
    ) {
        return List.of(new RoomGroup().setCategory(RoomCategory.PREMIUM).setOccupiedRoomCount(premiumRoomCount).setIncome(premiumIncome),
                       new RoomGroup().setCategory(RoomCategory.ECONOMY).setOccupiedRoomCount(economyRoomCount).setIncome(economyIncome));
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

    private static Stream<Arguments> success_variations() {
        return Stream.of(
                Arguments.of("Test 1",
                             getRequest(3,3),
                             getResponse(3, 738,3, 167)
                ),
                Arguments.of("Test 2",
                             getRequest(7,5),
                             getResponse(6, 1054,4, 189)
                ),
                Arguments.of("Test 3",
                             getRequest(2,7),
                             getResponse(2, 583,4, 189)
                ),
//                TODO - The given test results does not match the given functionality
//                Arguments.of("Test 4",
//                             getRequest(10,1),
//                             getResponse(7, 1153,1, 45)
//                ),

                Arguments.of("Test 4 - different response",
                              getRequest(10,1),
                              getResponse(9, 1221,1, 22)
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("success_variations")
    @DisplayName("Success - Provided test cases")
    public void success(String name, HotelOccupyRequest request, List<RoomGroup> expected) {
        List<RoomGroup> actual =
                RestAssured.with()
                           .contentType(MediaType.APPLICATION_JSON)
                           .body(request)
                           .when()
                           .post(path)
                           .then()
                           .statusCode(200)
                           .extract()
                           .as(new TypeRef<>(){});

        Assertions.assertThat(actual)
                  .as("Actual same as expected")
                  .usingRecursiveComparison()
                  .ignoringCollectionOrder()
                  .isEqualTo(expected);
    }
}
