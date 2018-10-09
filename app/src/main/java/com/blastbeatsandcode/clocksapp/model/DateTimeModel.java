package com.blastbeatsandcode.clocksapp.model;

import android.util.Log;

import com.blastbeatsandcode.clocksapp.controller.ClockCommandQueue;
import com.blastbeatsandcode.clocksapp.controller.ClockController;

import java.time.Clock;
import java.util.Calendar;
import java.util.Date;

import com.blastbeatsandcode.clocksapp.controller.SetTimeCommand;
import com.blastbeatsandcode.clocksapp.utils.Messages;

import static android.content.ContentValues.TAG;

public class DateTimeModel {
    ClockController _c;
    Date _currentTime;

    // Constructor for the DateTimeModel
    public DateTimeModel(ClockController c)
    {
        _c = ClockController.getInstance();

        // Set current time as the actual current time by default
        _currentTime = Calendar.getInstance().getTime();

        // Spawn a thread that adds a second onto the clock every second
        Thread thread = new Thread(){
            public void run(){
                while(!isInterrupted()) {
                    try {
                        Thread.sleep(1000);
                        _currentTime = addSecond(_currentTime);
                        update();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();

    }

    /*
     * Updates the controller.
     * Update is in it's own method because we need to be able to call it from a thread.
     */
    private void update()
    {
        _c.update();
    }

    public Date getDate()
    {
        return _currentTime;
    }

    public void setDate(Date dateTime)
    {
        _currentTime = dateTime;
        update();
    }

    /*
     * Adds one second onto the given date object.
     */
    public Date addSecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, 1);
        return cal.getTime();
    }
}
