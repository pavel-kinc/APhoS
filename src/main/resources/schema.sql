CREATE EXTENSION IF NOT EXISTS cube;
CREATE EXTENSION IF NOT EXISTS earthdistance;

CREATE OR REPLACE FUNCTION earth() RETURNS float8
AS
'select 180 / pi();'
    LANGUAGE SQL;

CREATE TABLE IF NOT EXISTS users
(
    google_sub  VARCHAR(100) NOT NULL PRIMARY KEY,
    username    VARCHAR(50) UNIQUE,
    description VARCHAR(300)
);

CREATE TABLE IF NOT EXISTS space_object
(
    id          SERIAL NOT NULL,
    name        VARCHAR(40),
    catalog     VARCHAR(20),
    catalog_id  VARCHAR(30) UNIQUE,
    catalog_rec VARCHAR(20),
    catalog_dec VARCHAR(20),
    coordinates earth,
    catalog_mag float4,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS photo_properties
(
    id             SERIAL    NOT NULL,
    exposure_begin timestamp NOT NULL,
    exposure_end   timestamp NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS flux
(
    id                  SERIAL       NOT NULL,
    rec                 VARCHAR(20)  NOT NULL,
    dec                 VARCHAR(20)  NOT NULL,
    coordinates         earth,
    ap_auto             float8,
    ap_auto_dev         float8,
    object_id           int4 REFERENCES space_object (id),
    user_id             VARCHAR(100) NOT NULL REFERENCES users (google_sub),
    photo_properties_id int4         NOT NULL REFERENCES photo_properties (id),
    apertures           float8[],
    aperture_devs       float8[],
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS uploading_logs
(
    id             SERIAL       NOT NULL,
    user_id        VARCHAR(100) NOT NULL REFERENCES users (google_sub),
    time_of_upload timestamp,
    success_cnt    int4,
    error_cnt      int4,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS uploading_error_messages
(
    id               SERIAL NOT NULL,
    uploading_log_id int4 REFERENCES uploading_logs (id),
    filename         VARCHAR(50),
    error_message    VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS object_coordinates_index
    ON space_object (coordinates);

CREATE INDEX IF NOT EXISTS object_catalog_index
    ON space_object (catalog);

CREATE INDEX IF NOT EXISTS object_catalog_mag_index
    ON space_object (catalog_mag);

CREATE INDEX IF NOT EXISTS flux_object_id_index
    ON flux (object_id);

CREATE INDEX IF NOT EXISTS flux_user_id_index
    ON flux (user_id);

CREATE INDEX IF NOT EXISTS flux_photo_properties_id_index
    ON flux (photo_properties_id);
