package com.gac.net.easy;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;


import com.gac.net.easy.certifate.SSLSocketClient;
import com.gac.net.easy.cookie.CookiesManager;

import java.io.InputStream;


import okhttp3.Call;


import okhttp3.Dispatcher;

import okhttp3.OkHttpClient;


/**
 * Created by gacmy on 2018/3/12.
 */

public class EasyOkHttp {
    public final static boolean ISDEBUG = true;
    public static Application mApplication;
    private static OkHttpClient mClient;
    public static Handler mHandler = new Handler(Looper.getMainLooper());
    private static boolean isSupportHttps = true;//是否支持https

    public OkHttpClient getOkHttpClient(){
        return mClient;
    }
    public EasyOkHttp(){
        this(null,false,null);
    }
    public EasyOkHttp(InputStream certicicates){
        this(null,false,certicicates);
    }
    public EasyOkHttp(boolean isSupportCookie){
        this(null,isSupportCookie,null);
    }
    public EasyOkHttp(OkHttpClient client,boolean isSupportCookie,InputStream certificates){
        if(mClient == null){
            synchronized (EasyOkHttp.class){
                if(client == null){
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    if(isSupportCookie){
                        builder.cookieJar(new CookiesManager());
                    }
                    if(isSupportHttps){
                        if (certificates != null){
                            builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(certificates));
                        }else{
                            //忽略证书
                            builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
                            builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
                        }
                    }
                    mClient = builder.build();
                }else{
                    mClient = client;
                }
            }
        }
    }

    //初始化 注入全局context
    public static void init(Application application){
        mApplication = application;
    }


    /**
     * do cacel by tag
     * @param tag tag
     */
    public void cancel(Object tag) {
        Dispatcher dispatcher = mClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
