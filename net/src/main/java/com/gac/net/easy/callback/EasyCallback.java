package com.gac.net.easy.callback;

import android.util.Log;


import com.gac.net.easy.EasyOkHttp;
import com.gac.net.easy.response.IResponseHandler;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by gacmy on 2018/3/12.
 * 通用回调接口 增加处理器
 */

public  class EasyCallback implements Callback {
    String method;
    String url;
    String params;
    String headParams;
    private IResponseHandler mResponseHandler;

    public EasyCallback(IResponseHandler handler,String method,String url,String params,String headParmas){
        mResponseHandler = handler;
        this.method = method;
        this.url = url;
        this.params = params;
        this.headParams = headParmas;
    }


    @Override
    public void onFailure(Call call,final IOException e) {
        EasyOkHttp.mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mResponseHandler != null){
                    log(e.toString());
                    mResponseHandler.onFailure(0,e.toString());
                }

            }
        });
    }

    @Override
    public void onResponse(Call call,final Response response) throws IOException {
        if(response.isSuccessful() && mResponseHandler != null) {
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // request the entire body.
            Buffer buffer = source.buffer();
            String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));
            log(responseBodyString);
            mResponseHandler.onSuccess(response);
        } else {
            //LogUtils.e("onResponse fail status=" + response.code());

            EasyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mResponseHandler != null){
                        mResponseHandler.onFailure(response.code(), "fail status=" + response.code());
                        log("fail status=" + response.code());
                    }

                }
            });
        }
    }
    //日志打印工具
    private void log(String content){
        if(EasyOkHttp.ISDEBUG){
            Log.e("EasyOkhttp","******************easyokhttp requst start**************");
            Log.e("EasyOkhttp",method);
            Log.e("EasyOkhttp",url);
            Log.e("EasyOkhttp",headParams);
            Log.e("EasyOkhttp",params);
            Log.e("EasyOkhttp","return content:"+content);
            Log.e("EasyOkhttp","******************easyokhttp requst   end***************");
        }

    }
}
