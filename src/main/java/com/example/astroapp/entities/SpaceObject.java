package com.example.astroapp.entities;

import javax.persistence.*;
import java.util.List;

@Table(name = "object", schema = "public")
public class SpaceObject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "object_generator")
    @SequenceGenerator(name = "object_generator", sequenceName = "object_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "catalog_id")
    private String catalogID;
    private String name;
    private String catalog;
    @Column(name = "catalog_dec")
    private String catalogDec;
    @Column(name = "catalog_rec")
    private String catalogRec;
    @Column(name = "catalog_mag")
    private Float catalogMag;
    @OneToMany(mappedBy = "object")
    private List<Flux> fluxes;

    public SpaceObject(String catalogID, String name, String catalog, String catalogDec, String catalogRec, Float catalogMag) {
        this.catalogID = catalogID;
        this.name = name;
        this.catalog = catalog;
        this.catalogDec = catalogDec;
        this.catalogRec = catalogRec;
        this.catalogMag = catalogMag;
    }

    public SpaceObject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
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
