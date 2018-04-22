package com.gac.net.easy.request;


import com.gac.net.easy.EasyOkHttp;
import com.gac.net.easy.callback.EasyCallback;
import com.gac.net.easy.response.IResponseHandler;
import com.google.gson.Gson;


import java.util.Map;

import okhttp3.Request;

/**
 * Created by gacmy on 2018/3/13.
 * get 请求
 */

public class EasyGetBuilder extends EasyRequestBuilder<EasyGetBuilder> {
    public EasyGetBuilder() {
        super(new EasyOkHttp());
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        try{
            if(mUrl == null || mUrl.length() == 0){
                throw new IllegalArgumentException("url can not be null");
            }
            if(mParams != null && mParams.size() > 0){
                mUrl = appendParams(mUrl,mParams);
            }

            Request.Builder builder = new Request.Builder().url(mUrl).get();
            appendHeaders(builder, mHeaders);
            String headparamsInfo = "HeadParams:";
            if(mHeaders != null && mHeaders.size() > 0){
                headparamsInfo += new Gson().toJson(mHeaders);
            }
            if (mTag != null) {
                builder.tag(mTag);
            }
            Request request = builder.build();
            mClient.getOkHttpClient().
                    newCall(request).
                    enqueue(new EasyCallback(responseHandler,"METHOD:GET","URL:"+mUrl,"params:",headparamsInfo));
        }catch (Exception e){
            //LogUtils.e("Get enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }

    //append params to url
    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
