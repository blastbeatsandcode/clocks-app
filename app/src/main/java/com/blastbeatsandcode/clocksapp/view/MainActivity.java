package com.blastbeatsandcode.clocksapp.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.blastbeatsandcode.clocksapp.R;
import com.blastbeatsandcode.clocksapp.controller.ClockController;
import com.blastbeatsandcode.clocksapp.model.DateTimeModel;
import com.blastbeatsandcode.clocksapp.utils.Messages;

public class MainActivity extends AppCompatActivity implements ClockView {

    DateTimeModel _model;
    ClockController _controller;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create the objects we need for the application
        _controller = new ClockController();
        _model = new DateTimeModel(_controller);
        _controller.registerModel(_model);

        // Register this main activity as a view
        _controller.registerView(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: This is only temporary. Update this and make it do something useful.
        // Set the temporary text
        text = (TextView) findViewById(R.id.temp_text);
        text.setText("CURRENT TIME\n\n" + _model.getDate().toString());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Add some new clocks and stuff!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Messages.MakeToast(getApplicationContext(), "The button has been pressed " +
                        "and a toast has been made!");
            }
        });
    }

    // Update the view
    @Override
    public void update() {
        // Set the temporary text
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText("CURRENT TIME\n\n" + _model.getDate().toString());
            }
        });
    }
}
