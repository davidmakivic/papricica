package com.restaurant.papricica.entity;

import com.restaurant.papricica.util.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@jakarta.persistence.Table(name = "reservations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"table_id", "start_date", "status"}))
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int partySize;

    @NotNull
    private OffsetDateTime startDate;

    @NotNull
    private OffsetDateTime endDate;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    private boolean pointsAwarded;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return partySize == that.partySize && Objects.equals(id, that.id) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(createdAt, that.createdAt) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, partySize, startDate, endDate, createdAt, status);
    }

    @PrePersist
    protected void onPrePersist() {
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
        if (this.endDate == null && this.startDate != null) {
            this.endDate = this.startDate.plusHours(2);
        }
    }
}
