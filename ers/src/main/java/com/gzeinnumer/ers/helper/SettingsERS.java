package com.gzeinnumer.ers.helper;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.gzeinnumer.ers.R;
import com.gzeinnumer.ers.RangeSeekBar;

public class SettingsERS {
    private final RangeSeekBar rangeSeekBar;
    private final Paint paintBar;
    private final Paint paintIndicator;
    private final Paint paintStep;
    private final TextPaint paintTextTop;
    private final TextPaint paintTextBottom;
    private final TextPaint paintBubbleTextCurrent;
    private final Paint paintBubble;
    private int colorBackground = Color.parseColor("#cccccc");
    private int colorStoppover = Color.BLACK;
    private int textColor = Color.parseColor("#6E6E6E");
    private int textTopSize = 12;
    private int textBottomSize = 12;
    private int textSizeBubbleCurrent = 16;
    private float barHeight = 15;
    private float paddingCorners;
    private boolean step_colorizeOnlyBeforeIndicator = true;
    private boolean drawTextOnTop = true;
    private boolean drawTextOnBottom = true;
    private boolean drawBubble = true;
    private boolean modeRegion = false;
    private boolean indicatorInside = false;
    private boolean regions_textFollowRegionColor = false;
    private boolean regions_centerText = false;
    private int regionColorLeft = Color.parseColor("#007E90");
    private int regionColorRight = Color.parseColor("#ed5564");
    private int bubbleColorEditing = Color.WHITE;

    public SettingsERS(RangeSeekBar rangeSeekBar) {
        this.rangeSeekBar = rangeSeekBar;

        paintIndicator = new Paint();
        paintIndicator.setAntiAlias(true);
        paintIndicator.setStrokeWidth(2);

        paintBar = new Paint();
        paintBar.setAntiAlias(true);
        paintBar.setStrokeWidth(2);
        paintBar.setColor(colorBackground);

        paintStep = new Paint();
        paintStep.setAntiAlias(true);
        paintStep.setStrokeWidth(5);
        paintStep.setColor(colorStoppover);

        paintTextTop = new TextPaint();
        paintTextTop.setAntiAlias(true);
        paintTextTop.setStyle(Paint.Style.FILL);
        paintTextTop.setColor(textColor);
        paintTextTop.setTextSize(textTopSize);

        paintTextBottom = new TextPaint();
        paintTextBottom.setAntiAlias(true);
        paintTextBottom.setStyle(Paint.Style.FILL);
        paintTextBottom.setColor(textColor);
        paintTextBottom.setTextSize(textBottomSize);

        paintBubbleTextCurrent = new TextPaint();
        paintBubbleTextCurrent.setAntiAlias(true);
        paintBubbleTextCurrent.setStyle(Paint.Style.FILL);
        paintBubbleTextCurrent.setColor(Color.WHITE);
        paintBubbleTextCurrent.setStrokeWidth(2);
        paintBubbleTextCurrent.setTextSize(dpToPx(textSizeBubbleCurrent));

        paintBubble = new Paint();
        paintBubble.setAntiAlias(true);
        paintBubble.setStrokeWidth(3);
    }

    public void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RangeSeekBar);
            setColorBackground(a.getColor(R.styleable.RangeSeekBar_rsb_barBackgroundColor, colorBackground));

            this.drawTextOnTop = a.getBoolean(R.styleable.RangeSeekBar_rsb_topTextVisible, drawTextOnTop);
            setTextTopSize(a.getDimensionPixelSize(R.styleable.RangeSeekBar_rsb_topTextSize, (int) dpToPx(textTopSize)));
            this.drawTextOnBottom = a.getBoolean(R.styleable.RangeSeekBar_rsb_bottomTextVisible, drawTextOnBottom);
            setTextBottomSize(a.getDimensionPixelSize(R.styleable.RangeSeekBar_rsb_bottomTextSize, (int) dpToPx(textBottomSize)));

            this.barHeight = a.getDimensionPixelOffset(R.styleable.RangeSeekBar_rsb_barHeight, (int) dpToPx(barHeight));
            this.drawBubble = a.getBoolean(R.styleable.RangeSeekBar_rsb_showBubble, drawBubble);

            this.regionColorLeft = a.getColor(R.styleable.RangeSeekBar_rsb_leftColor, regionColorLeft);
            this.regionColorRight = a.getColor(R.styleable.RangeSeekBar_rsb_rightColor, regionColorRight);

            this.indicatorInside = a.getBoolean(R.styleable.RangeSeekBar_rsb_smallDot, indicatorInside);
            this.regions_textFollowRegionColor = a.getBoolean(R.styleable.RangeSeekBar_rsb_textFollowRegionColor, regions_textFollowRegionColor);
            this.regions_centerText = a.getBoolean(R.styleable.RangeSeekBar_rsb_textFollowDot, regions_centerText);

            a.recycle();
        }
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
        rangeSeekBar.update();
    }

    public void setTextTopSize(int textSize) {
        this.textTopSize = textSize;
        this.paintTextTop.setTextSize(textSize);
        rangeSeekBar.update();
    }

    public void setTextBottomSize(int textSize) {
        this.textBottomSize = textSize;
        this.paintTextBottom.setTextSize(textSize);
        rangeSeekBar.update();
    }

    private float dpToPx(int size) {
        return size * rangeSeekBar.getResources().getDisplayMetrics().density;
    }

    private float dpToPx(float size) {
        return size * rangeSeekBar.getResources().getDisplayMetrics().density;
    }

    //sini

    public TextPaint getPaintTextTop() {
        return paintTextTop;
    }

    public TextPaint getPaintTextBottom() {
        return paintTextBottom;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public int getTextSizeBubbleCurrent() {
        return textSizeBubbleCurrent;
    }

    public float getBarHeight() {
        return barHeight;
    }

    public float getPaddingCorners() {
        return paddingCorners;
    }

    public void setPaddingCorners(float paddingCorners) {
        this.paddingCorners = paddingCorners;
    }

    public boolean isDrawTextOnTop() {
        return drawTextOnTop;
    }

    public boolean isDrawTextOnBottom() {
        return drawTextOnBottom;
    }

    public boolean isDrawBubble() {
        return drawBubble;
    }

    public boolean isIndicatorInside() {
        return indicatorInside;
    }

    public boolean isRegions_textFollowRegionColor() {
        return regions_textFollowRegionColor;
    }

    public boolean isRegions_centerText() {
        return regions_centerText;
    }

    public int getRegionColorLeft() {
        return regionColorLeft;
    }

    public int getRegionColorRight() {
        return regionColorRight;
    }

    public int getBubbleColorEditing() {
        return bubbleColorEditing;
    }

    public Paint getPaintBar() {
        return paintBar;
    }

    public Paint getPaintIndicator() {
        return paintIndicator;
    }

    public TextPaint getPaintBubbleTextCurrent() {
        return paintBubbleTextCurrent;
    }

    public Paint getPaintBubble() {
        return paintBubble;
    }
}
