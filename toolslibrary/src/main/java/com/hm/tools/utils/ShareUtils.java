package com.hm.tools.utils;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;



public class ShareUtils {

    /**
     * @param context
     * @param title        分享标题
     * @param url          分享url
     * @param chooserTitle 选择dialog 标题
     */
    public static void sharePage(Context context, String title, String url, String chooserTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);

        try {
            Intent intent = Intent.createChooser(shareIntent, chooserTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static void shareImage(Context context, String filePath, String chooserTitle) {
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, filePath);
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (uri == null) {
            uri = Uri.fromFile(new File(filePath));
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        //shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);

        try {
            Intent intent = Intent.createChooser(shareIntent, chooserTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static void shareImageByUriPath(Context context, String uriPath, String chooserTitle) {
        try {
            Uri uri = Uri.parse(uriPath);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            //shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            Intent intent = Intent.createChooser(shareIntent, chooserTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }
}
