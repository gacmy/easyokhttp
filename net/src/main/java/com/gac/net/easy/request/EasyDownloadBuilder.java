package com.gac.net.easy.request;


import com.gac.net.easy.EasyOkHttp;
import com.gac.net.easy.body.ResponseProgressBody;
import com.gac.net.easy.callback.EasyDownloadCallback;
import com.gac.net.easy.response.DownloadResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gacmy on 2018/3/13.
 * //下载 请求
 */

public class EasyDownloadBuilder {
    private EasyOkHttp mClient;
    private String mUrl = "";
    private Object mTag;
    private Map<String,String> mHeaders;
    private String mFileDir = "";//文件dir
    private String mFileName = "";//文件名
    private String mFilePath="";//文件路径
    private Long mCompleteBytes = 0L;//已经完成的字节数 断点续传

    public EasyDownloadBuilder(){
        mClient = new EasyOkHttp();
    }

    public EasyDownloadBuilder url(String url){
        mUrl = url;
        return this;
    }

    public EasyDownloadBuilder tag(Object tag){
        mTag = tag;
        return this;
    }
    public EasyDownloadBuilder fileDir(String fileDir){
        mFileDir = fileDir;
        return this;
    }
    public EasyDownloadBuilder fileName(String fileName){
        mFileName = fileName;
        return this;
    }

    public EasyDownloadBuilder filePath(String filePath){
        mFilePath = filePath;
        return this;
    }

    public EasyDownloadBuilder headers(Map<String,String> headers){
        mHeaders = headers;
        return this;
    }

    public EasyDownloadBuilder addHeader(String key,String val){
        if(mHeaders == null){
            mHeaders = new LinkedHashMap<>();
        }
        mHeaders.put(key,val);
        return this;
    }

    public EasyDownloadBuilder setCompleteBytes( Long completeBytes) {
        if(completeBytes > 0L) {
            this.mCompleteBytes = completeBytes;
            addHeader("RANGE", "bytes=" + completeBytes + "-");     //添加断点续传header
        }
        return this;
    }

    public Call enqueue(final DownloadResponseHandler downloadResponseHandler){
        try{
            if(mUrl == null || mUrl.length() == 0){
                throw new IllegalArgumentException("url can not be null");
            }
            if(mFilePath == null || mFilePath.length() == 0){
                if(mFileDir.length() == 0 || mFileName.length() == 0){
                    throw new IllegalArgumentException("filePath can not be null !");
                }else{
                    mFilePath = mFileDir + mFileName;
                }
            }
            checkFilePath(mFilePath,mCompleteBytes);
            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            Request request = builder.build();

            Call call = mClient.getOkHttpClient().newBuilder()
                    .addNetworkInterceptor(new Interceptor() {      //设置拦截器
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response originalResponse = chain.proceed(chain.request());
                            return originalResponse.newBuilder()
                                    .body(new ResponseProgressBody(originalResponse.body(), downloadResponseHandler))
                                    .build();
                        }
                    })
                    .build()
                    .newCall(request);
            call.enqueue(new EasyDownloadCallback(downloadResponseHandler, mFilePath, mCompleteBytes));

            return call;
        }catch (Exception e){
            //LogUtils.e("Download enqueue error:" + e.getMessage());
            downloadResponseHandler.onFailure(e.getMessage());
            return null;
        }
    }

    //检查filePath有效性
    private void checkFilePath(String filePath, Long completeBytes) throws Exception {
        File file = new File(filePath);
        if(file.exists()) {
            return ;
        }

        if(completeBytes > 0L) {       //如果设置了断点续传 则必须文件存在
            throw new Exception("断点续传文件" + filePath + "不存在！");
        }

        if (filePath.endsWith(File.separator)) {
            throw new Exception("创建文件" + filePath + "失败，目标文件不能为目录！");
        }

        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            if(!file.getParentFile().mkdirs()) {
                throw new Exception("创建目标文件所在目录失败！");
            }
        }


    }

    //append headers into builder
    private void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }
}
















