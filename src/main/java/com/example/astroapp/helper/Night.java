package com.example.astroapp.helper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Night {

    private Date firstDateOfTheNight;
    private Date secondDateOfTheNight;
    private String username;
    static final long HALF_A_DAY_IN_MS = 12 * 3600 * 1000;

    public Night(Timestamp date, String username) throws ParseException {
        Date time = new Date(date.getTime());
        SimpleDateFormat onlyDateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        Date onlyDate = onlyDateFormatter.parse(onlyDateFormatter.format(time));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(onlyDate);
        if (time.getTime() - onlyDate.getTime() < HALF_A_DAY_IN_MS) {
            calendar.add(Calendar.DATE, -1);
            firstDateOfTheNight = calendar.getTime();
            secondDateOfTheNight = onlyDate;
        } else {
            calendar.add(Calendar.DATE, 1);
            firstDateOfTheNight = onlyDate;
            secondDateOfTheNight = calendar.getTime();
        }
        this.username = username;
    }

    public void setFirstDateOfTheNight(Date firstDateOfTheNight) {
        this.firstDateOfTheNight = firstDateOfTheNight;
    }

    public void setSecondDateOfTheNight(Date secondDateOfTheNight) {
        this.secondDateOfTheNight = secondDateOfTheNight;
    }

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
}
