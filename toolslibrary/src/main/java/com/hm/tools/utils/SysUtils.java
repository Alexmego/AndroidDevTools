package com.hm.tools.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class SysUtils {
    private static final int ANDROID_LOW_MEMORY_DEVICE_THRESHOLD_MB = 512;
    private static final int KBS_IN_MB = 1024;
    private static final String TAG = "SysUtils";
    private static Boolean sLowEndDevice;

    private SysUtils() {
    }

    private static int amountOfPhysicalMemoryMB() {
        Pattern pattern = Pattern.compile("^MemTotal:\\s+([0-9]+) kB$");
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskReads();

        try {

            try (FileReader e = new FileReader("/proc/meminfo")) {
                try (BufferedReader reader = new BufferedReader(e)) {
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            Log.w("SysUtils", "/proc/meminfo lacks a MemTotal entry?");
                            break;
                        }

                        Matcher m = pattern.matcher(line);
                        if (m.find()) {
                            int totalMemoryKB = Integer.parseInt(m.group(1));
                            if (totalMemoryKB > 1024) {
                                int var7 = totalMemoryKB / 1024;
                                return var7;
                            }

                            Log.w("SysUtils", "Invalid /proc/meminfo total size in kB: " + m.group(1));
                            break;
                        }
                    }
                }
            }
        } catch (Exception var25) {
            Log.w("SysUtils", "Cannot get total physical size from /proc/meminfo", var25);
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }

        return 0;
    }


    public static boolean isLowEndDevice() {
        return false;
    }

    public static String getProcessName(Context context, int pid) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps != null) {
                for (ActivityManager.RunningAppProcessInfo info : runningApps) {
                    if (info.pid == pid) {
                        return info.processName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static int getCurrentAppUid(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
            return packageInfo.applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
