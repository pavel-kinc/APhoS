package cz.muni.aphos.dto;

/**
 * The Data Transfer Object representing a space object.
 */
public class SpaceObject {
    private Long id;
    private String catalogId;
    private String name;
    private String catalog;
    private String catalogDec;
    private String catalogRec;
    private Float catalogMag;

    /**
     * Instantiates a new Space object.
     *
     * @param catalogId  the catalog id of the object
     * @param name       the name of the object
     * @param catalog    the catalog used
     * @param catalogDec the catalog declination
     * @param catalogRec the catalog right ascension
     * @param catalogMag the catalog magnitude
     */
    public SpaceObject(String catalogId, String name, String catalog,
                       String catalogDec, String catalogRec, Float catalogMag) {
        this.catalogId = catalogId;
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

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
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
