package com.restaurant.papricica.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@jakarta.persistence.Table(name = "tables")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    public Table() { /* Default constructor */ }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reservations", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Set<Reservation> reservations = new HashSet<>();
}
