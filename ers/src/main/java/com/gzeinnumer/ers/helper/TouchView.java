package com.gzeinnumer.ers.helper;


import static android.view.MotionEvent.ACTION_UP;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchView extends FrameLayout {

    private final Rect viewRect;
    private CallbackTouchViewSeekBar callbackTouchViewSeekBar;

    public TouchView(Context context, Rect viewRect) {
        super(context);
        this.viewRect = viewRect;
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    public void setCallback(CallbackTouchViewSeekBar callbackTouchViewSeekBar) {
        this.callbackTouchViewSeekBar = callbackTouchViewSeekBar;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (x >= viewRect.left
                && x <= viewRect.right
                && y >= viewRect.top
                && y <= viewRect.bottom) {
            return false;
        } else if (event.getAction() == ACTION_UP) {
            if (callbackTouchViewSeekBar != null) {
                callbackTouchViewSeekBar.onClicked();
            }
        }
        return true;
    }

    public interface CallbackTouchViewSeekBar {
        void onClicked();
    }
}