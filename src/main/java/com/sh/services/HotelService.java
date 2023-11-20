package com.sh.services;

import com.sh.dtos.HotelOccupyRequest;
import com.sh.dtos.RoomGroup;
import com.sh.enums.RoomCategory;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class HotelService {
    private final Integer minimumPaymentForPremiumRoom;

    public HotelService(
            @ConfigProperty(name = "hotel.minimum-payment-for-premium-room")
            Integer minimumPaymentForPremiumRoom
    ) {
        this.minimumPaymentForPremiumRoom = minimumPaymentForPremiumRoom;
    }

    /**
     * Calculates the best coverage for the hotel
     * @param request The request from the caller
     * @return The RoomGroups filled with the chosen offers
     */
    public List<RoomGroup> occupy(HotelOccupyRequest request) {
        try {

            Map<RoomCategory, RoomGroup> roomGroups = new HashMap<>();

            //Sort and group the offers for easier processing
            Map<RoomCategory, List<Integer>> sortedPaymentOffersByRoomTypes
                    = request.getPaymentOffers()
                             .stream()
                             .sorted(Comparator.reverseOrder())
                             .collect(Collectors.groupingBy(this::getRoomType));

            //Handle premium offers
            Integer premiumRoomCount = request.getRoomCounts().getOrDefault(RoomCategory.PREMIUM, 0);
            sortedPaymentOffersByRoomTypes.getOrDefault(RoomCategory.PREMIUM, new ArrayList<>())
                                          .forEach(paymentOffer ->
                                                           roomGroups.computeIfAbsent(RoomCategory.PREMIUM, key -> new RoomGroup(key, premiumRoomCount))
                                                                     .occupyRoom(paymentOffer)
                                          );
            //Handle economy offers
            List<Integer> economyOffers = sortedPaymentOffersByRoomTypes.get(RoomCategory.ECONOMY);
            if(economyOffers == null) {
                return new ArrayList<>(roomGroups.values());
            }

            RoomGroup premiumRoomGroup = roomGroups.get(RoomCategory.PREMIUM);
            Integer economyRoomCount = request.getRoomCounts().getOrDefault(RoomCategory.ECONOMY, 0);
            int economyOffersMovedToPremium = 0;

            //Upgrade the highest economy offers to premium if needed
            if(premiumRoomGroup != null) {
                boolean tooManyEconomyOffers = economyRoomCount < economyOffers.size();
                int premiumRoomCountToFill = premiumRoomGroup.getTotalRoomCount() - premiumRoomGroup.getOccupiedRoomCount() - economyRoomCount;

                //It there won't be enough economy room but there are empty premium rooms, we do the upgrade
                if(tooManyEconomyOffers && 0 < premiumRoomCountToFill) {
                    economyOffers.stream()
                                 .limit(premiumRoomCountToFill)
                                 .forEach(premiumRoomGroup::occupyRoom);

                    economyOffersMovedToPremium = premiumRoomCountToFill;
                }
            }

            //Handle rest of the economy offers
            economyOffers.stream()
                         .skip(economyOffersMovedToPremium)
                         .forEach(paymentOffer ->
                                          roomGroups.computeIfAbsent(RoomCategory.ECONOMY, key -> new RoomGroup(key, economyRoomCount))
                                                    .occupyRoom(paymentOffer)
                         );

            return new ArrayList<>(roomGroups.values());

        } catch (Exception e) {
            log.error("Unexpected exception with request: '" + request + "'", e);
            throw e;
        }
    }

    /**
     * Determinate the room type by the paymentOffer
     * @param paymentOffer The money the quest want to pay
     * @return The RoomCategory, determined by business logic (currently by a single const value: {@link HotelService#minimumPaymentForPremiumRoom})
     */
    private RoomCategory getRoomType(Integer paymentOffer){
        if(minimumPaymentForPremiumRoom <= paymentOffer){
            return RoomCategory.PREMIUM;
        } else {
            return RoomCategory.ECONOMY;
        }
    }
}
