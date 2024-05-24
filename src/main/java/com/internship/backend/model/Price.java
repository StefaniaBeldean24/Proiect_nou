package com.internship.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String season;
    private String periodOfDay;
    private int price;

    @ManyToOne
    @JoinColumn(name="tennis_court_id")
    @JsonBackReference
    private TennisCourt tennisCourt;
}
