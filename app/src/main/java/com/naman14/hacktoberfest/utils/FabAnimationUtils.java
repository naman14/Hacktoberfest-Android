/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.naman14.hacktoberfest.utils;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Interpolator;

public class FabAnimationUtils {

    private static final long DEFAULT_DURATION = 300L;
    private static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();

    public static void scaleIn(final View fab) {
        scaleIn(fab, DEFAULT_DURATION, null);
    }

    private static void scaleIn(final View fab, long duration, final ScaleCallback callback) {
        fab.setVisibility(View.VISIBLE);
        ViewCompat.animate(fab)
                .scaleX(1.0F)
                .scaleY(1.0F)
                .alpha(1.0F)
                .setDuration(duration)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    public void onAnimationStart(View view) {
                        if (callback != null) callback.onAnimationStart();
                    }

                    public void onAnimationCancel(View view) {
                    }

                    public void onAnimationEnd(View view) {
                        view.setVisibility(View.VISIBLE);
                        if (callback != null) callback.onAnimationEnd();
                    }
                }).start();
    }

    public static void scaleOut(final View fab) {
        scaleOut(fab, DEFAULT_DURATION, null);
    }

    public static void scaleOut(final View fab, final ScaleCallback callback) {
        scaleOut(fab, DEFAULT_DURATION, callback);
    }

    public static void scaleOut(final View fab, long duration) {
        scaleOut(fab, duration, null);
    }

    public static void scaleOut(final View fab, long duration, final ScaleCallback callback) {
        ViewCompat.animate(fab)
                .scaleX(0.0F)
                .scaleY(0.0F).alpha(0.0F)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .setDuration(duration)
                .withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    public void onAnimationStart(View view) {
                        if (callback != null) callback.onAnimationStart();
                    }

                    public void onAnimationCancel(View view) {
                    }

                    public void onAnimationEnd(View view) {
                        view.setVisibility(View.INVISIBLE);
                        if (callback != null) callback.onAnimationEnd();
                    }
                }).start();
    }

    public interface ScaleCallback {
        public void onAnimationStart();

        public void onAnimationEnd();
    }

}
