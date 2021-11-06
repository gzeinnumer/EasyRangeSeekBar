package com.gzeinnumer.easyrangeseekbar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.gzeinnumer.ers.RangeSeekBar;
import com.gzeinnumer.ers.callback.ListenerSeekBar;
import com.gzeinnumer.ers.callback.TextFormatterSeekBar;
import com.gzeinnumer.ers.helper.Step;

import java.util.ArrayList;
import java.util.List;

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
        List<Step> steps = new ArrayList<>();
//        steps.add(new Step("test", 100, Color.GREEN, Color.BLUE));
//        steps.add(new Step("test", 100, Color.GREEN, Color.parseColor("#cccccc")));
//        steps.add(new Step("test", 100, Color.GREEN, RangeSeekBar.defaultColor));
        steps.add(new Step("test", 250, Color.GREEN, Color.RED));
        final RangeSeekBar rangeSeekBar = findViewById(R.id.rsb_2);
        rangeSeekBar.setMin(100);
        rangeSeekBar.setMax(500);
        rangeSeekBar.setCurrentValue(250);
        rangeSeekBar.addStep(steps);
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
        rangeSeekBar.addStep(new Step("test", 3500, Color.RED, Color.BLUE));

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
//        rangeSeekBar.addStep(new Step("test", 1500, Color.RED, Color.YELLOW));
    }
}