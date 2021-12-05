package com.example.astroapp.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "object", schema = "public")
public class SpaceObject {
    @Id
    private Long id;
    private String catalog;
    private String catalogRec;
    private String catalogDec;
    private float catalogMag;
    @OneToMany(mappedBy = "object")
    private List<Flux> fluxes;
}
