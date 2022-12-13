package com.example.zakatgoldapp;

import android.app.Activity;
import android.content.Intent;

public class Utils {
    private static int sTheme;

    public final static int THEME_STANDARD = 0;
    public final static int THEME_GOLD_MODE = 1;
    public final static int THEME_WHITE_MODE = 2;

    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case THEME_STANDARD:
                activity.setTheme(R.style.Theme_Standard);
                break;
            case THEME_GOLD_MODE:
                activity.setTheme(R.style.Theme_GoldMode);
                break;
            case THEME_WHITE_MODE:
                activity.setTheme(R.style.Theme_WhiteMode);
                break;

        }
    }
}