package com.gac.net.easy.request;

import com.gac.net.easy.EasyOkHttp;
import com.gac.net.easy.response.IResponseHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;

/**
 * Created by gacmy on 2018/3/13.
 */

public abstract class EasyRequestBuilder <T extends EasyRequestBuilder>{
    protected String mUrl;
    protected Object mTag;
    protected Map<String,String> mHeaders;
    protected EasyOkHttp mClient;
    protected Map<String,String> mParams;
    protected String mJsonParams = "";
    //异步回调
    abstract void enqueue(final IResponseHandler responseHandler);

    public EasyRequestBuilder(EasyOkHttp client){
        mClient = client;
    }
    public T jsonParams(String json){
        mJsonParams = json;
        return (T)this;
    }
    public T params(Map<String,String> params){
        mParams = params;
        return (T)this;
    }

    public T addParam(String key, String val) {
        if (this.mParams == null)
        {
            mParams = new LinkedHashMap<>();
        }
        mParams.put(key, val);
        return (T) this;
    }

    public T url(String url){
        this.mUrl = url;
        return (T)this;
    }

    public T tag(Object tag){
        this.mTag = tag;
        return (T)this;
    }

    public T headers(Map<String,String> headers){
        this.mHeaders = headers;
        return (T)this;
    }

    public T addHeader(String key,String val){
        if(this.mHeaders == null){
            mHeaders = new LinkedHashMap<>();
        }
        mHeaders.put(key,val);
        return (T)this;
    }

    protected void appendHeaders(Request.Builder builder, Map<String,String> headers){
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    protected void build(Request.Builder builder){
        Headers.Builder headerBuilder = new Headers.Builder();
        if (mHeaders == null || mHeaders.isEmpty()) return;
        for (String key : mHeaders.keySet()) {
            headerBuilder.add(key, mHeaders.get(key));
        }
        if(builder != null)
            builder.headers(headerBuilder.build());
    }
}





















