package com.example.astroapp.dto;

public class ObjectFluxCount {

    private long id;
    private String name;
    private String catalog;
    private String catalogId;
    private String catalogRec;
    private String catalogDec;
    private Float catalogMag;
    private int numberOfFluxes;

    public ObjectFluxCount() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
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

    public Float getCatalogMag() {
        return catalogMag;
    }

    public void setCatalogMag(Float catalogMag) {
        this.catalogMag = catalogMag;
    }

    public int getNumberOfFluxes() {
        return numberOfFluxes;
    }

    public void setNumberOfFluxes(int numberOfFluxes) {
        this.numberOfFluxes = numberOfFluxes;
    }
}
