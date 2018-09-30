package com.blastbeatsandcode.clocksapp.model;

import com.blastbeatsandcode.clocksapp.controller.ClockController;

import java.util.Calendar;
import java.util.Date;

public class DateTimeModel {
    ClockController _c;
    Date _currentTime;

    // Constructor for the DateTimeModel
    public DateTimeModel(ClockController c)
    {
        _c = c;

        // Set current time as the actual current time by default
        _currentTime = Calendar.getInstance().getTime();
    }

    public Date getDate()
    {
        return _currentTime;
    }

    public void setDate(Date dateTime)
    {
        _currentTime = dateTime;
        _c.update();
    }
}
