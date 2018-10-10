package com.blastbeatsandcode.clocksapp.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.blastbeatsandcode.clocksapp.R;
import com.blastbeatsandcode.clocksapp.controller.ClockCommandQueue;
import com.blastbeatsandcode.clocksapp.controller.ClockController;
import com.blastbeatsandcode.clocksapp.controller.SetTimeCommand;
import com.blastbeatsandcode.clocksapp.model.DateTimeModel;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ClockView {

    DateTimeModel _m;
    ClockController _c;
    LinearLayout _viewsList;
    ScrollView _scrollView;

    // TODO: Remove this, it is temporary
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create the objects we need for the application
        _c = ClockController.getInstance();
        _m = new DateTimeModel(_c);
        _c.registerModel(_m);

        // Register this main activity as a view
        _c.registerView(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the linear layout where we will append the views
        _viewsList = (LinearLayout) findViewById(R.id.scrollview_list);

        // Get the scrollview so we can control it
        _scrollView = (ScrollView) findViewById(R.id.scrollview);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//
//        // Floating action button, button press
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Messages.MakeToast(getApplicationContext(), "The button has been pressed " +
//                        "and a toast has been made!");
//            }
//        });

//        FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.menu);
//        fabMenu.setClosedOnTouchOutside(true);
//        fabMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
//
//            @Override
//            public void onMenuToggle(boolean opened) {
//                if (opened)
//                {
//
//                }
//                else {
//
//                }
//            }
//        });

    }

    // Update the view
    @Override
    public void update() {
        // Set the temporary text
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                DateFormat dateFormat = new SimpleDateFormat("MMMM dd, YYYY hh:mm:ss aa");
//                String dateStr = dateFormat.format(_c.getDate()).toString();
//                text.setText("CURRENT TIME\n\n" + dateStr);
//            }
//        });
    }

    /*
     * Open the time picker dialog to set the time
     * The button assignment is made in the XML layout file
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
                        // Update the date in the controller
                        // Get current time settings
                        Date d = _c.getDate();
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
                        // Get current time settings
                        final Date d = _c.getDate();
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

    public void AddDigitalClockView(View view)
    {
        View digitalClockview = LayoutInflater.from(this).inflate(R.layout.digital_clock_view, null);
        DigitalClockView clockView = new DigitalClockView(getApplicationContext(), null, (TextView) digitalClockview.findViewById(R.id.time_text), this);
        _viewsList.addView(digitalClockview);
        _c.registerView(clockView);
        scrollToBottom();
    }

    public void AddAnalogClockView(View view)
    {
        View analogClockView = LayoutInflater.from(this).inflate(R.layout.analog_clock_view, null, true);
        AnalogClockView clockView = new AnalogClockView(getApplicationContext(), null, this);
        _viewsList.addView(analogClockView);
        _c.registerView(clockView);
        scrollToBottom();
    }

    public void Undo(View view)
    {
        ClockController.getInstance().undo();
    }

    public void Redo(View view)
    {
        ClockController.getInstance().redo();
    }

    /*
     * Scroll to the bottom of the ScrollView
     */
    public void scrollToBottom()
    {
        _scrollView.post(new Runnable() {
            public void run() {
                _scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}
