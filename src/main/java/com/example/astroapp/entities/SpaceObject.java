package com.example.astroapp.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "object", schema = "public")
public class SpaceObject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "object_generator")
    @SequenceGenerator(name = "object_generator", sequenceName = "object_id_seq", allocationSize = 1)
    private Long id;
    private String catalog;
    private String catalogRec;
    private String catalogDec;
    private float catalogMag;
    @OneToMany(mappedBy = "object")
    private List<Flux> fluxes;

    public SpaceObject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCatalogRec() {
        return catalogRec;
    }

    public void setCatalogRec(String catalogRec) {
        this.catalogRec = catalogRec;
    }

    public String getCatalogDec() {
        return catalogDec;
    }

    public void setCatalogDec(String catalogDec) {
        this.catalogDec = catalogDec;
    }

    public float getCatalogMag() {
        return catalogMag;
    }

    public void setCatalogMag(float catalogMag) {
        this.catalogMag = catalogMag;
    }
}
