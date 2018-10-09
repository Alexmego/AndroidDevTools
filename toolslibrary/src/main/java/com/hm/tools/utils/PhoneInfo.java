package com.hm.tools.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;


import java.lang.reflect.Method;
import java.util.Locale;


public class PhoneInfo {
    private static final String logTag = "PhoneInfo:";
    private int sdkInt;
    private String sdkRelease;
    // private String sdkModel;

    private String language;
    private String IMEI;
    private String IMSI;
    private String MSISDN; // MSISDN for a GSM phone
    private String SIMSN; // SIM卡序列号
    private String networkOperator;

    private String MAC; // mac地址
    //private String resolution; // 分辨率

    private String oem;
    private String product;
    private String model;
    private String buildnumber;
    private String brand;
    private String fingerprint;

    private String serialNum;

    private static PhoneInfo instance = null;

    public static PhoneInfo getInstance(Context context) {
        if (instance == null) {
            instance = new PhoneInfo(context);
        }
        return instance;
    }

    @SuppressLint("MissingPermission")
    private PhoneInfo(Context context) {
        try {
            sdkInt = android.os.Build.VERSION.SDK_INT;
            sdkRelease = android.os.Build.VERSION.RELEASE;

            brand = android.os.Build.BRAND;
            language = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = tm.getDeviceId();
            if (IMEI == null) {
                IMEI = "";
            }

            IMSI = tm.getSubscriberId();
            if (IMSI == null) {
                IMSI = "";
            }

            MSISDN = tm.getLine1Number();
            SIMSN = tm.getSimSerialNumber();
            networkOperator = tm.getNetworkOperator();
            setDeviceVersionInfo();
            MAC = getMacAddress(context);
        } catch (Exception ignored) {
        }
    }

    public String getLanguage() {
        return language;
    }

    public int getSdkInt() {
        return sdkInt;
    }

    public String getSdkRelease() {
        return sdkRelease;
    }

    public String getIMEI() {
        return IMEI;
    }

    public String getIMSI() {
        return IMSI;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public String getSIMSN() {
        return SIMSN;
    }

    public String getNetwrokOperator() {
        return networkOperator;
    }

    public String getMacAddress() {
        return MAC;
    }

    public String getOem() {
        return this.oem;
    }

    public String getProduct() {
        return this.product;
    }

    public String getModel() {
        return model;
    }

    public String getBuildnumber() {
        return this.buildnumber;
    }

    public String getSerialNum() {
        return this.serialNum;
    }

    public String getFingerPrint() {
        return this.fingerprint;
    }

    private void setDeviceVersionInfo() {
        try {
            Object object = new Object();
            Method get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);

            // String serialNum =(String) get.invoke(object, "ro.serialno");

            oem = (String) get.invoke(object, "ro.product.manufacturer");
            product = (String) get.invoke(object, "ro.product.device");
            model = (String) get.invoke(object, "ro.product.model");
            buildnumber = (String) get.invoke(object, "ro.build.display.id");
            this.serialNum = (String) get.invoke(object, "ro.serialno");
            fingerprint = (String) get.invoke(object, "ro.build.fingerprint");
        } catch (Exception e) {
        }
    }

    public static String getAndroidID(Context context) {
        try {
            return android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID) + "";
        } catch (Exception e) {

        }

        return "";
    }

    public static String getMacAddress(Context context) {
        try {
            WifiInfo info = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
            if (info != null) {
                return info.getMacAddress();
            }
        } catch (Exception e) {
        }

        return "";
    }
}
