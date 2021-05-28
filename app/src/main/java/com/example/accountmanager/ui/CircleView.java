package com.example.accountmanager.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleView extends View {
    private Paint paint;
    private Paint solidPaint;

    private int choosen = 0;
    private int circleNum;
    private int paintColor;

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initPaint();
        int width = getWidth();
        int height = getHeight();
        int x = width / 2 - 45 - (circleNum / 2 - 1) * 90;
        for (int i = 0; i < circleNum; i++) {
            if (i < choosen) {
                canvas.drawCircle(x, height / 2f, 18, solidPaint);
            } else {
                canvas.drawCircle(x, height / 2f, 16, paint);
            }
            x += 90;
        }
    }

    private void initPaint() {
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            paint.setColor(paintColor);
        }
        if (solidPaint == null) {
            solidPaint = new Paint();
            solidPaint.setAntiAlias(true);
            solidPaint.setStyle(Paint.Style.FILL);
            solidPaint.setColor(paintColor);
        }
    }

    public void setCircleNum(int circleNum) {
        this.circleNum = circleNum;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }

    public void setChosen(int num) {
        if (num > circleNum) return;
        choosen = num;
        invalidate();
    }
}
