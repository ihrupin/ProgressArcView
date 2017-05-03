package com.hrupin.samples.roundanimation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Igor Khrupin www.hrupin.com on 03-05-2017.
 */
public class ProgressArcView extends View {
    private Paint finishedPaint;
    private Paint unfinishedPaint;
    private Paint innerCirclePaint;

    private RectF finishedOuterRect = new RectF();
    private RectF unfinishedOuterRect = new RectF();
    private float progress = 0;
    private int max = 1000;
    private int finishedStrokeColor = Color.parseColor("#ffffff");
    private int unfinishedStrokeColor = Color.parseColor("#4c008cfd");
    private int startingDegree = -90;
    private float strokeWidth = dp2px(getResources(), 14.5f);
    private int innerBackgroundColor = Color.parseColor("#008cfd");

    private int minSize = (int) dp2px(getResources(), 270);


    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_STARTING_DEGREE = "starting_degree";

    public ProgressArcView(Context context) {
        this(context, null);
    }

    public ProgressArcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPainters();
    }

    protected void initPainters() {
        finishedPaint = new Paint();
        finishedPaint.setAntiAlias(true);
        finishedPaint.setColor(finishedStrokeColor);
        finishedPaint.setStyle(Paint.Style.STROKE);
        finishedPaint.setStrokeWidth(strokeWidth);

        unfinishedPaint = new Paint();
        unfinishedPaint.setAntiAlias(true);
        unfinishedPaint.setColor(unfinishedStrokeColor);
        unfinishedPaint.setStyle(Paint.Style.STROKE);
        unfinishedPaint.setStrokeWidth(strokeWidth);

        innerCirclePaint = new Paint();
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setColor(innerBackgroundColor);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    private float getProgressAngle() {
        return getProgress() / (float) max * 360f;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public int getStartingDegree() {
        return startingDegree;
    }

    public void setStartingDegree(int startingDegree) {
        this.startingDegree = startingDegree;
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = minSize;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float delta = strokeWidth / 2f;
        finishedOuterRect.set(delta, delta, getWidth() - delta, getHeight() - delta);

        unfinishedOuterRect.set(delta, delta, getWidth() - delta, getHeight() - delta);

        float innerCircleRadius = getWidth() / 2f - strokeWidth;
        canvas.drawArc(unfinishedOuterRect, getStartingDegree() + getProgressAngle(), 360 - getProgressAngle(), false, unfinishedPaint);
        canvas.drawArc(finishedOuterRect, getStartingDegree(), getProgressAngle(), false, finishedPaint);
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, innerCirclePaint);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_STARTING_DEGREE, getStartingDegree());
        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            initPainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setStartingDegree(bundle.getInt(INSTANCE_STARTING_DEGREE));
            setProgress(bundle.getFloat(INSTANCE_PROGRESS));
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }
}