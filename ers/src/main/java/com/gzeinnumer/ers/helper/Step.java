package com.gzeinnumer.ers.helper;

import android.graphics.Color;

import androidx.annotation.NonNull;

public class Step implements Comparable<Step> {
    private final String name;
    private final float value;

    private float xStart;
    private final int colorBefore;
    private int colorAfter = Color.parseColor("#ed5564");

    public Step(String name, float value, int colorBefore) {
        this.name = name;
        this.value = value;
        this.colorBefore = colorBefore;
    }

    public Step(String name, float value, int colorBefore, int colorAfter) {
        this(name, value, colorBefore);
        this.colorAfter = colorAfter;
    }

    @Override
    public int compareTo(@NonNull Step o) {
        return Float.compare(value, o.value);
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }

    public float getxStart() {
        return xStart;
    }

    public void setxStart(float xStart) {
        this.xStart = xStart;
    }

    public int getColorBefore() {
        return colorBefore;
    }

    public int getColorAfter() {
        return colorAfter;
    }

    public void setColorAfter(int colorAfter) {
        this.colorAfter = colorAfter;
    }
}