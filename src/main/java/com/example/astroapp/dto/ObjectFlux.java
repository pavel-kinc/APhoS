package com.example.astroapp.dto;

public class ObjectFlux {

    private String name;
    private String catalog;
    private String catalogId;
    private Float catalogRec;
    private Float catalogDec;
    private Float catalogMag;
    private int numberOfFluxes;

    public ObjectFlux() {
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

    public Float getCatalogRec() {
        return catalogRec;
    }

    public void setCatalogRec(Float catalogRec) {
        this.catalogRec = catalogRec;
    }

    public Float getCatalogDec() {
        return catalogDec;
    }

    public void setCatalogDec(Float catalogDec) {
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
