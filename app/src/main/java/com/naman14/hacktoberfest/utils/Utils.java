package com.naman14.hacktoberfest.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;

import androidx.browser.customtabs.CustomTabsIntent;

import android.text.TextUtils;
import android.util.Property;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.naman14.hacktoberfest.MainActivity;
import com.naman14.hacktoberfest.R;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by naman on 4/10/17.
 */

public class Utils {

    private static final String HACKTOBERFEST_START = "%d-09-30T00:00:00-12:00..%d-10-31T23:59:59-12:00";
    private static final String PREFERENCE_LANGUAGE = "preference_language";
    private static final String PREFERENCE_TAGS = "preference_tag";

    private static final int TEXTVIEW_TAGS_MAX_LENGTH = 30;

    @SuppressLint("DefaultLocale")
    public static String getHacktoberfestStatusQuery(String username) {
        return "-label:invalid+created:"
                + String.format(HACKTOBERFEST_START, yearForHacktoberfest(), yearForHacktoberfest())
                + "+type:pr+is:public+author:" + username;
    }

    @SuppressLint("DefaultLocale")
    public static String getHacktoberfestIssuesQuery(String language, String[] tags) {
        String extraQuery = "";
        String extraQueryTags = "";

        if (!TextUtils.isEmpty(language) && !language.equals("All")) {
            extraQuery += "+language:" + language;
        }

        if (tags.length != 0) {
            extraQueryTags += Utils.tagsQueryBuilder(tags);
        }

        return "+label:hacktoberfest" + extraQueryTags +
                "+updated:" + String.format(HACKTOBERFEST_START, yearForHacktoberfest(), yearForHacktoberfest())
                + "+type:issue+state:open" + extraQuery;
    }


    public static String[] getLanguagesArray() {
        return new String[]{"All", "JavaScript", "Dart", "Python", "PHP", "Java", "Kotlin", "Go", "C++", "C", "HTML", "Ruby", "Rust", "CSS"};
    }

    public static String[] getTagsArray() {
        String[] tagsArray = new String[]{
                "help wanted",
                "easy",
                "intermediate",
                "hard",
                "enhancement",
                "good first issue",
                "documentation",
                "good first patch",
                "beginner",
                "bug",
                "design",
                "ui"
        };

        Arrays.sort(tagsArray);
        return tagsArray;
    }

    public static String getStatusMessage(int prCount) {
//        return "okay";
        switch (prCount) {
            case 0:
//                return "It's not too late to start!";
                return MainActivity.getContext().getString(R.string.n_0_pr);
            case 1:
//                return "Off to a great start, keep going!";
                return MainActivity.getContext().getString(R.string.n_1_pr);
            case 2:
//                return "Half way there, keep it up!";
                return MainActivity.getContext().getString(R.string.n_2_pr);
            case 3:
//                return "So close!";
                return MainActivity.getContext().getString(R.string.n_3_pr);
            case 4:
//                return "Way to go!";
                return MainActivity.getContext().getString(R.string.n_4_pr);
            //we don't need case 5, default is the same
//            case 5:
////                return "Now you're just showing off!";
//                return MainActivity.getContext().getString(R.string.n_5_pr);
            default:
//                return "Now you're just showing off!";
                return MainActivity.getContext().getString(R.string.n_5_pr);
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


    public static String getLanguagePreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREFERENCE_LANGUAGE, "All");
    }

    public static void setLanguagePreference(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREFERENCE_LANGUAGE, language).apply();
    }

    public static String[] getTagsPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String tagsJson = preferences.getString(PREFERENCE_TAGS, null);
        if (tagsJson == null) {
            return new String[]{};
        } else {
            return new Gson().fromJson(tagsJson, String[].class);
        }
    }

    public static String tagsQueryBuilder(String[] stringArray) {
        if (stringArray.length == 0) {
            return null;
        } else {
            StringBuilder tagsText = new StringBuilder();

            for (String tag : stringArray) {
                tagsText.append("+label:\"").append(tag).append("\"");
            }
            return tagsText.toString();
        }
    }

    public static String getTagsPreferenceString(Context context) {
        String[] tagsArray = Utils.getTagsPreference(context);

        if (tagsArray.length == 0) {
            return "All";
        } else {
            StringBuilder tagsText = new StringBuilder();

            for (String tag : tagsArray) {
                if (tagsText.length() >= TEXTVIEW_TAGS_MAX_LENGTH) {
                    break;
                }

                tagsText.append(tag).append(", ");
            }

            tagsText.delete(tagsText.length() - 2, tagsText.length());

            if (tagsText.length() > TEXTVIEW_TAGS_MAX_LENGTH) {
                tagsText.append("...");
            }

            return tagsText.toString();
        }
    }

    public static void setTagsPreference(Context context, String[] tags) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREFERENCE_TAGS, new Gson().toJson(tags)).apply();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void openChromeCustomTab(Context context, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(context.getResources().getColor(R.color.hacktoberfest_background));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    private static int yearForHacktoberfest() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int yearForHacktoberfest;
        if (currentMonth >= Calendar.OCTOBER) {
            yearForHacktoberfest = currentYear;
        } else yearForHacktoberfest = currentYear - 1;
        return yearForHacktoberfest;
    }
}
