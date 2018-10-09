package com.hm.tools.statusbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;

public class StatusHelper {

    private final static int STATUS_BAR_TYPE_DEFAULT = 0;
    private final static int STATUS_BAR_TYPE_MI_UI = 1;
    private final static int STATUS_BAR_TYPE_FLY_ME = 2;
    private final static int STATUS_BAR_TYPE_ANDROID6 = 3;
    private final static int STATUS_BAR_DEFAULT_HEIGHT_DP = 25;
    private static int mStatusBarHeight = -1;

    @StatusBarType
    private static int mStatusBarType = STATUS_BAR_TYPE_DEFAULT;

    public static void translucent(Activity activity) {
        translucent(activity, 0x40000000);
    }

    public static int getStatusbarHeight(Context context) {
        if (mStatusBarHeight == -1) {
            initStatusBarHeight(context);
        }
        return mStatusBarHeight;
    }

    public static boolean isFullScreen(Activity activity) {
        boolean ret = false;
        try {
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            ret = (attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 设置白色字体
     */
    public static boolean setStatusBarDarkMode(Activity activity) {
        if (mStatusBarType == STATUS_BAR_TYPE_DEFAULT) {
            return true;
        }

        if (mStatusBarType == STATUS_BAR_TYPE_MI_UI) {
            return MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (mStatusBarType == STATUS_BAR_TYPE_FLY_ME) {
            return FlySetStatusBarLightMode(activity.getWindow(), false);
        } else if (mStatusBarType == STATUS_BAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.getWindow(), false);
        }
        return true;
    }

    /**
     * 设置黑色字体
     */
    public static boolean setStatusBarLightMode(Activity activity) {
        if (DevicesHelper.isZTE2016()) {
            return false;
        }

        if (mStatusBarType != STATUS_BAR_TYPE_DEFAULT) {
            return setStatusBarLightMode(activity, mStatusBarType);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMIUICustomStatusBarLightModeImpl() && MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                mStatusBarType = STATUS_BAR_TYPE_MI_UI;
                return true;
            } else if (FlySetStatusBarLightMode(activity.getWindow(), true)) {
                mStatusBarType = STATUS_BAR_TYPE_FLY_ME;
                return true;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Android6SetStatusBarLightMode(activity.getWindow(), true);
                mStatusBarType = STATUS_BAR_TYPE_ANDROID6;
                return true;
            }
        }
        return false;
    }

    @TargetApi(19)
    private static void translucent(Activity activity, @ColorInt int colorOn5x) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (DevicesHelper.isMeiZu() || DevicesHelper.isMi()) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportTranslucentStatusBar6()) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(colorOn5x);
            }
        }
    }

    private static boolean setStatusBarLightMode(Activity activity, @StatusBarType int type) {
        if (type == STATUS_BAR_TYPE_MI_UI) {
            return MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == STATUS_BAR_TYPE_FLY_ME) {
            return FlySetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == STATUS_BAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.getWindow(), true);
        }
        return false;
    }

    @TargetApi(23)
    private static int changeStatusBarModeRetainFlag(Window window, int out) {
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        return out;
    }

    private static int retainSystemUiFlag(Window window, int out, int type) {
        int now = window.getDecorView().getSystemUiVisibility();
        if ((now & type) == type) {
            out |= type;
        }
        return out;
    }

    @TargetApi(23)
    private static boolean Android6SetStatusBarLightMode(Window window, boolean light) {
        View decorView = window.getDecorView();
        int systemUi = light ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        systemUi = changeStatusBarModeRetainFlag(window, systemUi);
        decorView.setSystemUiVisibility(systemUi);
        return true;
    }

    private static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                @SuppressLint("PrivateApi") Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception ignored) {

            }
        }
        return result;
    }

    private static boolean isMIUICustomStatusBarLightModeImpl() {
        return DevicesHelper.isMIUIV5() || DevicesHelper.isMIUIV6() ||
                DevicesHelper.isMIUIV7() || DevicesHelper.isMIUIV8();
    }

    private static boolean FlySetStatusBarLightMode(Window window, boolean dark) {
        Android6SetStatusBarLightMode(window, dark);
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field field = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                field.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = field.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                field.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception ignored) {

            }
        }
        return result;
    }


    /**
     * 检测 Android 6.0 是否可以启用 window.setStatusBarColor(Color.TRANSPARENT)。
     */
    private static boolean supportTranslucentStatusBar6() {
        return !(DevicesHelper.isZUK_Z1() || DevicesHelper.isZTE2016());
    }


    @SuppressLint("PrivateApi")
    private static void initStatusBarHeight(Context context) {
        Class<?> clazz;
        Object obj = null;
        Field field = null;
        try {
            clazz = Class.forName("com.android.internal.R$dimen");
            obj = clazz.newInstance();
            if (DevicesHelper.isMeiZu()) {
                try {
                    field = clazz.getField("status_bar_height_large");
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            if (field == null) {
                field = clazz.getField("status_bar_height");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (field != null && obj != null) {
            try {
                int id = Integer.parseInt(field.get(obj).toString());
                mStatusBarHeight = context.getResources().getDimensionPixelSize(id);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (DevicesHelper.isTablet(context)
                && mStatusBarHeight > dip2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP)) {
            mStatusBarHeight = 0;
        } else {
            if (mStatusBarHeight <= 0
                    || mStatusBarHeight > dip2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP * 2)) {
                mStatusBarHeight = dip2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP);
            }
        }
    }


    @IntDef({STATUS_BAR_TYPE_DEFAULT, STATUS_BAR_TYPE_MI_UI, STATUS_BAR_TYPE_FLY_ME, STATUS_BAR_TYPE_ANDROID6})
    @Retention(RetentionPolicy.SOURCE)
    private @interface StatusBarType {

    }

    private static int dip2px(Context context, int dip) {
        return (int) (context.getResources().getDisplayMetrics().scaledDensity * dip + 0.5f);
    }

}
