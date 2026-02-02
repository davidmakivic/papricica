package com.restaurant.papricica.entity;

import com.restaurant.papricica.util.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotNull
    @Getter
    @Setter
    private int partySize;

    @NotNull
    @Getter
    @Setter
    private Date startDate;

    @NotNull
    @Getter
    @Setter
    private Date endDate;

    @NotNull
    @Getter
    @Setter
    private Date createdAt;

    @NotNull
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private com.restaurant.papricica.entity.Table table;

    public Reservation() { /* Default constructor */ }


}
