package com.gzeinnumer.easyrangeseekbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gzeinnumer.ers.RangeSeekBar;
import com.gzeinnumer.ers.callback.ListenerSeekBar;

public class ExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        RangeSeekBar rangeSeekBar = findViewById(R.id.rsb);
        rangeSeekBar.setMin(100);
        rangeSeekBar.setMax(500);
        rangeSeekBar.setListener(new ListenerSeekBar() {
            @Override
            public void valueChanged(RangeSeekBar rangeSeekBar, float currentValue) {

            }
        });
    }
}