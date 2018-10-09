package com.hm.tools.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FileUtils {
    static class FilterHiddenFile implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return !pathname.isHidden();
        }
    }

    public static boolean saveObject(Context context, Serializable serializable, String name) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            String filePath = context.getCacheDir().getAbsolutePath() + File.separator;
            File dirFile = new File(filePath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(filePath, name);
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(serializable);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static Serializable readObject(Context context, String name) {
        String filePath = context.getCacheDir().getAbsolutePath() + File.separator;
        if (!isExistDataCache(context, name))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        File file = null;
        ;
        try {
            file = new File(filePath, name);
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof InvalidClassException) {
                if (file != null && file.exists()) {
                    file.delete();
                }
            }
        } finally {
            try {
                ois.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 判断缓存是否存在
     */
    public static boolean isExistDataCache(Context context, String name) {
        if (context == null) {
            return false;
        }

        String cachefile = context.getCacheDir().getAbsolutePath() + File.separator;
        boolean exist = false;
        File dirFile = new File(cachefile);
        if (dirFile.exists()) {
            File file = new File(cachefile, name);
            if (file.exists()) {
                exist = true;
            }
        }
        return exist;
    }

    public static String readFileData(Context context, String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            BufferedReader br = null;
            if (file.exists()) {
                try {
                    br = new BufferedReader(new FileReader(file));
                    String readLine = "";
                    StringBuffer sb = new StringBuffer();
                    while ((readLine = br.readLine()) != null) {
                        sb.append(readLine);
                    }
                    return sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}
