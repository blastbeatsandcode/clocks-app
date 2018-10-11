package com.blastbeatsandcode.clocksapp.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blastbeatsandcode.clocksapp.R;
import com.blastbeatsandcode.clocksapp.controller.ClockCommandQueue;
import com.blastbeatsandcode.clocksapp.controller.ClockController;
import com.blastbeatsandcode.clocksapp.controller.SetTimeCommand;
import com.blastbeatsandcode.clocksapp.model.DateTimeModel;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;

import java.util.Calendar;
import java.util.Date;

/*
 * MainActivity acts as the insertion point of the android application
 * We also use it as a view that houses the other views
 */
public class MainActivity extends AppCompatActivity implements ClockView {
    // Because MainActivity is the insertion point for the program,
    // We create the model here
    private DateTimeModel _m;
    private LinearLayout _viewsList;
    private ScrollView _scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create the objects we need for the application
        _m = new DateTimeModel();
        ClockController.getInstance().registerModel(_m);

        // Register this main activity as a view
        ClockController.getInstance().registerView(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the linear layout where we will append the views
        _viewsList = (LinearLayout) findViewById(R.id.scrollview_list);

        // Get the scrollview so we can control it
        _scrollView = (ScrollView) findViewById(R.id.scrollview);
    }

    // Update the view
    @Override
    public void update() {
        // We don't have to do anything here since this view does not have a time to update itself
    }

    /*
     * Open the time picker dialog to set the time
     * The button assignment is made in the XML layout file
     * This time picker includes the seconds, thanks to this project:
     * https://github.com/IvanKovac/TimePickerWithSeconds
     */
    public void OpenTimePickerDialog(View view) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {
                // Update the time in the controller
                    Date d = ClockController.getInstance().getDate();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.set(Calendar.HOUR, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);
                    cal.set(Calendar.SECOND, seconds);

                    // There was an issue with the AM and PM not being properly set and for some reason this fixes it??
                    cal.set(Calendar.AM_PM, Calendar.AM);

                    // update the time
                    SetTimeCommand c = new SetTimeCommand(cal.getTime());
                    ClockCommandQueue.getInstance().add(c);
                    c.Execute();
            }
        }, hour, minute, second, false);
        mTimePicker.show();

    }

    /*
     * Open the date picker dialog to set the date
     * The button assignment is made in the XML layout file
     */
    public void OpenDatePickerDialog(View view) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Launch date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update the date in the controller
                        final Date d = ClockController.getInstance().getDate();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // update the time
                        SetTimeCommand c = new SetTimeCommand(cal.getTime());
                        ClockCommandQueue.getInstance().add(c);
                        c.Execute();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    /*
     * Add a digital clock to the view
     */
    public void AddDigitalClockView(View view)
    {
        // Create the view and inflate it into the linear layout
        View digitalClockview = LayoutInflater.from(this).inflate(R.layout.digital_clock_view, null);

        // Create the DigitalClockView so we can manage the UI display
        DigitalClockView clockView = new DigitalClockView(getApplicationContext(), null, (TextView) digitalClockview.findViewById(R.id.time_text), this);

        // Add the view to the linear layout and register the view to the controller so we can update it
        _viewsList.addView(digitalClockview);
        ClockController.getInstance().registerView(clockView);

        // Scroll to bottom for clean UX
        scrollToBottom();
    }

    /*
     * Add an analog clock to the view
     */
    public void AddAnalogClockView(View view)
    {
        // Create the view and inflate it into the linear layout
        View analogClockView = LayoutInflater.from(this).inflate(R.layout.analog_clock_view, null, true);

        // Create the AnalogClockView so we can manage the UI display
        AnalogClockView clockView = new AnalogClockView(getApplicationContext(), null, this);

        // Add the view to the linear layout and register the view to the controller so we can update it
        _viewsList.addView(analogClockView);
        ClockController.getInstance().registerView(clockView);

        // Scroll to bottom for clean UX
        scrollToBottom();
    }

    /*
     * Execute Undo
     */
    public void Undo(View view)
    {
        ClockController.getInstance().undo();
    }

    /*
     * Execute Redo
     */
    public void Redo(View view)
    {
        ClockController.getInstance().redo();
    }

    /*
     * Scroll to the bottom of the ScrollView
     */
    private void scrollToBottom()
    {
        _scrollView.post(new Runnable() {
            public void run() {
                _scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}
