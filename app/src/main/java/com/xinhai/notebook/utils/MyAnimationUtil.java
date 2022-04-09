package com.xinhai.notebook.utils;

import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

public class MyAnimationUtil {

    /**
     * 控件左右摇晃
     */
    public static Animation shakeAnimation(int CycleTimes) {
        Animation translateAnimation =
                new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(1500);
        return translateAnimation;
    }

}
