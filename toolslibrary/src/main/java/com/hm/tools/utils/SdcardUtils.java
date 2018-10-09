package com.hm.tools.utils;

import android.os.Environment;

import java.io.File;


public class SdcardUtils {

    private static String httpCachePath;

    public static String getHttpCachePath() {
        File externalFile = Environment.getExternalStorageDirectory();
        File file = new File(externalFile, ".ha");
        return file.toString();
    }
}
