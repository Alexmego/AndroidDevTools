package com.hm.tools.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Patterns;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class BitmapLoaderImpl {

    private static String TAG = "BitmapLoader";


    public static void loadResource(Context context, Integer resourceId, ImageView view) {

        loadResource(context, resourceId, -1, -1, view);

    }

    public static void loadResource(Context context, Integer resourceId, int placeHolder, ImageView view) {
        loadResource(context, resourceId, placeHolder, -1, view);
    }


    public static void loadResource(Context context, Integer resourceId, int plcaeHolderId, int errorHolderId, ImageView view) {
        if (resourceId == null) {
            return;
        }

        if (plcaeHolderId < 0 && errorHolderId < 0) {
            Glide.with(context)
                    .load(resourceId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId < 0 && errorHolderId > 0) {
            Glide.with(context)
                    .load(resourceId)
                    .placeholder(plcaeHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId > 0 && errorHolderId < 0) {
            Glide.with(context)
                    .load(resourceId)
                    .placeholder(plcaeHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId > 0 && errorHolderId > 0) {
            Glide.with(context)
                    .load(resourceId)
                    .placeholder(plcaeHolderId)
                    .error(errorHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        }

    }


    public static void loadFile(Context context, File file, ImageView view) {
        loadFile(context, file, -1, -1, view);
    }

    public static void loadFile(Context context, File file, int placeHolder, ImageView view) {
        loadFile(context, file, placeHolder, -1, view);
    }

    public static void loadFile(Context context, File file, int plcaeHolderId, int errorHolderId, ImageView view) {
        if (!file.exists()) {
            return;
        }

        if (plcaeHolderId < 0 && errorHolderId < 0) {
            Glide.with(context)
                    .load(file)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId < 0 && errorHolderId > 0) {
            Glide.with(context)
                    .load(file)
                    .placeholder(plcaeHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId > 0 && errorHolderId < 0) {
            Glide.with(context)
                    .load(file)
                    .placeholder(plcaeHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId > 0 && errorHolderId > 0) {
            Glide.with(context)
                    .load(file)
                    .placeholder(plcaeHolderId)
                    .error(errorHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        }
    }


    public static void loadFile(Context context, File file, int plcaeHolderId, int errorHolderId, int animationId, int duration, ImageView view) {
        if (!file.exists()) {
            return;
        }
        Glide.with(context)
                .load(file)
                .placeholder(plcaeHolderId)
                .error(errorHolderId)
                .crossFade(animationId, duration)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }


    public static void loadUrl(Context context, String urlStr, ImageView view) {
        loadUrl(context, urlStr, -1, -1, view);
    }


    public static void loadUrl(Context context, String urlStr, int plcaeHolderId, ImageView view) {
        loadUrl(context, urlStr, plcaeHolderId, -1, view);
    }


    public static void loadUrl(Context context, String urlStr, int plcaeHolderId, int errorHolderId, ImageView view) {
        if (!Patterns.WEB_URL.matcher(urlStr).matches() || urlStr == null) {
            return;
        }

        if (plcaeHolderId < 0 && errorHolderId < 0) {
            Glide.with(context)
                    .load(urlStr)
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId < 0 && errorHolderId > 0) {
            Glide.with(context)
                    .load(urlStr)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(plcaeHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId > 0 && errorHolderId < 0) {
            Glide.with(context)
                    .load(urlStr)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(plcaeHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId > 0 && errorHolderId > 0) {
            Glide.with(context)
                    .load(urlStr)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(plcaeHolderId)
                    .error(errorHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        }
    }


    public static void loadurl(Context context, String urlStr, int plcaeHolderId, int errorHolderId, int animationId, int duration, ImageView view) {
        if (!Patterns.WEB_URL.matcher(urlStr).matches() || urlStr == null) {
            return;
        }
        Glide.with(context)
                .load(urlStr)
                .placeholder(plcaeHolderId)
                .error(errorHolderId)
                .crossFade(animationId, duration)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void loadUri(Context context, Uri uri, ImageView view) {
        loadUri(context, uri, -1, -1, view);
    }

    public static void loadUri(Context context, Uri uri, int placeHolder, ImageView view) {
        loadUri(context, uri, placeHolder, -1, view);
    }

    public static void loadUri(Context context, Uri uri, int plcaeHolderId, int errorHolderId, ImageView view) {
        if (uri == null) {
            return;
        }
        if (plcaeHolderId < 0 && errorHolderId < 0) {
            Glide.with(context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId < 0 && errorHolderId > 0) {
            Glide.with(context)
                    .load(uri)
                    .placeholder(plcaeHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId > 0 && errorHolderId < 0) {
            Glide.with(context)
                    .load(uri)
                    .placeholder(plcaeHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else if (plcaeHolderId > 0 && errorHolderId > 0) {
            Glide.with(context)
                    .load(uri)
                    .placeholder(plcaeHolderId)
                    .error(errorHolderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        }
    }


    public static void loadUri(Context context, Uri uri, int plcaeHolderId, int errorHolderId, int animationId, int duration, ImageView view) {
        if (uri == null) {
            return;
        }
        Glide.with(context)
                .load(uri)
                .placeholder(plcaeHolderId)
                .error(errorHolderId)
                .crossFade(animationId, duration)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }


    public static void loadAssets(Context context, String name, ImageView iv) {
        InputStream is = null;
        try {
            is = context.getAssets().open(name);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            iv.setImageBitmap(bitmap);
        } catch (OutOfMemoryError ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {

                }
            }
        }

    }

}
