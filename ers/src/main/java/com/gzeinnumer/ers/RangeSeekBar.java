package com.gzeinnumer.ers;

import static android.view.MotionEvent.ACTION_UP;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.gzeinnumer.ers.callback.ListenerSeekBar;
import com.gzeinnumer.ers.callback.TextFormatterSeekBar;
import com.gzeinnumer.ers.helper.EurosTextFormatterSeekBar;
import com.gzeinnumer.ers.helper.SettingsERS;
import com.gzeinnumer.ers.helper.TouchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RangeSeekBar extends FrameLayout {

    private static final float DISTANCE_TEXT_BAR = 10;
    private static final float BUBBLE_PADDING_HORIZONTAL = 15;
    private static final float BUBBLE_PADDING_VERTICAL = 10;
    private static final float BUBBLE_ARROW_HEIGHT = 10;
    private static final float BUBBLE_ARROW_WIDTH = 10;
    boolean moving = false;
    private ListenerSeekBar listenerSeekBar;
    private GestureDetectorCompat detector;
    private SettingsERS settingsERS;
    private float max = 1000;
    private float min = 0;
    private float currentValue = 0;
    private float oldValue = Float.MIN_VALUE;
    private float barY;
    private float barWidth;
    private float indicatorX;
    private int indicatorRadius;
    private float barCenterY;
    private Bubble bubble = new Bubble();
    private TextFormatterSeekBar textFormatterSeekBar = new EurosTextFormatterSeekBar();
    private RegionTextFormatter regionTextFormatter = null;

    private String textMax = "";
    private String textMin = "";
    private int calculatedHieght = 0;
    private boolean isEditing = false;
    private String textEditing = "";
    private EditText editText;
    private TouchView touchView;

    public static int defaultColor = Color.parseColor("#cccccc");

    @Nullable
    private ViewGroup parentScroll;

    public RangeSeekBar(Context context) {
        this(context, null);
    }

    public RangeSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        parentScroll = (ViewGroup) getScrollableParentView();
    }

    private void closeEditText() {
        editText.clearFocus();

        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        ((ViewGroup) touchView.getParent()).removeView(touchView);
        removeView(editText);

        isEditing = false;
        if (TextUtils.isEmpty(textEditing)) {
            textEditing = String.valueOf(currentValue);
        }
        float value;
        try {
            value = Float.parseFloat(textEditing);
        } catch (Exception e) {
            e.printStackTrace();
            value = min;
        }

        value = Math.min(value, max);
        value = Math.max(value, min);
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentValue, value);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setCurrentValueNoUpdate(((float) animation.getAnimatedValue()));
                postInvalidate();
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
        editText = null;
        touchView = null;
        postInvalidate();
    }

    private void editBubbleEditPosition() {
        if (isEditing) {
            editText.setX(Math.min(bubble.getX(), getWidth() - editText.getWidth()));
            editText.setY(bubble.getY());

            final ViewGroup.LayoutParams params = editText.getLayoutParams();
            params.width = (int) bubble.width;
            params.height = (int) bubble.getHeight();
            editText.setLayoutParams(params);

            editText.animate().alpha(1f);
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            dispatchKeyEvent(event);
            closeEditText();
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }


    private void init(Context context, @Nullable AttributeSet attrs) {
        setWillNotDraw(false);

        detector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                return super.onContextClick(e);
            }
        });

        this.settingsERS = new SettingsERS(this);
        this.settingsERS.init(context, attrs);
    }

    public void setListener(ListenerSeekBar listenerSeekBar) {
        this.listenerSeekBar = listenerSeekBar;
    }

    private float dpToPx(int size) {
        return size * getResources().getDisplayMetrics().density;
    }

    private float dpToPx(float size) {
        return size * getResources().getDisplayMetrics().density;
    }

    public void setMax(float max) {
        this.max = max;
        updateValues();
        update();
    }

    public void setMin(float min) {
        this.min = min;
        updateValues();
        update();
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float value) {
        this.currentValue = value;
        updateValues();
        update();
    }

    private void setCurrentValueNoUpdate(float value) {
        this.currentValue = value;
        listenerSeekBar.valueChanged(RangeSeekBar.this, currentValue);
        updateValues();

    }

    private View getScrollableParentView() {
        View view = this;
        while (view.getParent() != null && view.getParent() instanceof View) {
            view = (View) view.getParent();
            if (view instanceof ScrollView || view instanceof RecyclerView || view instanceof NestedScrollView) {
                return view;
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return handleTouch(event);
    }

    boolean handleTouch(MotionEvent event) {
        if (isEditing) {
            return false;
        }
        boolean handledByDetector = this.detector.onTouchEvent(event);
        if (!handledByDetector) {

            final int action = MotionEventCompat.getActionMasked(event);
            switch (action) {
                case ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (parentScroll != null) {
                        parentScroll.requestDisallowInterceptTouchEvent(false);
                    }
                    moving = false;
                    break;
                case MotionEvent.ACTION_DOWN:
                    final float evY = event.getY();
                    if (evY <= barY || evY >= (barY + barWidth)) {
                        return true;
                    } else {
                        moving = true;
                    }
                    if (parentScroll != null) {
                        parentScroll.requestDisallowInterceptTouchEvent(true);
                    }
                case MotionEvent.ACTION_MOVE: {
                    if (moving) {
                        float evX = event.getX();

                        evX = evX - settingsERS.getPaddingCorners();
                        if (evX < 0) {
                            evX = 0;
                        }
                        if (evX > barWidth) {
                            evX = barWidth;
                        }
                        this.indicatorX = evX;

                        update();
                    }
                }
                break;
            }
        }

        return true;
    }

    public void update() {
        if (barWidth > 0f) {
            float currentPercent = indicatorX / barWidth;
            currentValue = currentPercent * (max - min) + min;
            currentValue = Math.round(currentValue);

            if (listenerSeekBar != null && oldValue != currentValue) {
                oldValue = currentValue;
                listenerSeekBar.valueChanged(RangeSeekBar.this, currentValue);
            } else {

            }

            updateBubbleWidth();
            editBubbleEditPosition();
        }
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateValues();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        updateValues();
        super.onMeasure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(calculatedHieght, MeasureSpec.EXACTLY));
    }

    private void updateBubbleWidth() {
        this.bubble.width = calculateBubbleTextWidth() + dpToPx(BUBBLE_PADDING_HORIZONTAL) * 2f;
        this.bubble.width = Math.max(150, this.bubble.width);
    }

    private boolean isRegions() {
        return true;
    }

    private void updateValues() {

        if (currentValue < min) {
            currentValue = min;
        }

        settingsERS.setPaddingCorners(settingsERS.getBarHeight());

        barWidth = getWidth() - this.settingsERS.getPaddingCorners() * 2;

        if (settingsERS.isDrawBubble()) {
            updateBubbleWidth();
            this.bubble.height = dpToPx(settingsERS.getTextSizeBubbleCurrent()) + dpToPx(BUBBLE_PADDING_VERTICAL) * 2f + dpToPx(BUBBLE_ARROW_HEIGHT);
        } else {
            this.bubble.height = 0;
        }

        this.barY = 0;
        if (settingsERS.isDrawTextOnTop()) {
            barY += DISTANCE_TEXT_BAR * 2;
            if (isRegions()) {
                float topTextHeight = 0;
                final String tmpTextLeft = formatRegionValue(0, 0);
                final String tmpTextRight = formatRegionValue(1, 0);
                topTextHeight = Math.max(topTextHeight, calculateTextMultilineHeight(tmpTextLeft, settingsERS.getPaintTextTop()));
                topTextHeight = Math.max(topTextHeight, calculateTextMultilineHeight(tmpTextRight, settingsERS.getPaintTextTop()));

                this.barY += topTextHeight + 3;
            } else {
                float topTextHeight = 0;

                this.barY += topTextHeight;
            }
        } else {
            if (settingsERS.isDrawBubble()) {
                this.barY -= dpToPx(BUBBLE_ARROW_HEIGHT) / 1.5f;
            }
        }

        this.barY += bubble.height;

        this.barCenterY = barY + settingsERS.getBarHeight() / 2f;

        if (settingsERS.isIndicatorInside()) {
            this.indicatorRadius = (int) (settingsERS.getBarHeight() * .5f);
        } else {
            this.indicatorRadius = (int) (settingsERS.getBarHeight() * .9f);
        }

        indicatorX = (currentValue - min) / (max - min) * barWidth;

        calculatedHieght = (int) (barCenterY + indicatorRadius);

        float bottomTextHeight = 0;
        if (!TextUtils.isEmpty(textMax)) {
            bottomTextHeight = Math.max(
                    calculateTextMultilineHeight(textMax, settingsERS.getPaintTextBottom()),
                    calculateTextMultilineHeight(textMin, settingsERS.getPaintTextBottom())
            );
        }

        calculatedHieght += bottomTextHeight;

        calculatedHieght += 10; //padding bottom
    }

    public void setTextMax(String textMax) {
        this.textMax = textMax;
        postInvalidate();
    }

    public void setTextMin(String textMin) {
        this.textMin = textMin;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        final float paddingLeft = settingsERS.getPaddingCorners();
        final float paddingRight = settingsERS.getPaddingCorners();


        if (isRegions()) {
            settingsERS.getPaintIndicator().setColor(settingsERS.getRegionColorLeft());
            settingsERS.getPaintBubble().setColor(settingsERS.getRegionColorLeft());
        }

        final float radiusCorner = settingsERS.getBarHeight() / 2f;

        final float indicatorCenterX = indicatorX + paddingLeft;

        final float centerCircleLeft = paddingLeft;
        final float centerCircleRight = getWidth() - paddingRight;

        settingsERS.getPaintBar().setColor(settingsERS.getColorBackground());

        canvas.drawCircle(centerCircleLeft, barCenterY, radiusCorner, settingsERS.getPaintBar());
        canvas.drawCircle(centerCircleRight, barCenterY, radiusCorner, settingsERS.getPaintBar());
        canvas.drawRect(centerCircleLeft, barY, centerCircleRight, barY + settingsERS.getBarHeight(), settingsERS.getPaintBar());

        if (isRegions()) {
            settingsERS.getPaintBar().setColor(settingsERS.getRegionColorLeft());

            canvas.drawCircle(centerCircleLeft, barCenterY, radiusCorner, settingsERS.getPaintBar());
            canvas.drawRect(centerCircleLeft, barY, indicatorCenterX, barY + settingsERS.getBarHeight(), settingsERS.getPaintBar());
        }

        if (settingsERS.isDrawTextOnTop()) {
            final float textY = barY - dpToPx(DISTANCE_TEXT_BAR);
            if (isRegions()) {
                float leftValue;
                float rightValue;

                if (settingsERS.isRegions_centerText()) {
                    leftValue = currentValue;
                    rightValue = max - leftValue;
                } else {
                    leftValue = min;
                    rightValue = max;
                }

                if (settingsERS.isRegions_textFollowRegionColor()) {
                    settingsERS.getPaintTextTop().setColor(settingsERS.getRegionColorLeft());
                }

                float textX;
                if (settingsERS.isRegions_centerText()) {
                    textX = (indicatorCenterX - paddingLeft) / 2f + paddingLeft;
                } else {
                    textX = paddingLeft;
                }

                drawIndicatorsTextAbove(canvas, formatRegionValue(0, leftValue), settingsERS.getPaintTextTop(), textX, textY, Layout.Alignment.ALIGN_CENTER);

                if (settingsERS.isRegions_textFollowRegionColor()) {
                    settingsERS.getPaintTextTop().setColor(settingsERS.getRegionColorRight());
                }

                if (settingsERS.isRegions_centerText()) {
                    textX = indicatorCenterX + (barWidth - indicatorCenterX - paddingLeft) / 2f + paddingLeft;
                } else {
                    textX = paddingLeft + barWidth;
                }
                drawIndicatorsTextAbove(canvas, formatRegionValue(1, rightValue), settingsERS.getPaintTextTop(), textX, textY, Layout.Alignment.ALIGN_CENTER);
            } else {
                drawIndicatorsTextAbove(canvas, formatValue(min), settingsERS.getPaintTextTop(), 0 + paddingLeft, textY, Layout.Alignment.ALIGN_CENTER);
                drawIndicatorsTextAbove(canvas, formatValue(max), settingsERS.getPaintTextTop(), canvas.getWidth(), textY, Layout.Alignment.ALIGN_CENTER);
            }
        }

        final float bottomTextY = barY + settingsERS.getBarHeight() + 15;

        if (settingsERS.isDrawTextOnBottom()) {
            if (!TextUtils.isEmpty(textMax)) {
                drawMultilineText(canvas, textMax, canvas.getWidth(), bottomTextY, settingsERS.getPaintTextBottom(), Layout.Alignment.ALIGN_CENTER);
            }

            if (!TextUtils.isEmpty(textMin)) {
                drawMultilineText(canvas, textMin, 0, bottomTextY, settingsERS.getPaintTextBottom(), Layout.Alignment.ALIGN_CENTER);
            }
        }

        final int color = settingsERS.getPaintIndicator().getColor();
        canvas.drawCircle(indicatorCenterX, this.barCenterY, indicatorRadius, settingsERS.getPaintIndicator());
        settingsERS.getPaintIndicator().setColor(Color.WHITE);
        canvas.drawCircle(indicatorCenterX, this.barCenterY, indicatorRadius * 0.85f, settingsERS.getPaintIndicator());
        settingsERS.getPaintIndicator().setColor(color);

        if (settingsERS.isDrawBubble()) {
            float bubbleCenterX = indicatorCenterX;
            float trangleCenterX;

            bubble.x = bubbleCenterX - bubble.width / 2f;
            bubble.y = 0;

            if (bubbleCenterX > canvas.getWidth() - bubble.width / 2f) {
                bubbleCenterX = canvas.getWidth() - bubble.width / 2f;
            } else if (bubbleCenterX - bubble.width / 2f < 0) {
                bubbleCenterX = bubble.width / 2f;
            }

            trangleCenterX = (bubbleCenterX + indicatorCenterX) / 2f;

            drawBubble(canvas, bubbleCenterX, trangleCenterX, 0);
        }

        canvas.restore();
    }

    private String formatValue(float value) {
        return textFormatterSeekBar.format(value);
    }

    private String formatRegionValue(int region, float value) {
        if (regionTextFormatter != null) {
            return regionTextFormatter.format(region, value);
        } else {
            return formatValue(value);
        }
    }

    private void drawText(Canvas canvas, String text, float x, float y, TextPaint paint, Layout.Alignment aligment) {
        canvas.save();
        canvas.translate(x, y);
        final StaticLayout staticLayout = new StaticLayout(text, paint, (int) paint.measureText(text), aligment, 1.0f, 0, false);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    private void drawMultilineText(Canvas canvas, String text, float x, float y, TextPaint paint, Layout.Alignment aligment) {
        final float lineHeight = paint.getTextSize();
        float lineY = y;
        for (CharSequence line : text.split("\n")) {
            canvas.save();
            final float lineWidth = (int) paint.measureText(line.toString());
            float lineX = x;
            if (aligment == Layout.Alignment.ALIGN_CENTER) {
                lineX -= lineWidth / 2f;
            }
            if (lineX < 0) {
                lineX = 0;
            }

            final float right = lineX + lineWidth;
            if (right > canvas.getWidth()) {
                lineX = canvas.getWidth() - lineWidth - settingsERS.getPaddingCorners();
            }

            canvas.translate(lineX, lineY);
            final StaticLayout staticLayout = new StaticLayout(line, paint, (int) lineWidth, aligment, 1.0f, 0, false);
            staticLayout.draw(canvas);

            lineY += lineHeight;
            canvas.restore();
        }

    }

    private void drawIndicatorsTextAbove(Canvas canvas, String text, TextPaint paintText, float x, float y, Layout.Alignment alignment) {

        final float textHeight = calculateTextMultilineHeight(text, paintText);
        y -= textHeight;

        final int width = (int) paintText.measureText(text);
        if (x >= getWidth() - settingsERS.getPaddingCorners()) {
            x = (getWidth() - width - settingsERS.getPaddingCorners() / 2f);
        } else if (x <= 0) {
            x = width / 2f;
        } else {
            x = (x - width / 2f);
        }

        if (x < 0) {
            x = 0;
        }

        if (x + width > getWidth()) {
            x = getWidth() - width;
        }

        drawText(canvas, text, x, y, paintText, alignment);
    }

    private float calculateTextMultilineHeight(String text, TextPaint textPaint) {
        return text.split("\n").length * textPaint.getTextSize();
    }

    private float calculateBubbleTextWidth() {
        String bubbleText = formatValue(getCurrentValue());
        if (isEditing) {
            bubbleText = textEditing;
        }
        return settingsERS.getPaintBubbleTextCurrent().measureText(bubbleText);
    }

    private void drawBubblePath(Canvas canvas, float triangleCenterX, float height, float width) {
        final Path path = new Path();

        int padding = 3;
        final Rect rect = new Rect(padding, padding, (int) width - padding, (int) (height - dpToPx(BUBBLE_ARROW_HEIGHT)) - padding);

        final float roundRectHeight = dpToPx(10);

        path.moveTo(rect.left + roundRectHeight, rect.top);
        path.lineTo(rect.right - roundRectHeight, rect.top);
        path.quadTo(rect.right, rect.top, rect.right, rect.top + roundRectHeight);
        path.lineTo(rect.right, rect.bottom - roundRectHeight);
        path.quadTo(rect.right, rect.bottom, rect.right - roundRectHeight, rect.bottom);

        path.lineTo(triangleCenterX + dpToPx(BUBBLE_ARROW_WIDTH) / 2f, height - dpToPx(BUBBLE_ARROW_HEIGHT) - padding);
        path.lineTo(triangleCenterX, height - padding);
        path.lineTo(triangleCenterX - dpToPx(BUBBLE_ARROW_WIDTH) / 2f, height - dpToPx(BUBBLE_ARROW_HEIGHT) - padding);

        path.lineTo(rect.left + roundRectHeight, rect.bottom);
        path.quadTo(rect.left, rect.bottom, rect.left, rect.bottom - roundRectHeight);
        path.lineTo(rect.left, rect.top + roundRectHeight);
        path.quadTo(rect.left, rect.top, rect.left + roundRectHeight, rect.top);
        path.close();

        canvas.drawPath(path, settingsERS.getPaintBubble());
    }

    private void drawBubble(Canvas canvas, float centerX, float triangleCenterX, float y) {
        final float width = this.bubble.width;
        final float height = this.bubble.height;

        canvas.save();

        canvas.translate(centerX - width / 2f, y);
        triangleCenterX -= (centerX - width / 2f);

        if (!isEditing) {
            drawBubblePath(canvas, triangleCenterX, height, width);
        } else {
            final int savedColor = settingsERS.getPaintBubble().getColor();

            settingsERS.getPaintBubble().setColor(settingsERS.getBubbleColorEditing());
            settingsERS.getPaintBubble().setStyle(Paint.Style.FILL);
            drawBubblePath(canvas, triangleCenterX, height, width);

            settingsERS.getPaintBubble().setStyle(Paint.Style.STROKE);
            settingsERS.getPaintBubble().setColor(settingsERS.getPaintIndicator().getColor());
            drawBubblePath(canvas, triangleCenterX, height, width);

            settingsERS.getPaintBubble().setStyle(Paint.Style.FILL);
            settingsERS.getPaintBubble().setColor(savedColor);
        }

        if (!isEditing) {
            final String bubbleText = formatValue(getCurrentValue());
            drawText(canvas, bubbleText, dpToPx(BUBBLE_PADDING_HORIZONTAL), dpToPx(BUBBLE_PADDING_VERTICAL) - 3, settingsERS.getPaintBubbleTextCurrent(), Layout.Alignment.ALIGN_NORMAL);
        }

        canvas.restore();

    }

    public void setTextFormatter(TextFormatterSeekBar textFormatterSeekBar) {
        this.textFormatterSeekBar = textFormatterSeekBar;
        update();
    }

    public void setRegionTextFormatter(RegionTextFormatter regionTextFormatter) {
        this.regionTextFormatter = regionTextFormatter;
        update();
    }


    public interface RegionTextFormatter {
        String format(int region, float value);
    }

    private class Bubble {
        private float height;
        private float width;
        private float x;
        private float y;

        public boolean clicked(MotionEvent e) {
            return e.getX() >= x && e.getX() <= x + width
                    && e.getY() >= y && e.getY() < y + height;
        }

        public float getHeight() {
            return height - dpToPx(BUBBLE_ARROW_HEIGHT);
        }

        public float getX() {
            return Math.max(x, 0);
        }

        public float getY() {
            return Math.max(y, 0);
        }
    }
}
