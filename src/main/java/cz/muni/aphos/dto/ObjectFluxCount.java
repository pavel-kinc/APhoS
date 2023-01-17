package cz.muni.aphos.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import cz.muni.aphos.helper.ViewField;

/**
 * The Data Transfer Object for a result of user searching for a space object.
 * It consists of the object catalog info and number of its fluxes in the database.
 */

@JsonPropertyOrder({"id", "catalog","name", "rightAsc", "declination", "magnitude", "fluxesCount" })
public class ObjectFluxCount {

    @JsonIgnore()
    private long id;

    @JsonProperty("name")
    @JsonView(ViewField.Public.class)
    private String name;

    @JsonProperty("catalog")
    @JsonView(ViewField.Public.class)
    private String catalog;

    @JsonProperty("id")
    @JsonView(ViewField.Public.class)
    private String catalogId;

    @JsonProperty("rightAsc")
    @JsonView(ViewField.Public.class)
    private String catalogRec;

    @JsonProperty("declination")
    @JsonView(ViewField.Public.class)
    private String catalogDec;

    @JsonProperty("magnitude")
    @JsonView(ViewField.Public.class)
    private Float catalogMag;
    /**
     * number of fluxes the object has in the database
     */

    @JsonProperty("fluxesCount")
    @JsonView(ViewField.Public.class)
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
