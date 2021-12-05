package com.example.astroapp.entities;

import javax.persistence.*;

@Entity
@Table(name = "aperture", schema = "public")
public class Aperture {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "aperture_generator")
    @SequenceGenerator(name = "aperture_generator", sequenceName = "apeture_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "fluxid")
    private Flux flux;
    private Float value;

    public Aperture() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flux getFlux() {
        return flux;
    }

    public void setFlux(Flux flux) {
        this.flux = flux;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
