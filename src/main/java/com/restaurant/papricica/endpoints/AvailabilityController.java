package com.restaurant.papricica.endpoints;

import com.restaurant.papricica.dtos.AvailabilityResponse;
import com.restaurant.papricica.service.AvailabilityService;
import jakarta.annotation.security.PermitAll;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("api/v1/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping
    @PermitAll
    public AvailabilityResponse getAvailability(@RequestParam("start") OffsetDateTime start) {

        return availabilityService.getAvailability(start);
    }
}
