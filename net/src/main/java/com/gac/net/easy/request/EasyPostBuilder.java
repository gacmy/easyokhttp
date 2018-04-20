package com.gac.net.easy.request;



import com.gac.net.easy.EasyOkHttp;
import com.gac.net.easy.callback.EasyCallback;
import com.gac.net.easy.response.IResponseHandler;
import com.google.gson.Gson;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by gacmy on 2018/3/13.
 */

public class EasyPostBuilder extends EasyRequestBuilder<EasyPostBuilder> {
    private String mJsonParams = "";
    public EasyPostBuilder() {
        super(new EasyOkHttp());
    }

    public EasyPostBuilder jsonParams(String json){
        mJsonParams = json;
        return this;
    }
    @Override
   public void enqueue(IResponseHandler responseHandler) {
        if(mUrl == null || mUrl.length() == 0){
            throw new IllegalArgumentException("url can not be null !");
        }
        Request.Builder builder = new Request.Builder().url(mUrl);
        appendHeaders(builder,mHeaders);
        String headParamsInfo = "HeadParams:";
        if(mHeaders != null && mHeaders.size() > 0){
            headParamsInfo += new Gson().toJson(mHeaders);
        }
        if(mTag != null){
            builder.tag(mTag);
        }
        String mParamsInfo;
        if(mJsonParams.length() > 0) {      //上传json格式参数
            mParamsInfo = mJsonParams;
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonParams);
            builder.post(body);
        } else {
            //普通kv参数
            FormBody.Builder encodingBuilder = new FormBody.Builder();
            appendParams(encodingBuilder, mParams);
            builder.post(encodingBuilder.build());
            mParamsInfo = new Gson().toJson(mParams);
        }
        Request request = builder.build();
        mClient.getOkHttpClient()
                .newCall(request)
                .enqueue(new EasyCallback(responseHandler,"METHOD:POST",mUrl,"REQUEST PARAMS:"+mParamsInfo,headParamsInfo));
    }

    //append params to form builder
    private void appendParams(FormBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }
}

















