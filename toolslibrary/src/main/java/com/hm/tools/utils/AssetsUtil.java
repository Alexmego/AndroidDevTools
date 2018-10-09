package com.hm.tools.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class AssetsUtil {
    public static String getStringFromAssetsFile(Context context, String filePath) {
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getResources().getAssets().open(filePath));
            bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder builder = new StringBuilder();
            while ((line = bufReader.readLine()) != null)
                builder.append(line);
            return builder.toString();
        } catch (Exception ignored) {

        } finally {
            if (inputReader != null) {
                try {
                    inputReader.close();
                } catch (IOException ignored) {
                }
            }
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return "";
    }

    public static boolean copyAssetsFile(Context context, String assetsFile, String destFile) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getAssets().open(assetsFile);
            fos = new FileOutputStream(new File(destFile));
            byte[] buf = new byte[4096];
            int readBytes = 0;
            while ((readBytes = is.read(buf)) != -1) {
                fos.write(buf, 0, readBytes);
            }

            fos.flush();
            return true;
        } catch (Exception ignored) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {

                }
            }
        }

        return false;
    }
}
