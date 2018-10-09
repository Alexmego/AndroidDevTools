package com.hm.tools.net;

import com.hm.tools.utils.HandlerUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequest {
    private static HttpRequest instance = null;

    public static HttpRequest getInstance() {
        if (instance == null) {
            synchronized (HttpRequest.class) {
                if (instance == null) {
                    instance = new HttpRequest();
                }
            }
        }
        return instance;
    }

    private OkHttpClient mOkHttpClient;

    private HttpRequest() {
        mOkHttpClient = OkHttpClientProvider.getInstance().getOkHttpClient();
    }

    public void postForString(String url, Map<String, String> headers, Map<String, String> params, final StringCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback mast not be null!");
        }

        Request.Builder builder = new Request.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        FormBody.Builder bodyBuilder = new FormBody.Builder();
        String value;
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                value = entry.getValue();
                if (value == null) {
                    value = "";
                }
                bodyBuilder.add(entry.getKey(), value);
            }
        }

        Request request = builder.url(url).post(bodyBuilder.build()).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                HandlerUtils.postOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(-1, e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (!call.isCanceled() && response.isSuccessful()) {
                    final String result = response.body().string();
                    HandlerUtils.postOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(result);
                        }
                    });
                } else {
                    HandlerUtils.postOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(response.code(), response.message());
                        }
                    });
                }
            }
        });
    }
}
