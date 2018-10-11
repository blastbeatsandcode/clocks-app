package com.blastbeatsandcode.clocksapp.controller;

import java.util.Date;

public class SetTimeCommand implements ClockCommand {
    private Date _previousDate;
    private Date _currentDate;

    public SetTimeCommand(Date date)
    {
        _previousDate = ClockController.getInstance().getDate();
        _currentDate = date;
    }

    // Set time to current time
    @Override
    public void Execute() {
        ClockController.getInstance().setTime(_currentDate);
    }

    // Set time to previous time
    @Override
    public void UnExecute() {
        ClockController.getInstance().setTime(_previousDate);
    }
}
