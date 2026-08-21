package com.restaurant.papricica.service;

import com.restaurant.papricica.dtos.CreateReservationDto;
import com.restaurant.papricica.dtos.ReservationDeleteRequest;
import com.restaurant.papricica.dtos.ReservationDetailsDto;
import com.restaurant.papricica.dtos.ReservationDto;
import com.restaurant.papricica.entity.Reservation;
import com.restaurant.papricica.entity.Table;
import com.restaurant.papricica.entity.User;
import com.restaurant.papricica.exceptions.ReservationAlreadyExistsException;
import com.restaurant.papricica.exceptions.TableNotFound;
import com.restaurant.papricica.mapper.ReservationMapper;
import com.restaurant.papricica.repository.ReservationRepository;
import com.restaurant.papricica.repository.TableRepository;
import com.restaurant.papricica.repository.UserRepository;
import com.restaurant.papricica.util.ReservationStatus;
import com.restaurant.papricica.util.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.List;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final UserRepository userRepository;
    private final TableRepository tableRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper, UserRepository userRepository, TableRepository tableRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.tableRepository = tableRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationDetailsDto> getAllForUser(String email) {

        User user = userRepository.findUserByEmail(email);

        if(user == null || user.getStatus() == UserStatus.LOCKED){
            throw new RuntimeException("User not found");
        }

        return reservationRepository.findAllByUserEmail(email).stream()
                .map(reservationMapper::reservationToReservationDetailsDto)
                .toList();

    }


    @Transactional
    public ReservationDetailsDto createReservation(CreateReservationDto createReservationDto, String email) {
        LOGGER.info("Reservation to be created {} for user {}:", createReservationDto, email);

        Reservation stored = reservationRepository.findByTableIdAndStartDateAndStatus(createReservationDto.tableId(), createReservationDto.startDate(), ReservationStatus.RESERVED);

        if (stored != null) {
            throw new RuntimeException("We are very sorry but, it seems that the table you are trying to reserve is already reserved for the selected time slot :(. Please choose a different time or table.");
        }

        Reservation reservation = reservationMapper.createReservationDtoToReservation(createReservationDto);
        LOGGER.info("Mapped Reservation from createReservationDti {}", reservation);

        Table table = tableRepository.findById(createReservationDto.tableId()).orElseThrow(TableNotFound::new);
        LOGGER.info("The found table for the reservation: {}", table);

        User user = userRepository.findUserByEmail(email);
        if(user == null || user.getStatus() == UserStatus.LOCKED){
            throw new RuntimeException("User not found");
        }

        if(table.getPartySize() < createReservationDto.partySize()){
            throw new RuntimeException("Party size is too large for this table");
        }

        reservation.setUser(user);
        reservation.setTable(table);
        reservation.setStatus(ReservationStatus.RESERVED);
        reservation.setEndDate(reservation.getStartDate().plusHours(2));

        try {
            reservationRepository.save(reservation);
        } catch(DataIntegrityViolationException e){
            throw new ReservationAlreadyExistsException();
        }

        table.getReservations().add(reservation);
        ReservationDetailsDto ab = reservationMapper.reservationToReservationDetailsDto(reservation);
        LOGGER.info("The ReservationDetailsDto to be returned: {}", ab);
        return ab;
    }

    @Transactional
    public void delete(ReservationDeleteRequest reservationDeleteRequest, String email){

        Reservation stored = reservationRepository.findByTableIdAndStartDate(reservationDeleteRequest.tableId(), reservationDeleteRequest.startDate());

        if(stored == null){
            throw new RuntimeException("Reservation doesn't exist"); // CREATE SEPARATE EXCEPTION CALLED "RESERVATION_NON_EXISTENT"
        }

        if(!stored.getUser().getEmail().equals(email) || stored.getUser().getStatus() == UserStatus.LOCKED) {
            throw new RuntimeException("User is not allowed to cancel this reservation");
        }

        if(stored.getStartDate().isAfter(OffsetDateTime.now().plusHours(3))){
            throw new RuntimeException("You can no longer cancel this reservation");
        }

        stored.setStatus(ReservationStatus.CANCELLED);
    }

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 2)
    @Transactional
    public void awardPointsToUserForCompletedReservation(){

        OffsetDateTime now = OffsetDateTime.now();

        List<Reservation> reservations = reservationRepository
                .findAllByStatusAndEndDateBefore(ReservationStatus.RESERVED, now);

        for(Reservation r : reservations) {
            calculatePoints(r);
        }
    }

    private void calculatePoints(Reservation reservation) {

        if(reservation.isPointsAwarded()) {
            return;
        }

        User user = reservation.getUser();
        user.setPoints(user.getPoints() + 100); //THIS CAN EVENTUALLY BE CHANGED TO THE DEIRED AMOUNT OF POINTS

        reservation.setPointsAwarded(true);
        reservation.setStatus(ReservationStatus.COMPLETED);
    }

    @Transactional
    public void cancelReservation (ReservationDeleteRequest dto) {
        Reservation reservation = reservationRepository.
                findByTableIdAndStartDate(dto.tableId(), dto.startDate());

        if (reservation == null) {
            return;
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
    }

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedDelay = 30)
    @Transactional
    public void clearDbFromOldReservations(){
        OffsetDateTime now = OffsetDateTime.now();

        reservationRepository.deleteAllBefore(now);
    }
}