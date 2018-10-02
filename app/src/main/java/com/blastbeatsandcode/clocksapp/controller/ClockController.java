package com.blastbeatsandcode.clocksapp.controller;

import com.blastbeatsandcode.clocksapp.model.DateTimeModel;
import com.blastbeatsandcode.clocksapp.view.ClockView;

import java.util.ArrayList;

/*
 * ClockController is the controller for Clocks-App.
 * Handles communicating between the views and the clock model.
 * */
public class ClockController {
    // Hold a collection of views so they may be referenced
    private ArrayList<ClockView> _views;

    // Hold a reference to the datetime model
    private DateTimeModel _m;

    /*
     * Empty constructor
     * Instead, we register the model outside of the constructor
     * For proper initialization
     * */
    public ClockController()
    {
        _views = new ArrayList<ClockView>();
    }

    /*
     * Add the model to the controller
     * */
    public void registerModel(DateTimeModel model)
    {
        _m = model;
    }

    /*
     * Add view to views collection
     */
    public void registerView(ClockView v)
    {
        _views.add(v);
    }

    // TODO: Implement this!
    /*
     * Update the views
     */
    public void update()
    {
        for(ClockView v : _views)
        {
            v.update();
        }
    }
}
