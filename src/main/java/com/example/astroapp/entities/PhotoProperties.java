package com.example.astroapp.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "photo_properties", schema = "public")
public class PhotoProperties {
    @Id
    private long id;
    private Timestamp exposureBegin;
    private Timestamp exposureEnd;
}
