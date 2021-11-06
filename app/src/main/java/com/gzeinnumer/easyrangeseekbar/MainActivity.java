package com.gzeinnumer.easyrangeseekbar;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.gzeinnumer.ers.RangeSeekBar;
import com.gzeinnumer.ers.callback.ListenerSeekBar;
import com.gzeinnumer.ers.callback.TextFormatterSeekBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init2();
        init3();
        init4();
    }

    private void init2() {
        final RangeSeekBar rangeSeekBar = findViewById(R.id.rsb_2);
        rangeSeekBar.setMin(100);
        rangeSeekBar.setMax(500);
        rangeSeekBar.setCurrentValue(250);
        rangeSeekBar.setTextMax("max\nvalue");
        rangeSeekBar.setTextMin("min\nvalue");
        rangeSeekBar.setTextFormatter(new TextFormatterSeekBar() {
            @Override
            public String format(float value) {
                return String.format("Rp. %d", (int) value);
            }
        });
        rangeSeekBar.setListener(new ListenerSeekBar() {
            @Override
            public void valueChanged(RangeSeekBar rangeSeekBar, float currentValue) {
                Log.d("_TAG", "valueChanged: "+currentValue);
            }
        });
    }

    private void init3() {
        final RangeSeekBar rangeSeekBar = (RangeSeekBar) findViewById(R.id.rsb_3);
        rangeSeekBar.setMax(5000);

    }

    private void init4() {
        final RangeSeekBar rangeSeekBar = (RangeSeekBar) findViewById(R.id.rsb_4);
        rangeSeekBar.setMax(3000);
//        rangeSeekBar.setCurrentValue(1500);
        rangeSeekBar.setRegionTextFormatter(new RangeSeekBar.RegionTextFormatter() {
            @Override
            public String format(int region, float value) {
                return String.format("region %d : %d", region, (int) value);
            }
        });
    }
}