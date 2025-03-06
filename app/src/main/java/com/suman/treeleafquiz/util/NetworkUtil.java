package com.suman.treeleafquiz.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class NetworkUtil {

    private static NetworkInfo activeNetwork;

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        if (context != null){

            activeNetwork = getNetworkInfo(context);
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    public static void openSettings(Context context) {
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

}
