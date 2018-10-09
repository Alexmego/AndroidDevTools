package com.hm.tools.statusbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;

public class DevicesHelper {
    private final static String TAG = "DevicesHelper";
    private final static String KEY_MI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_FLY_VERSION_NAME = "ro.build.display.id";
    private final static String FLY = "flyme";
    private final static String ZTE_2016 = "zte c2016";
    private final static String ZUK_Z1 = "zuk z1";
    private final static String MEI_ZU_BOARD[] = {"m9", "M9", "mx", "MX"};
    private static String mMiVersionName;
    private static String mFlyVersionName;
    private static boolean mTabChecked = false;
    private static boolean mTabValue = false;

    static {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            Properties properties = new Properties();
            properties.load(fileInputStream);
            @SuppressLint("PrivateApi")
            Class<?> clzSystemProperties = Class.forName("android.os.SystemProperties");
            Method getMethod = clzSystemProperties.getDeclaredMethod("get", String.class);
            mMiVersionName = getLowerCaseName(properties, getMethod, KEY_MI_VERSION_NAME);
            mFlyVersionName = getLowerCaseName(properties, getMethod, KEY_FLY_VERSION_NAME);

        } catch (Exception ignored) {

        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean _isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    public static boolean isTablet(Context context) {
        if (mTabChecked) {
            return mTabValue;
        }
        mTabValue = _isTablet(context);
        mTabChecked = true;
        return mTabValue;
    }


    public static boolean isFly() {
        return !TextUtils.isEmpty(mFlyVersionName) && mFlyVersionName.contains(FLY);
    }

    public static boolean isMi() {
        return !TextUtils.isEmpty(mMiVersionName);
    }

    public static boolean isMIUIV5() {
        return "v5".equals(mMiVersionName);
    }

    public static boolean isMIUIV6() {
        return "v6".equals(mMiVersionName);
    }

    public static boolean isMIUIV7() {
        return "v7".equals(mMiVersionName);
    }

    public static boolean isMIUIV8() {
        return "v8".equals(mMiVersionName);
    }

    public static boolean isMIUIV9() {
        return "v9".equals(mMiVersionName);
    }

    public static boolean isFlymeVersionHigher5_2_4() {
        boolean isHigher = true;
        if (mFlyVersionName != null && !mFlyVersionName.equals("")) {
            Pattern pattern = Pattern.compile("(\\d+\\.){2}\\d");
            Matcher matcher = pattern.matcher(mFlyVersionName);
            if (matcher.find()) {
                String versionString = matcher.group();
                if (versionString != null && !versionString.equals("")) {
                    String[] version = versionString.split("\\.");
                    if (version.length == 3) {
                        if (Integer.valueOf(version[0]) < 5) {
                            isHigher = false;
                        } else if (Integer.valueOf(version[0]) > 5) {
                            isHigher = true;
                        } else {
                            if (Integer.valueOf(version[1]) < 2) {
                                isHigher = false;
                            } else if (Integer.valueOf(version[1]) > 2) {
                                isHigher = true;
                            } else {
                                if (Integer.valueOf(version[2]) < 4) {
                                    isHigher = false;
                                } else if (Integer.valueOf(version[2]) >= 5) {
                                    isHigher = true;
                                }
                            }
                        }
                    }

                }
            }
        }
        return isMeiZu() && isHigher;
    }

    public static boolean isMeiZu() {
        return isPhone(MEI_ZU_BOARD) || isFly();
    }

    public static boolean isXiaomi() {
        return Build.BRAND.toLowerCase().contains("xiaomi");
    }


    public static boolean isZUK_Z1() {
        final String board = Build.MODEL;
        return board != null && board.toLowerCase().contains(ZUK_Z1);
    }

    public static boolean isZTE2016() {
        final String board = Build.MODEL;
        return board != null && board.toLowerCase().contains(ZTE_2016);
    }

    private static boolean isPhone(String[] boards) {
        final String board = Build.BOARD;
        if (board == null) {
            return false;
        }
        for (String board1 : boards) {
            if (board.equals(board1)) {
                return true;
            }
        }
        return false;
    }


    @Nullable
    private static String getLowerCaseName(Properties p, Method get, String key) {
        String name = p.getProperty(key);
        if (name == null) {
            try {
                name = (String) get.invoke(null, key);
            } catch (Exception ignored) {
            }
        }
        if (name != null) name = name.toLowerCase();
        return name;
    }
}
