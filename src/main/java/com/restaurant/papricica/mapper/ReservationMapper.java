package com.restaurant.papricica.mapper;

import com.restaurant.papricica.dtos.ReservationDto;
import com.restaurant.papricica.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(target = "tableId", source = "table.id")
    ReservationDto reservationToReservationDto(Reservation reservation);

    Reservation reservationDtoToReservation(ReservationDto reservationDto);

    List<ReservationDto> reservationsListToReservationDtosList(List<Reservation> reservations);
}
