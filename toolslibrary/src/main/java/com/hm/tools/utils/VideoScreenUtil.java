package com.ume.player.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class VideoScreenUtil {

    private Activity mActivity;
    private int mLastOrientation;
    private boolean mForceLandSpace;
    private View mCustomView;

    public boolean videoForceLandscape() {
        return mForceLandSpace;
    }

    public VideoScreenUtil(Activity activity, View customView) {
        this.mActivity = activity;
        this.mCustomView = customView;
    }

    public void setOrientation(Activity activity) {
        mLastOrientation = activity.getRequestedOrientation();
    }

    public void setFullScreen(boolean fullScreen) {
        Window win = mActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (fullScreen) {
            winParams.flags |= bits;
            mLastOrientation = mActivity.getRequestedOrientation();
            if (mActivity.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                // 不是横屏时强制横屏
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mForceLandSpace = true;
            } else {
                mForceLandSpace = false;
            }
        } else {
            winParams.flags &= ~bits;
            if (mForceLandSpace) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if (mCustomView != null) {
                mCustomView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            mActivity.setRequestedOrientation(mLastOrientation);
            //reset
            mForceLandSpace = false;
        }
        win.setAttributes(winParams);
    }
}
