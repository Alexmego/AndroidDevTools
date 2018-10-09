package com.hm.tools.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.concurrent.atomic.AtomicInteger;


public class KeyboardUtils {
    public static void showKeyboard(final View view) {
        if (view != null) {
            final Handler handler = new Handler();
            final AtomicInteger attempt = new AtomicInteger();
            Runnable openRunnable = new Runnable() {
                public void run() {
                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    try {
                        imm.showSoftInput(view, 0);
                    } catch (IllegalArgumentException e) {
                        if (attempt.incrementAndGet() <= 10) {
                            handler.postDelayed(this, 100L);
                        } else {
                            Log.e("KeyboardUtils", "Unable to open keyboard.  Giving up.", e);
                        }
                    }

                }
            };
            openRunnable.run();
        }
    }

    public static boolean hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        return false;
    }

}
