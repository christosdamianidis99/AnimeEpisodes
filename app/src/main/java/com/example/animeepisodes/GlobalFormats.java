package com.example.animeepisodes;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;

import java.util.ArrayList;

public class GlobalFormats {
    public static void reloadActivity(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0, 0);

    }
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void setUpListView(animeListViewAdapter adapter, Context context, ArrayList<Anime> animeList, Activity activity, ListView animeListView)
    {
        adapter = new animeListViewAdapter(context, animeList, activity);
        animeListView.setAdapter(adapter);
        animeListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        animeListView.setDividerHeight(16);

        if (!(MyAnimeList_activity.MODE == 1))
        {
            Animation cardAnimation = AnimationUtils.loadAnimation(context, R.anim.bounce_animation);
            LayoutAnimationController controller = new LayoutAnimationController(cardAnimation);
            controller.setDelay(0.2f);
            animeListView.setLayoutAnimation(controller);
            animeListView.scheduleLayoutAnimation();
        }

    }
}
