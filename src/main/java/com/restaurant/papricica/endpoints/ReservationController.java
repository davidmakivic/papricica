package com.restaurant.papricica.endpoints;

import com.restaurant.papricica.dtos.CreateReservationDto;
import com.restaurant.papricica.dtos.ReservationDeleteRequest;
import com.restaurant.papricica.dtos.ReservationDetailsDto;
import com.restaurant.papricica.service.ReservationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Secured("ROLE_USER")
    @PostMapping
    public ResponseEntity<ReservationDetailsDto> createReservation(@Valid @RequestBody CreateReservationDto createReservationDto, Principal principal) {
        LOGGER.info("Creating a new reservation");
        ReservationDetailsDto reservationDetailsDto = reservationService.createReservation(createReservationDto, principal.getName());
        return reservationDetailsDto != null ? ResponseEntity.ok(reservationDetailsDto) : ResponseEntity.badRequest().build();
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody ReservationDeleteRequest reservationDeleteRequest, Principal principal){
       LOGGER.info("Deleting reservation");
       reservationService.delete(reservationDeleteRequest, principal.getName());
       return ResponseEntity.noContent().build();
    }

    @Secured("ROLE_USER")
    @GetMapping
    public ResponseEntity<List<ReservationDetailsDto>> getReservationsForUser(Principal principal) {
        List<ReservationDetailsDto> reservations = reservationService.getAllForUser(principal.getName());
        return ResponseEntity.ok(reservations);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping
    public ResponseEntity<Void> cancelReservation(ReservationDeleteRequest dto){
       reservationService.cancelReservation(dto);
       return ResponseEntity.noContent().build();
    }
}
