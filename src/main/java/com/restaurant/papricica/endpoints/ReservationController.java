package com.restaurant.papricica.endpoints;

import com.restaurant.papricica.dtos.CreateReservationDto;
import com.restaurant.papricica.dtos.ReservationDetailsDto;
import com.restaurant.papricica.dtos.ReservationDto;
import com.restaurant.papricica.service.ReservationService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PermitAll
    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
         LOGGER.info("getAllReservations");
         LOGGER.debug("getAllReservations");
            List<ReservationDto> reservations = reservationService.getAllReservations();
            return reservations != null ? ResponseEntity.ok(reservations) : ResponseEntity.notFound().build();
    }

    @PermitAll
    @PostMapping
    public ResponseEntity<ReservationDetailsDto> createReservation(CreateReservationDto createReservationDto) {
        LOGGER.info("Creating a new reservation");
        ReservationDetailsDto reservationDetailsDto = reservationService.createReservation(createReservationDto);
        return reservationDetailsDto != null ? ResponseEntity.ok(reservationDetailsDto) : ResponseEntity.badRequest().build();
    }
}
