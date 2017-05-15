package com.meyhuan.applicationlast.hook;

import android.content.Context;


import com.meyhuan.applicationlast.inflate.CustomLayoutInflater;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * User  : guanhuan
 * Date  : 2017/5/3
 */

public class Hooker {
    private static final String TAG = "Hooker";


    public static void hookLayoutInflater() throws Exception {
        //获取 ServiceFetcher 实例 ServiceFetcherImpl
        Class<?> ServiceFetcher = Class.forName("android.app.SystemServiceRegistry$ServiceFetcher");
        Object ServiceFetcherImpl = Proxy.newProxyInstance(Hooker.class.getClassLoader(),
                new Class[]{ServiceFetcher}, new ServiceFetcherHandler());

        //获取 SystemServiceRegistry # registerService 方法
        Class<?> SystemServiceRegistry = Class.forName("android.app.SystemServiceRegistry");
        Method registerService = SystemServiceRegistry.getDeclaredMethod("registerService",
                String.class, CustomLayoutInflater.class.getClass(), ServiceFetcher);
        registerService.setAccessible(true);

        //调用 registerService 方法，将自定义的 CustomLayoutInflater 设置到 SystemServiceRegistry
        registerService.invoke(SystemServiceRegistry,
                new Object[]{Context.LAYOUT_INFLATER_SERVICE, CustomLayoutInflater.class, ServiceFetcherImpl});

        //获取 SystemServiceRegistry 的 SYSTEM_SERVICE_FETCHERS 静态变量
//        Field SYSTEM_SERVICE_FETCHERS = SystemServiceRegistry.getDeclaredField("SYSTEM_SERVICE_FETCHERS");
//        SYSTEM_SERVICE_FETCHERS.setAccessible(true);
//        Log.e(TAG, SYSTEM_SERVICE_FETCHERS.getName());
//        HashMap SYSTEM_SERVICE_FETCHERS_FIELD = (HashMap) SYSTEM_SERVICE_FETCHERS.get(SystemServiceRegistry);
//
//        Set set = SYSTEM_SERVICE_FETCHERS_FIELD.keySet();
//        Iterator iterator = set.iterator();



    }

}
