package com.blastbeatsandcode.clocksapp.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blastbeatsandcode.clocksapp.R;
import com.blastbeatsandcode.clocksapp.controller.ClockController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DigitalClockView extends LinearLayout implements ClockView {

    private TextView _text;
    private ClockController _c;
    private Activity _uiThreadActivity;


    /*
     *   DigitalClockView constructor which prepares the view.
     * */
    public DigitalClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * DigitalClockView constructor which accepts the controller for concurrent timekeeping
     * We specify the Textview so we can set it
     * We specify the activity so we can update the UI thread with the time. It is not possible otherwise
     */
    public DigitalClockView(Context context, @Nullable AttributeSet attrs, TextView textView, Activity activity)
    {
        super(context, attrs);
        _c = ClockController.getInstance();
        _uiThreadActivity = activity;
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.digital_clock_view, this, true);

        init(textView);
    }

    /*
     * Prepare the view
     */
    private void init(TextView textView)
    {
        _text = textView;
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, YYYY hh:mm:ss a");
        String dateStr = dateFormat.format(_c.getDate()).toString();
        try {
            _text.setText("\n" + dateStr + "\n");
        }
        catch (Exception e)
        {
            Log.d("Error","Initial time set failed!");
        }
    }

    /*
     * Update the view
     */
    @Override
    public void update() {
        new Thread(new Runnable() {
            public void run() {
                // We have to run this on the main UI thread, which is why this view accepts an activity in the constructor
                ((Activity) _uiThreadActivity).runOnUiThread(new Runnable() {
                    public void run() {
                        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, YYYY hh:mm:ss a");
                        String dateStr = dateFormat.format(_c.getDate()).toString();

                        try {
                            _text.setText("\n" + dateStr + "\n");
                        }
                        catch(Exception e)
                        {
                            Log.d("Error","Update time thread failed!");
                        }
                    }
                });
            }
        }).start();
    }
}
