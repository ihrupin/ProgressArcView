package com.hrupin.samples.roundanimation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private ProgressArcView progressArcView;
    private ValueAnimator progressAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressArcView = (ProgressArcView)findViewById(R.id.progressArcView);
        progressArcView.setProgress(0);
        progressArcView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            startProgress();
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            if(progressAnim != null){
                progressAnim.end();
            }
            progressAnim = null;
            if(progressArcView != null){
                progressArcView.setProgress(0);
            }
        }
        return true;
    }

    private void startProgress() {
        if(progressAnim == null) {
            progressAnim = ValueAnimator.ofInt(0, progressArcView.getMax());
            progressAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    if(progressArcView != null){
                        progressArcView.setProgress(value);
                    }
                }
            });

            progressAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(progressArcView != null){
                        progressArcView.setProgress(0);
                    }
                    progressAnim = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            progressAnim.setDuration(10000);
            progressAnim.setInterpolator(new LinearInterpolator());
            progressAnim.start();
        }
    }
}
