package com.restaurant.papricica.repository;

import com.restaurant.papricica.entity.Reservation;
import com.restaurant.papricica.util.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findAllByUserEmail(@Param("email")String email);

    @Query("""
    select distinct r.table.id from Reservation r
    where r.status in :blocking
    and r.startDate < :end
    and r.endDate > :start
    """)
    Set<Long> findBlockedTableIds(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end,
            @Param("blocking") Set<ReservationStatus> blocking
    );

    @Query("""
    select distinct r from Reservation r
        where r.table.id = :tableId
        and r.startDate = :startDate
    """)
    Reservation findByTableIdAndStartDate(Long tableId, OffsetDateTime startDate);

}
