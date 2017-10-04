package com.naman14.hacktoberfest;

import android.text.TextUtils;

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

}
