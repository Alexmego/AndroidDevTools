package com.hm.tools.net;

import android.content.res.AssetManager;

import com.hm.tools.BuildConfig;
import com.hm.tools.utils.SdcardUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpClientProvider {
    private volatile static OkHttpClientProvider instance = null;
    private static final long mTimeout = 10 * 1000;
    private OkHttpClient mOkHttpClient;
    private AssetManager assets;

    public static OkHttpClientProvider getInstance() {
        if (instance == null) {
            synchronized (OkHttpClientProvider.class) {
                if (instance == null) {
                    instance = new OkHttpClientProvider();
                }
            }
        }
        return instance;
    }

    private static final int HTTP_CACHE_SIZE = 1024 * 1024;

    private OkHttpClientProvider() {
        X509TrustManager manager = new CustomTrustManager();
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(mTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(mTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(mTimeout, TimeUnit.MILLISECONDS).sslSocketFactory(getVerifyFactory(),manager);
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new CustomInterceptor());
        }
        builder.cache(new Cache(new File(SdcardUtils.getHttpCachePath()), HTTP_CACHE_SIZE));
        mOkHttpClient = builder.build();
    }

    public OkHttpClient getOkHttpClient() {

        return mOkHttpClient;
    }

    public AssetManager getAssets() {
        return assets;
    }

    class CustomInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            /*
            long t1 = System.nanoTime();
            Logger.i(String.format("Sending request url:%s ", request.url()));

            long t2 = System.nanoTime();
            Logger.i(String.format("Received response in%.1f ms %n %s", (t2 - t1) / 1e6d, response.headers()));
            */

            return response;
        }
    }


    public SSLSocketFactory getVerifyFactory() {
        try {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // From https://www.washington.edu/itconnect/security/ca/load-der.crt
            // FIXME: 2017/11/24
            AssetManager assetManager = getAssets();
            InputStream open = assetManager.open("down360safecom.crt");
            Certificate ca = null;
            try (InputStream caInput = new BufferedInputStream(open)) {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } catch (CertificateException ignored) {
            }
            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            // Tell the URLConnection to use a SocketFactory from our SSLContext
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static class CustomTrustManager implements X509TrustManager{
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
