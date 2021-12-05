package com.example.astroapp.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "flux", schema = "public")
public class Flux {
    @Id
    private Long id;
    private String rec;
    private String dec;
    private Float apertureAuto;
    @OneToMany(mappedBy = "flux")
    private Set<Aperture> apertures;
    @ManyToOne
    @JoinColumn(name = "photo_properties_id")
    private PhotoProperties photoProperty;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name="object_id")
    private SpaceObject object;

}
