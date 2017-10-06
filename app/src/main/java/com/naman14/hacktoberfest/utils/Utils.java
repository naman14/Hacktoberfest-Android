package com.naman14.hacktoberfest.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by naman on 4/10/17.
 */

public class Utils {

    private static final String HACKTOBERFEST_START = "2017-09-30T00:00:00-12:00..2017-10-31T23:59:59-12:00";

    public static String getHacktoberfestStatusQuery(String username) {
        return "-label:invalid+created:" + HACKTOBERFEST_START + "+type:pr+is:public+author:" + username;
    }

    public static String getHacktoberfestIssuesQuery(String language) {
        String extraQuery = "";

        if (!TextUtils.isEmpty(language)) {
            extraQuery += "+language:" + language;
        }
        return "+label:hacktoberfest+updated:" + HACKTOBERFEST_START + "+type:issue+state:open" + extraQuery;
    }


    public static List<String> getLanguages() {

        String[] languages = new String[] {"JavaScript", "Python", "PHP", "Java", "Go", "C++", "C", "HTML", "Ruby", "Rust", "CSS"};

        return Arrays.asList(languages);
    }

    public static String getStatusMessage(int prCount){

        switch (prCount) {
            case 0:
                return "It's not too late to start!";
            case 1:
                return "Off to a great start, keep going!";
            case 2:
                return "Half way there, keep it up!";
            case 3:
                return "So close!";
            case 4:
                return "Way to go!";
            case 5:
                return "Now you're just showing off!";
            default:
                return "Now you're just showing off!";
        }
    }

    public static int getContrastColor(int colorIntValue) {
        int red = Color.red(colorIntValue);
        int green = Color.green(colorIntValue);
        int blue = Color.blue(colorIntValue);
        double lum = (((0.299 * red) + ((0.587 * green) + (0.114 * blue))));
        return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;
    }

    public static final Property<View, Integer> BACKGROUND_COLOR
            = new AnimUtils.IntProperty<View>("backgroundColor") {

        @Override
        public void setValue(View view, int value) {
            view.setBackgroundColor(value);
        }

        @Override
        public Integer get(View view) {
            Drawable d = view.getBackground();
            if (d instanceof ColorDrawable) {
                return ((ColorDrawable) d).getColor();
            }
            return Color.TRANSPARENT;
        }
    };



}
