package com.meyhuan.applicationlast;

import android.app.Application;

import com.meyhuan.applicationlast.hook.Hooker;
import com.umeng.analytics.MobclickAgent;

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

        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,
                "56fa320d67e58ed80200281b", "yy"));
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.setCatchUncaughtExceptions(false);

        INSTANCE = this;
        try {
            Hooker.hookLayoutInflater();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        GrowingIO.startWithConfiguration(this, new Configuration()
//                .useID()
//                .trackAllFragments()
//                .setChannel("XXX应用商店"));
    }
}
