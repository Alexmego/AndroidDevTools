package com.hm.tools.utils;

import android.content.Context;

import java.lang.reflect.Field;

public class ResourceIDUtils {

    public static int getStringId(Context context,String name) {
        int stringID = context.getResources().getIdentifier(name, "string", context.getPackageName());
        return stringID;
    }

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }

}
