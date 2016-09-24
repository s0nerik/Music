package com.github.s0nerik.music.ui.views;

import android.support.v4.view.ViewPager;
import android.view.View;

public class PlaybackSongsPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MAX_SCALE = 1f;

    private static final float MAX_ELEVATION = 8f;
    private static final float MIN_ELEVATION = 4f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        float posOffset = Math.abs(position);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
            view.setElevation(MIN_ELEVATION);
        } else if (position <= 1) { // [-1,1]
            float scaleFactor = (1 - posOffset) * (MAX_SCALE - MIN_SCALE) + MIN_SCALE;
            float elevation = Math.max(MIN_ELEVATION, MAX_ELEVATION - posOffset * (MAX_ELEVATION - MIN_ELEVATION));

            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setElevation(elevation);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
            view.setElevation(MIN_ELEVATION);
        }
    }
}