package com.sh.dtos;

import com.sh.enums.RoomCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class HotelOccupyRequest {

    @NotNull
    private Map<@NotNull RoomCategory, @NotNull @Min(0L) Integer> roomCounts = null;

    @NotNull
    private List<@NotNull @Min(0L) Integer> paymentOffers = null;
}
