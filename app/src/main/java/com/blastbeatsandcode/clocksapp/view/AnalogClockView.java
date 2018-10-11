package com.blastbeatsandcode.clocksapp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.blastbeatsandcode.clocksapp.controller.ClockController;
import com.blastbeatsandcode.clocksapp.utils.Enums;

import java.util.Calendar;

/*
 * Logic for Analog Clock Views
 * The UI for the face of the clock was made possible with help from this article:
 * https://viblo.asia/p/simple-way-to-create-a-custom-analog-clock-in-android-1VgZv9aRKAw
 */
public class AnalogClockView extends View implements ClockView {
    private Activity _uiThreadActivity;

    // Characteristics of the clock face
    private int _height;
    private int _width;
    private int _padding;
    private int _numberSpacing;

    // Truncate the height of the different clockhands
    private int _minuteHandTruncation;
    private int _hourHandTruncation;

    // Calcuation and drawing variables
    private int _radius;
    private Paint _paint;
    private Rect _rect;
    private boolean _isInit; // Set this flag when the clock is initialized


    /*
     *   AnalogClockView constructor which prepares the view.
     * */
    public AnalogClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * AnalogClockView constructor which accepts the controller for concurrent timekeeping
     * We specify the Textview so we can set it
     * We specify the activity so we can update the UI thread with the time. It is not possible otherwise
     */
    public AnalogClockView(Context context, @Nullable AttributeSet attrs, Activity activity)
    {
        super(context, attrs);
        _uiThreadActivity = activity;

        init();
    }

    /*
     * Prepare the view
     */
    private void init()
    {
        // Set default characteristics
        _height = 0;
        _width = 0;
        _padding = 0;
        _numberSpacing = 0;
        _minuteHandTruncation = 0;
        _hourHandTruncation = 0;
        _radius = 0;
    }

    /*
     * Draw the clock to the view
     */
    @Override
    protected void onDraw(Canvas canvas) {

        if (!_isInit) {
            _paint = new Paint();
            _height = getHeight();
            _width = getWidth();
            _padding = _numberSpacing + 50;  // spacing from the circle border
            int minAttr = Math.min(_height, _width);
            _radius = minAttr / 2 - _padding;
            _rect = new Rect();
            _rect.set(100, 100, 100, 100);

            // Change the lengths of the clock hands
            _minuteHandTruncation = minAttr / 20;
            _hourHandTruncation = minAttr / 17;

            _isInit = true;  // set true once initialized
        }

        // Set to the default background color
        canvas.drawColor(Color.parseColor("#FAFAFA"));

        // Create the circle border
        _paint.reset();
        _paint.setColor(Color.BLACK);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(4);
        _paint.setAntiAlias(true);
        canvas.drawCircle(_width / 2, _height / 2, _radius + _padding - 10, _paint);

        // Create center of clock
        _paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(_width / 2, _height / 2, 12, _paint);  // the 03 clock hands will be rotated from this center point.

        // Set the hours in the circle
        int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        _paint.setTextSize(fontSize);

        // Iterate over each hour
        for (Enums.Hour h : Enums.Hour.values()) {
            String tmp = String.valueOf(h);
            _paint.getTextBounds(tmp, 0, tmp.length(), _rect);  // for circle-wise bounding

            // find the circle-wise (x, y) position as mathematical rule
            double angle = Math.PI / 6 * (h.getValue() - 3);
            int x = (int) (_width / 2 + Math.cos(angle) * _radius - _rect.width() / 2) + 25;
            int y = (int) (_height / 2 + Math.sin(angle) * _radius + _rect.height() / 2);

            canvas.drawText(Integer.toString(h.getValue()), x, y, _paint);
        }

        String month = Integer.toString(ClockController.getInstance().getMonthOfYear());
        String day = Integer.toString(ClockController.getInstance().getDayOfMonth());
        String year = Integer.toString(ClockController.getInstance().getYear());
        String currDate = "Date: " + month + "/" + day + "/" + year;
        canvas.drawText(currDate, 50, 50, _paint);

        /** draw clock hands to represent the every single time */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ClockController.getInstance().getDate());

        float hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;

        drawHandLine(canvas, (hour + calendar.get(Calendar.MINUTE) / 60) * 5f, true, false); // draw hours
        drawHandLine(canvas, calendar.get(Calendar.MINUTE), false, false); // draw minutes
        drawHandLine(canvas, calendar.get(Calendar.SECOND), false, true); // draw seconds

        /** invalidate the appearance for next representation of time  */
        invalidate();
    }

    /*
     * Draw the hand lines
     */
    private void drawHandLine(Canvas canvas, double moment, boolean isHour, boolean isSecond)
    {
        double angle = Math.PI * moment / 30 - Math.PI / 2;
        int handRadius = isHour ? _radius - _minuteHandTruncation - _hourHandTruncation : _radius - _minuteHandTruncation;
        if (isSecond) _paint.setColor(Color.RED);
        canvas.drawLine(_width / 2, _height / 2, (float) (_width / 2 + Math.cos(angle) * handRadius), (float) (_height / 2 + Math.sin(angle) * handRadius), _paint);
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
                        // Invalidate the drawing so that it will be redrawn with current information
                        invalidate(0,0,getWidth(),getHeight());
                    }
                });
            }
        }).start();
    }
}
