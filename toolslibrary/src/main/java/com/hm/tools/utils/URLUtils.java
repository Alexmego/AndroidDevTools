package com.hm.tools.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class URLUtils {
    // all domain names
//    private static final String[] ext = {
//            "top", "com", "net", "org", "edu", "gov", "int", "mil", "cn", "tel", "biz", "cc", "tv", "info",
//            "name", "hk", "mobi", "asia", "cd", "travel", "pro", "museum", "coop", "aero", "ad", "ae", "af",
//            "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "az", "ba", "bb", "bd",
//            "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz",
//            "ca", "cc", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cq", "cr", "cu", "cv", "cx",
//            "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "es", "et", "ev", "fi",
//            "fj", "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gh", "gi", "gl", "gm", "gn", "gp",
//            "gr", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "in", "io",
//            "iq", "ir", "is", "it", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw",
//            "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md",
//            "mg", "mh", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mv", "mw", "mx", "my", "mz",
//            "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nt", "nu", "nz", "om", "qa", "pa",
//            "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "pt", "pw", "py", "re", "ro", "ru", "rw",
//            "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "st",
//            "su", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk", "tm", "tn", "to", "tp", "tr", "tt",
//            "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "va", "vc", "ve", "vg", "vn", "vu", "wf", "ws",
//            "ye", "yu", "za", "zm", "zr", "zw"
//    };
//
//    private static final Pattern CUSTOM_WEB_URL;
//
//    static {
//        StringBuilder sb = new StringBuilder();
//        sb.append("(");
//        for (int i = 0; i < ext.length; i++) {
//            sb.append(ext[i]);
//            sb.append("|");
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        sb.append(")");
//        // final pattern str
//        String pattern = "((https?|s?ftp|irc[6s]?|git|afp|telnet|smb)://)?((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|((www\\.|[a-zA-Z\\.]+\\.)?[a-zA-Z0-9\\-]+\\." + sb.toString() + "(:[0-9]{1,5})?))((/[a-zA-Z0-9\\./,;\\?'\\+&%\\$#=~_\\-]*)|([^\\u4e00-\\u9fa5\\s0-9a-zA-Z\\./,;\\?'\\+&%\\$#=~_\\-]*))";
//        // Log.v(TAG, "pattern = " + pattern);
//        CUSTOM_WEB_URL = Pattern.compile(pattern);
//    }


    public static boolean isValidUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                return true;
            } else if (Patterns.WEB_URL.matcher(url).matches()) {
                return true;
            }
        }

        return false;
        //return !TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url).matches();
    }

    /**
     * 检查网址是否带有http://头
     *
     * @param url
     * @return
     */
    public static String addUrlHeader(String url) {
        //if ((!url.startsWith("http://")) && (!url.startsWith("https://")) && (!url.startsWith("file://")))
        if (!url.contains("//")) {
            return "http://" + url;
        }
        return url;
    }

    /**
     * @return True iff the url is an about: url.
     */
    public static boolean isAboutUrl(String url) {
        return (null != url) && url.startsWith("about:");
    }

    /**
     * @return True iff the url is a data: url.
     */
    public static boolean isDataUrl(String url) {
        return (null != url) && url.startsWith("data:");
    }

    /**
     * @return True iff the url is a javascript: url.
     */
    public static boolean isJavaScriptUrl(String url) {
        return (null != url) && url.startsWith("javascript:");
    }

    public static boolean isHttpOrHttpsUrl(String url) {
        return isHttpUrl(url) || isHttpsUrl(url);
    }

    /**
     * @return True iff the url is an http: url.
     */
    public static boolean isHttpUrl(String url) {
        return (null != url) &&
                (url.length() > 6) &&
                url.substring(0, 7).equalsIgnoreCase("http://");
    }

    /**
     * @return True iff the url is an https: url.
     */
    public static boolean isHttpsUrl(String url) {
        return (null != url) &&
                (url.length() > 7) &&
                url.substring(0, 8).equalsIgnoreCase("https://");
    }

    /**
     * add by Figo,2017.2.28
     *
     * @param address
     * @return
     */
    public static String getDomainFirstChar(String address) {

        String domainFirstChar = "IP";

        if (!TextUtils.isEmpty(address)) {
            if (!isHostIP(address)) {
                try {
                    address = addUrlHeader(address);
                    URL url = new URL(address);
                    String host = url.getHost();
                    if (!TextUtils.isEmpty(host)) {
                        if (!host.contains(".") || !host.substring(0, host.lastIndexOf(".")).contains(".")) {
                            domainFirstChar = host.substring(0, 1);
                        } else {
                            int i = host.indexOf(".");
                            domainFirstChar = host.substring(i + 1, i + 2);
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        return domainFirstChar;
    }


    public static boolean isHostIP(String urlStr) {
        urlStr = URLUtils.addUrlHeader(urlStr);
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String host = url.getHost();// 获取主机名
        if (host.length() < 7 || host.length() > 15 || "".equals(host)) {
            return false;
        }
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(host);
        boolean ipAddress = mat.find();
        return ipAddress;
    }

    public static String deleteUrlHeader(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            url = url.substring(url.indexOf("//") + 2);
        }
        return url;
    }

    /**
     * This function will fix up the url typed to try to make it a valid url for
     * loading in a ChromeView.
     *
     * @param url
     */
    public static String fixUrl(String url) {
        // todo(feldstein): This needs to be beefed up and
        // ensure it still supports about:crash et al.
//        if (url.indexOf(":") == -1)
//            url = "http://" + url;

        if (!url.contains(":/")) {
            url = "http://" + url;
        }
        return url;
    }


    public static String getDomainName(String url) {
        url = addUrlHeader(url);
        String topMain = getTopMain(url);
        String name = topMain;
        if (topMain.contains(".")) {
            name = topMain.substring(0, topMain.indexOf("."));
        }
        return upperCase(name);
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String upperCase(String str) {
        if (!TextUtils.isEmpty(str)) {
            char[] ch = str.toCharArray();
            if (ch[0] >= 'a' && ch[0] <= 'z') {
                ch[0] = (char) (ch[0] - 32);
            }
            str = new String(ch);
        }
        return str;
    }

    public static String getTopMain(String url) {
        String regStr = "[0-9a-zA-Z]+((\\.com)|(\\.cn)|(\\.org)|(\\.net)|(\\.edu)|(\\.com.cn)|(\\.xyz)|(\\.xin)|(\\.club)|(\\.shop)|(\\.site)|(\\.wang)" +
                "|(\\.top)|(\\.win)|(\\.online)|(\\.tech)|(\\.store)|(\\.bid)|(\\.cc)|(\\.ren)|(\\.lol)|(\\.pro)|(\\.red)|(\\.kim)|(\\.space)|(\\.link)|(\\.click)|(\\.news)|(\\.news)|(\\.ltd)|(\\.website)" +
                "|(\\.biz)|(\\.help)|(\\.mom)|(\\.work)|(\\.date)|(\\.loan)|(\\.mobi)|(\\.live)|(\\.studio)|(\\.info)|(\\.pics)|(\\.photo)|(\\.trade)|(\\.vc)|(\\.party)|(\\.game)|(\\.rocks)|(\\.band)" +
                "|(\\.gift)|(\\.wiki)|(\\.design)|(\\.software)|(\\.social)|(\\.lawyer)|(\\.engineer)|(\\.org)|(\\.net.cn)|(\\.org.cn)|(\\.gov.cn)|(\\.name)|(\\.tv)|(\\.me)|(\\.asia)|(\\.co)|(\\.press)|(\\.video)|(\\.market)" +
                "|(\\.games)|(\\.science)|(\\.中国)|(\\.公司)|(\\.网络)|(\\.pub)" +
                "|(\\.la)|(\\.auction)|(\\.email)|(\\.sex)|(\\.sexy)|(\\.one)|(\\.host)|(\\.rent)|(\\.fans)|(\\.cn.com)|(\\.life)|(\\.cool)|(\\.run)" +
                "|(\\.gold)|(\\.rip)|(\\.ceo)|(\\.sale)|(\\.hk)|(\\.io)|(\\.cl)|(\\.gg)|(\\.tm)|(\\.com.hk)|(\\.gs)|(\\.us))";
        String host = null;
        String domain = url;
        try {
            URL urlObj = new URL(url);
            host = urlObj.getHost().toLowerCase();
            Pattern p = Pattern.compile(regStr);
            Matcher m = p.matcher(host);
            while (m.find()) {
                domain = m.group();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            domain = deleteUrlHeader(domain);
            return domain;
        }
    }

    public static String getTopMainUrl(String url) {
        String domainUrl = null;
        String protocol = null;
        String domain = getTopMain(url);
        try {
            URL urlObj = new URL(url);
            protocol = urlObj.getProtocol();
            if (!TextUtils.isEmpty(domain)) {
                domainUrl = protocol + "://" + domain;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            return domainUrl;
        }
    }
}
