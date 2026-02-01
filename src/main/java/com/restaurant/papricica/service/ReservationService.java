package com.restaurant.papricica.service;

import com.restaurant.papricica.dtos.ReservationDto;
import com.restaurant.papricica.mapper.ReservationMapper;
import com.restaurant.papricica.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    public List<ReservationDto> getAllReservations() {
        return reservationMapper.reservationsListToReservationDtosList(reservationRepository.findAll());
    }
}
