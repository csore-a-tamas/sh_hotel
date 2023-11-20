package com.sh;

import com.sh.dtos.Request;
import com.sh.dtos.ResponseEntity;
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

    @POST
    @Path("/occupy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<ResponseEntity> occupy(@Valid @NotNull Request request) {
        throw new RuntimeException("Not implemented");
    }
}
