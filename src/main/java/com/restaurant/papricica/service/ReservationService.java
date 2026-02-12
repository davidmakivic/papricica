package com.restaurant.papricica.service;

import com.restaurant.papricica.dtos.CreateReservationDto;
import com.restaurant.papricica.dtos.ReservationDetailsDto;
import com.restaurant.papricica.dtos.ReservationDto;
import com.restaurant.papricica.entity.Reservation;
import com.restaurant.papricica.entity.User;
import com.restaurant.papricica.mapper.ReservationMapper;
import com.restaurant.papricica.repository.ReservationRepository;
import com.restaurant.papricica.repository.UserRepository;
import com.restaurant.papricica.util.ReservationStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;
    private UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    public List<ReservationDto> getAllReservations() {
        return reservationMapper.reservationsListToReservationDtosList(reservationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<ReservationDto> getAllForUser(String email) {

        User user = userRepository.findUserByEmail(email);

        if(user == null){
            throw new RuntimeException("User not found");
        }

        return reservationRepository.findAllByUserEmail(email).stream()
                .map(reservationMapper::reservationToReservationDto)
                .toList();

    }

    public void deleteForUser(long reservationId, String email) {
        Reservation r = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (r.getUser() == null || r.getUser().getEmail() == null || !r.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Not allowed");
        }
    }

    public ReservationDetailsDto createReservation(CreateReservationDto createReservationDto) {
        Reservation stored = reservationRepository.findByTableIdAndStartDate(createReservationDto.tableId(), createReservationDto.startDate());

        if (stored != null) {
            throw new RuntimeException("We are very sorry but, it seems that the table you are trying to reserve is already reserved for the selected time slot :(. Please choose a different time or table.");
        }

        Reservation reservation = reservationMapper.createReservationDtoToReservation(createReservationDto);

        reservation.setStatus(ReservationStatus.RESERVED);

        Reservation saved = reservationRepository.save(reservation);

        return reservationMapper.reservationToReservationDetailsDto(saved);
    }
}

