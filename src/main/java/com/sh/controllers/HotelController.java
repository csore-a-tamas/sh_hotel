package com.sh.controllers;

import com.sh.dtos.HotelOccupyRequest;
import com.sh.dtos.RoomGroup;
import com.sh.services.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/hotel")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @POST
    @Path("/occupy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoomGroup> occupy(@Valid @NotNull HotelOccupyRequest request) {
        return hotelService.occupy(request);
    }
}
