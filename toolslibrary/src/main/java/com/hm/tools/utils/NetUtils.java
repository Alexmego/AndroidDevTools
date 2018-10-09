package com.hm.tools.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {

    private static int WIFI = 1, CMWAP = 2, CMNET = 3, UNIWAP = 4, UNINET = 5, G3WAP = 6, G3NET = 7, CTWAP = 8,
            CTNET = 9, OTHER = 10;


    public static boolean isWiFiNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isMobileNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }


    public static boolean isNetworkConnected(Context context) {
        try {
            NetworkInfo networkinfo = getActiveNetworkInfo(context);
            if (networkinfo != null)
                return networkinfo.isAvailable() && networkinfo.isConnected();
        } catch (Exception exception) {
        }
        return false;
    }

    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivitymanager.getActiveNetworkInfo();
    }

    public static int getAPNType(Context context) {
        int netType = -1;
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo == null) {
                return netType;
            }

            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_MOBILE) {
                String extraInfoString = networkInfo.getExtraInfo().toLowerCase();
                if (extraInfoString.equals("cmnet")) {
                    netType = CMNET;
                } else if (extraInfoString.equals("cmwap")) {
                    netType = CMWAP;
                } else if (extraInfoString.equals("uniwap")) {
                    netType = UNIWAP;
                } else if (extraInfoString.equals("uninet")) {
                    netType = UNINET;
                } else if (extraInfoString.equals("3gwap")) {
                    netType = G3WAP;
                } else if (extraInfoString.equals("3gnet")) {
                    netType = G3NET;
                } else if (extraInfoString.equals("ctwap")) {
                    netType = CTWAP;
                } else if (extraInfoString.equals("ctnet")) {
                    netType = CTNET;
                } else {
                    netType = OTHER;
                }
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                netType = WIFI;
            }
        } catch (Exception ignored) {
        }

        return netType;
    }

}
