package com.example.animeepisodes;

import android.app.Activity;

public class GlobalFormats {
    public static void reloadActivity(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0, 0);

    }
}
