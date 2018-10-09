package com.hm.tools.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

public class ThirdUrlHandler {
    private static final String SCHEME_WTAI = "wtai://wp/";
    private static final String SCHEME_WTAI_AP = "wtai://wp/ap;";
    private static final String SCHEME_WTAI_MC = "wtai://wp/mc;";
    private static final String SCHEME_WTAI_SD = "wtai://wp/sd;";

    private static final String SCHEME_TEL = "tel:";

    public static boolean handleThirdApplication(final Context context, String url) {
        try {
            if (handleWtaiUrl(context, url)) {
                return true;
            }

            final Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            final PackageManager packageManager = context.getPackageManager();
            final ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfo != null) {
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean handleWtaiUrl(Context context, String url) {
        if (url.startsWith(SCHEME_WTAI)) {
            if (url.startsWith(SCHEME_WTAI_MC)) {
                // wtai://wp/mc;number
                // number=string(phone-number)
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(SCHEME_TEL + url.substring(SCHEME_WTAI_MC.length())));
                context.startActivity(intent);
                return true;
            } else if (url.startsWith(SCHEME_WTAI_SD)) {
                // wtai://wp/sd;dtmf
                // dtmf=string(dialstring)
                String dtmf = new String(decode(url.substring(SCHEME_WTAI_SD.length()).getBytes()));
                dtmf = dtmf.replace('*', ',');
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(SCHEME_TEL + dtmf));
                context.startActivity(intent);
                return true;
            } else if (url.startsWith(SCHEME_WTAI_AP)) {
                // wtai://wp/ap;number;name
                // number=string(phone-number)
                // name=string
                saveContactForWtaiUrl(context, url);
            }
        }

        return false;
    }

    private static void saveContactForWtaiUrl(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
            String number_name = url.substring(SCHEME_WTAI_AP.length());
            int index = number_name.indexOf(";");
            String number;
            String name = null;
            if (index != -1) {
                number = new String(decode(number_name.substring(0, index).getBytes()));
                name = new String(decode(number_name.substring(index + 1).getBytes()));
            } else {
                number = new String(decode(number_name.getBytes()));
            }

            StringBuilder number_temp = new StringBuilder();
            for (int i = 0; i < number.length(); i++) {
                char c = number.charAt(i);
                if (PhoneNumberUtils.isReallyDialable(c)) {
                    number_temp.append(c);
                }
            }

            intent.putExtra(ContactsContract.Intents.Insert.PHONE, number_temp.toString());
            if (name != null) {
                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
            }

            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] decode(byte[] url) throws IllegalArgumentException {
        if (url.length == 0) {
            return new byte[0];
        }

        // Create a new byte array with the same length to ensure capacity
        byte[] tempData = new byte[url.length];

        int tempCount = 0;
        for (int i = 0; i < url.length; i++) {
            byte b = url[i];
            if (b == '%') {
                if (url.length - i > 2) {
                    b = (byte) (parseHex(url[i + 1]) * 16
                            + parseHex(url[i + 2]));
                    i += 2;
                } else {
                    throw new IllegalArgumentException("Invalid format");
                }
            }
            tempData[tempCount++] = b;
        }
        byte[] retData = new byte[tempCount];
        System.arraycopy(tempData, 0, retData, 0, tempCount);
        return retData;
    }

    private static int parseHex(byte b) {
        if (b >= '0' && b <= '9') return (b - '0');
        if (b >= 'A' && b <= 'F') return (b - 'A' + 10);
        if (b >= 'a' && b <= 'f') return (b - 'a' + 10);

        throw new IllegalArgumentException("Invalid hex char '" + b + "'");
    }
}
