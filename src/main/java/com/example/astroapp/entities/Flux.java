package com.example.astroapp.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "flux", schema = "public")
public class Flux {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flux_generator")
    @SequenceGenerator(name = "flux_generator", sequenceName = "flux_id_seq", allocationSize = 1)
    private Long id;
    private Float rec;
    private Float dec;
    @Column(name = "ap_auto")
    private Float apertureAuto;
    @OneToMany(mappedBy = "flux")
    private Set<Aperture> apertures;
    @ManyToOne
    @JoinColumn(name = "photo_properties_id")
    private PhotoProperties photoProperty;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
//    @ManyToOne
//    @JoinColumn(name = "object_id")
//    private SpaceObject object;

    public Flux() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRec() {
        return rec;
    }

    public void setRec(Float rec) {
        this.rec = rec;
    }

    public Float getDec() {
        return dec;
    }

    public void setDec(Float dec) {
        this.dec = dec;
    }

    public Float getApertureAuto() {
        return apertureAuto;
    }

    public void setApertureAuto(Float apertureAuto) {
        this.apertureAuto = apertureAuto;
    }

    public PhotoProperties getPhotoProperty() {
        return photoProperty;
    }

    public void setPhotoProperty(PhotoProperties photoProperty) {
        this.photoProperty = photoProperty;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
