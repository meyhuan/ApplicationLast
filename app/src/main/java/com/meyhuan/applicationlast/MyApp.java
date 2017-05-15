package com.meyhuan.applicationlast;

import android.app.Application;

import com.growingio.android.sdk.collection.Configuration;
import com.growingio.android.sdk.collection.GrowingIO;
import com.meyhuan.applicationlast.hook.Hooker;

/**
 * User  : guanhuan
 * Date  : 2017/5/14
 */

public class MyApp extends Application{

    private static MyApp INSTANCE ;

    public static MyApp getInstance() {
        return INSTANCE;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        try {
            Hooker.hookLayoutInflater();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GrowingIO.startWithConfiguration(this, new Configuration()
                .useID()
                .trackAllFragments()
                .setChannel("XXX应用商店"));
    }
}
