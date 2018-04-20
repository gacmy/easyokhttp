package com.gac.net.easy.response;


import com.gac.net.easy.EasyOkHttp;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by gacmy on 2018/3/12.
 * //处理list数组 也可以用JsonResponseHandler处理
 */

public abstract class GsonListResponseHandler<T> implements IResponseHandler {
    private Type mType;
    public GsonListResponseHandler(){
        Type myclass = getClass().getGenericSuperclass();//获取带泛型的class
        if(myclass instanceof Class){
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) myclass;      //获取所有泛型
        mType = $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);  //将泛型转为type
    }
    private Type getType(){
        return mType;
    }

    @Override
    public void onSuccess(final Response response) {
        ResponseBody responseBody = response.body();
        String responseBodyStr = "";
        try {
            responseBodyStr = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
            EasyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                   onFailure(response.code(),"failed read response body");
                }
            });
            return;
        }finally {
            try {
                responseBody.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final String finalResponseBodyStr = responseBodyStr;

        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<T>>() {
            }.getType();
           final List<T>  array = gson.fromJson(finalResponseBodyStr,type );
            EasyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(response.code(), array);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //LogUtils.e("onResponse fail parse gson, body=" + finalResponseBodyStr);
            EasyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), "fail parse gson, body=" + finalResponseBodyStr);
                }
            });

        }

    }
    public abstract void onSuccess(int statusCode,List<T> result);
}


























