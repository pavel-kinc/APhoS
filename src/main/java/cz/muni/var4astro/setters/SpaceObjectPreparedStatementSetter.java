package cz.muni.var4astro.setters;

import cz.muni.var4astro.services.FileHandlingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

import static cz.muni.var4astro.utils.Conversions.angleToFloatForm;
import static cz.muni.var4astro.utils.Conversions.hourAngleToDegrees;

/**
 * The prepared statement setter that sets values from the search form input to the sql query.
 */
public class SpaceObjectPreparedStatementSetter implements PreparedStatementSetter {

    private final String RA;
    private final String dec;
    private final String radius;
    private final String name;
    private final String minMag;
    private final String maxMag;
    private final String catalog;
    private final String objectId;

    private static final Logger log = LoggerFactory.getLogger(FileHandlingService.class);

    /**
     * Instantiates a new Space object prepared statement setter.
     *
     * @param RA       the right ascension
     * @param dec      the declination
     * @param radius   the radius of the search
     * @param name     the name of the space object
     * @param minMag   the min magnitude
     * @param maxMag   the max magnitude
     * @param catalog  the catalog
     * @param objectId the catalog id
     */
    public SpaceObjectPreparedStatementSetter(String RA, String dec, String radius, String name,
                                              String minMag, String maxMag, String catalog, String objectId) {
        this.dec = dec;
        this.RA = RA;
        this.radius = radius;
        this.name = name;
        this.minMag = minMag;
        this.maxMag = maxMag;
        this.catalog = catalog;
        this.objectId = objectId;
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        int queryParamNumber = 1;
        if (!RA.isEmpty()) {
            ps.setFloat(1, angleToFloatForm(dec));
            try {
                ps.setFloat(2, hourAngleToDegrees(RA));
            } catch (ParseException e) {
                log.error("Cannot parse right ascension value", e);
            }
            ps.setFloat(3, Float.parseFloat(radius));
            queryParamNumber = 4;
        }
        if (!name.isEmpty()) {
            ps.setString(queryParamNumber, name);
            queryParamNumber++;
        }
        if (!(minMag.equals("0") && (maxMag.equals("15")))) {
            ps.setFloat(queryParamNumber, Float.parseFloat(minMag));
            ps.setFloat(queryParamNumber + 1, Float.parseFloat(maxMag));
            queryParamNumber += 2;
        }

        if (!catalog.equals("All catalogues")) {
            ps.setString(queryParamNumber, catalog);
            queryParamNumber++;
        }

        if (!objectId.isEmpty()) {
            ps.setString(queryParamNumber, objectId);
        }
    }
}
