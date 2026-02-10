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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
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
    private OffsetDateTime startDate;

    @NotNull
    @Getter
    @Setter
    private OffsetDateTime endDate;

    @NotNull
    @Getter
    @Setter
    private OffsetDateTime createdAt;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    @Setter
    private User user;

    public Reservation() { /* Default constructor */ }
}
