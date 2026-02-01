package com.restaurant.papricica.mapper;

import com.restaurant.papricica.dtos.ReservationDto;
import com.restaurant.papricica.entity.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationDto reservationToReservationDto(Reservation reservation);

    Reservation reservationDtoToReservation(ReservationDto reservationDto);
}
