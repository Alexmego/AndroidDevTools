package com.hm.tools.utils;

import android.content.Context;
import android.os.Environment;

import com.hm.tools.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <uses-permission android:name="android.permission.READ_LOGS" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 */
public class LogCacheUtil {
    private String PATH_LOGCAT;
    private LogDumper mLogDumper = null;
    private int mPId = android.os.Process.myPid();
    private ThreadLocal<SimpleDateFormat> mDateLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss", Locale.getDefault());
        }
    };

    public void startLogcatToFile(Context context) {

        if (BuildConfig.DEBUG) {
            String folderPath;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                folderPath = Environment.getExternalStorageDirectory() + "/android/data/" + context.getPackageName() + "/logcat/";
            } else {
                folderPath = context.getFilesDir().getAbsolutePath() + File.separator + "Logcat";

            }
            start(folderPath, "logcat -s AD_LOG");
        }
    }


    public void stopLogcatToFile() {
        stop();
    }

    private void setFolderPath(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean b = folder.mkdirs();
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("The logcat folder path is not a directory: " + folderPath);
        }
        PATH_LOGCAT = folderPath.endsWith("/") ? folderPath : folderPath + "/";
    }

    private void start(String saveDirectory, String cmd) {
        setFolderPath(saveDirectory);
        if (mLogDumper == null) {
            mLogDumper = new LogDumper(String.valueOf(mPId), PATH_LOGCAT, cmd);
        }
        mLogDumper.start();
    }

    private void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }

    private class LogDumper extends Thread {
        private Process process;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        private String cmd = null;
        private String mPID;
        private FileOutputStream out = null;

        LogDumper(String pid, String dir, String cmd) {
            mPID = pid;
            try {
                out = new FileOutputStream(new File(dir, "logcat-" + mDateLocal.get().format(new Date())
                        + ".log"), true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (cmd == null)
                this.cmd = "logcat | grep \\(" + mPID + "\\)";
            else
                this.cmd = cmd;
        }

        void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                process = Runtime.getRuntime().exec(cmd);
                mReader = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024);
                String line;
                Calendar instance = Calendar.getInstance(Locale.getDefault());
                while (mRunning && (line = mReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    if (out != null && line.contains(mPID)) {
                        StringBuilder builder = new StringBuilder();
                        int hour = instance.get(Calendar.HOUR);
                        int minute = instance.get(Calendar.MINUTE);
                        int second = instance.get(Calendar.SECOND);
                        int milliSecond = instance.get(Calendar.MILLISECOND);
                        byte[] bytes = builder.append(hour).append(":").append(minute).append(":")
                                .append(second).append(":").append(milliSecond)
                                .append("  ").append(line).append("\n").toString().getBytes();
                        out.write(bytes);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (process != null) {
                    process.destroy();
                    process = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out = null;
                }
            }
        }

    }

}