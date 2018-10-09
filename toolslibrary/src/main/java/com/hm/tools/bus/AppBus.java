package com.hm.tools.bus;


import com.squareup.otto.Bus;


public class AppBus extends Bus {

    private static class SingletonHolder {
        static final AppBus INSTANCE = new AppBus();
    }

    public static AppBus getInstance() {
        return SingletonHolder.INSTANCE;
    }


}
