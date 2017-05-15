package com.meyhuan.applicationlast.hook;

import android.content.Context;

import com.meyhuan.applicationlast.inflate.CustomLayoutInflater;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * User  : guanhuan
 * Date  : 2017/5/4
 */

public class ServiceFetcherHandler implements InvocationHandler{

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return new CustomLayoutInflater((Context) args[0]);
    }
}
