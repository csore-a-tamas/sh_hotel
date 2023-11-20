package com.sh.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sh.enums.RoomCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class RoomGroup {
    @NotNull
    private RoomCategory category = null;

    //This does not need to be in the current response but due to responsibility reasons,
    // it makes sense to be here during inner process flow
    @JsonIgnore
    private Integer totalRoomCount = null;

    @NotNull
    @Min(0L)
    private Integer occupiedRoomCount = null;

    @NotNull
    @Min(0L)
    private Integer income = null;

    /**
     * Default constructor is only used for (de)serialization.
     * Use this during inner process flows
     * @param category The category of the group
     * @param totalRoomCount The total available room count
     */
    public RoomGroup(RoomCategory category, Integer totalRoomCount) {
        this.category = category;
        this.totalRoomCount = totalRoomCount;
        this.occupiedRoomCount = 0;
        this.income = 0;
    }

    /**
     * If there are still empty rooms, it occupies it by
     * - increasing {@link RoomGroup#income} <br>
     * - incrementing {@link RoomGroup#income} <br>
     * @param payment The amount to increase {@link RoomGroup#income}
     * @return {@code TRUE} if there was room for a new quest {@code FALSE} otherwise
     */
    public boolean occupyRoom(Integer payment){
        if(occupiedRoomCount < totalRoomCount){
            income += payment;
            occupiedRoomCount++;

            return true;
        } else {
            return false;
        }
    }
}
