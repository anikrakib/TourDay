package com.anikrakib.tourday.Utils.TapToProgress;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CircleAnimation extends Animation {
    private final Circle circle;
    private final float angle;
    private final float endAngle;

    public CircleAnimation(Circle circle, float endAngle) {
        this.angle = circle.getAngle();
        this.endAngle = endAngle;
        this.circle = circle;
    }


    protected void applyTransformation(float f, Transformation transformation) {
        this.circle.setAngle(this.angle + ((this.endAngle - this.angle) * f));
        this.circle.requestLayout();
    }
}
