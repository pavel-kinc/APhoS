package com.example.astroapp.entities;

import javax.persistence.*;

@Entity
@Table(name="aperture", schema="public")
public class Aperture {

    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "fluxid")
    private Flux flux;
    private Float value;
}
