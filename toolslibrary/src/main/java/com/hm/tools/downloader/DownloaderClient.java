package com.hm.tools.downloader;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

public class DownloaderClient {

    private Context context;
    private DownloadManager downloadManager;
    DownloaderClient(Context context) {
        this.context = context;
        this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    private static DownloaderClient sInstance;
    public static synchronized DownloaderClient getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new DownloaderClient(context);
        }
        return sInstance;
    }

    public long startDownload(Uri uri) {
        DownloadManager.Request req = new DownloadManager.Request(uri);

        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//        req.setAllowedOverRoaming()

        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "ume");
        req.allowScanningByMediaScanner();
        req.setVisibleInDownloadsUi(true);
        String fileName = uri.getLastPathSegment();

        req.setTitle(fileName);
//        req.setDescription("下载完后请点击打开");
//        req.setMimeType("application/vnd.android.package-archive");
        req.setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileName));

        return downloadManager.enqueue(req);
    }

    public void setApkDownloadDoneAutoOpen(final Long startDownloadingId) {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (startDownloadingId - downloadId == 0) {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    Uri downloadFileUri = downloadManager.getUriForDownloadedFile(startDownloadingId);
                    install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);
                }
            }
        };
        context.registerReceiver(receiver, filter);
    }


    public void checkDownloadingStatus(Long startDownloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(startDownloadId);
        Cursor c = downloadManager.query(query);
        if (c != null && c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PENDING:
                    break;
                case DownloadManager.STATUS_PAUSED:
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    break;
                case DownloadManager.STATUS_FAILED:
                    break;
            }
            if (c != null) {
                c.close();
            }
        }
    }

    public String getDownloadedFileUri(Long startDownloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(startDownloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            if (c.moveToFirst()) {
                String fileUri = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                return fileUri;
            }
            c.close();
        }
        return null;
    }

}
