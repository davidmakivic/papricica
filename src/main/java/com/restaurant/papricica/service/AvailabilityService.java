package com.restaurant.papricica.service;

import com.restaurant.papricica.dtos.AvailabilityResponse;
import com.restaurant.papricica.dtos.TableAvailabilityDto;
import com.restaurant.papricica.repository.ReservationRepository;
import com.restaurant.papricica.repository.TableRepository;
import com.restaurant.papricica.util.ReservationStatus;
import com.restaurant.papricica.util.TableStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Service
public class AvailabilityService {
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public AvailabilityService(TableRepository tableRepository, ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    public AvailabilityResponse getAvailability(OffsetDateTime start) {
        OffsetDateTime end = start.plusHours(2);

        Set<ReservationStatus> blocking = Set.of(ReservationStatus.RESERVED);

        Set<Long> blockedTableIds = reservationRepository.findBlockedTableIds(start, end, blocking);

        List<TableAvailabilityDto> tables = tableRepository.findAll().stream()
                .map(t -> new TableAvailabilityDto(
                        t.getId(),
                        blockedTableIds.contains(t.getId()) ? TableStatus.RESERVED : TableStatus.AVAILABLE
                ))
                .toList();

        return new AvailabilityResponse(start, end, tables);
    }
}
