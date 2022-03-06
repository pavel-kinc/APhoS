package com.example.astroapp.helper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * The type Night to represent a night during which a certain set of photos was taken.
 * This is a helper class to identify a certain night with the intention of setting aperture
 * for that night. One night includes times from 12:00 of the first date to 12:00 of the second date.
 */
public class Night implements Comparable<Night> {

    private String firstDateOfTheNight;
    private String secondDateOfTheNight;
    private String username;
    private int idOnPage;
    private int apToBeUsed;
    private int refApToBeUsed;

    /**
     * 12 hours represented in milliseconds
     */
    static final long HALF_A_DAY_IN_MS = 12 * 3600 * 1000;

    /**
     * Instantiates a new Night.
     *
     * @param date     the time when the photo was taken
     * @param username the username of the uploader
     * @throws ParseException the parse exception
     */
    public Night(Timestamp date, String username) throws ParseException {
        Date time = new Date(date.getTime());
        SimpleDateFormat onlyDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date onlyDate = onlyDateFormatter.parse(onlyDateFormatter.format(time));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(onlyDate);
        if (time.getTime() - onlyDate.getTime() < HALF_A_DAY_IN_MS) {
            calendar.add(Calendar.DATE, -1);
            firstDateOfTheNight = onlyDateFormatter.format(calendar.getTime());
            secondDateOfTheNight = onlyDateFormatter.format(onlyDate);
        } else {
            calendar.add(Calendar.DATE, 1);
            firstDateOfTheNight = onlyDateFormatter.format(onlyDate);
            secondDateOfTheNight = onlyDateFormatter.format(calendar.getTime());
        }
        this.username = username;
    }

    public int getApToBeUsed() {
        return apToBeUsed;
    }

    public void setApToBeUsed(int apToBeUsed) {
        this.apToBeUsed = apToBeUsed;
    }

    public int getRefApToBeUsed() {
        return refApToBeUsed;
    }

    public void setRefApToBeUsed(int refApToBeUsed) {
        this.refApToBeUsed = refApToBeUsed;
    }

    public int getIdOnPage() {
        return idOnPage;
    }

    public void setIdOnPage(int idOnPage) {
        this.idOnPage = idOnPage;
    }

    /**
     * Gets first date of the night.
     *
     * @return the first date of the night
     */
    public String getFirstDateOfTheNight() {
        return firstDateOfTheNight;
    }

    /**
     * Gets second date of the night.
     *
     * @return the second date of the night
     */
    public String getSecondDateOfTheNight() {
        return secondDateOfTheNight;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets first date of the night.
     *
     * @param firstDateOfTheNight the first date of the night
     */
    public void setFirstDateOfTheNight(String firstDateOfTheNight) {
        this.firstDateOfTheNight = firstDateOfTheNight;
    }

    /**
     * Sets second date of the night.
     *
     * @param secondDateOfTheNight the second date of the night
     */
    public void setSecondDateOfTheNight(String secondDateOfTheNight) {
        this.secondDateOfTheNight = secondDateOfTheNight;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Night night = (Night) o;
        return firstDateOfTheNight.equals(night.firstDateOfTheNight)
                && secondDateOfTheNight.equals(night.secondDateOfTheNight)
                && username.equals(night.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstDateOfTheNight, secondDateOfTheNight, username);
    }


    @Override
    public int compareTo(Night o) {
        SimpleDateFormat onlyDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date thisDate = onlyDateFormatter.parse(this.firstDateOfTheNight);
            Date thatDate = onlyDateFormatter.parse(o.firstDateOfTheNight);
            if (thisDate.getTime() < thatDate.getTime()) {
                return -1;
            } else if (thisDate.getTime() > thatDate.getTime()) {
                return 1;
            }
            return this.username.compareTo(o.username);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
