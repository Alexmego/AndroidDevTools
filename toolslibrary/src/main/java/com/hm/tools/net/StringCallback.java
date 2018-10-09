package com.hm.tools.net;

public interface StringCallback {
    public abstract void onSuccess(String result);
    public abstract void onFailure(int errorCode, String errorMsg);
}
