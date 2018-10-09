package com.hm.tools.utils;

import java.io.Closeable;
import java.io.IOException;

public class StreamUtil {
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }
}
