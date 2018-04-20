package com.gac.net.easy.request;

import com.gac.net.easy.EasyOkHttp;
import com.gac.net.easy.body.ProgressRequestBody;
import com.gac.net.easy.callback.EasyCallback;
import com.gac.net.easy.response.IResponseHandler;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by gacmy on 2018/3/13.
 */

public class EasyUploadBuilder extends EasyRequestBuilder<EasyUploadBuilder>{
    private Map<String, File> mFiles;
    private List<MultipartBody.Part> mExtraParts;

    public EasyUploadBuilder() {
        super(new EasyOkHttp());
    }

    /**
     * add upload files
     * @param files files
     * @return
     */
    public EasyUploadBuilder files(Map<String, File> files) {
        this.mFiles = files;
        return this;
    }

    /**
     * add one upload file
     * @param key file key
     * @param file file
     * @return
     */
    public EasyUploadBuilder addFile(String key, File file) {
        if (this.mFiles == null)
        {
            mFiles = new LinkedHashMap<>();
        }
        mFiles.put(key, file);
        return this;
    }

    /**
     * add one upload file
     * @param key file key
     * @param fileName file name
     * @param fileContent byte[] file content
     * @return
     */
    public EasyUploadBuilder addFile(String key, String fileName, byte[] fileContent) {
        if(this.mExtraParts == null) {
            this.mExtraParts = new ArrayList<MultipartBody.Part>();
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), fileContent);
        this.mExtraParts.add(MultipartBody.Part.create(Headers.of("Content-Disposition",
                "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                fileBody));
        return this;
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        try {
            if(mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }

            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            appendParams(multipartBuilder, mParams);
            appendFiles(multipartBuilder, mFiles);
            appendParts(multipartBuilder, mExtraParts);
            builder.post(new ProgressRequestBody(multipartBuilder.build(),responseHandler));
            Request request = builder.build();
            mClient.getOkHttpClient().newCall(request).enqueue(new EasyCallback(responseHandler,"","","",""));
        } catch (Exception e) {
            //LogUtils.e("Upload enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }

    //append params into MultipartBody builder
    private void appendParams(MultipartBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    //append files into MultipartBody builder
    private void appendFiles(MultipartBody.Builder builder, Map<String, File> files) {
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
    }

    //appends parts into MultipartBody builder
    private void appendParts(MultipartBody.Builder builder, List<MultipartBody.Part> parts) {
        if(parts != null && parts.size() > 0) {
            for(int i=0; i < parts.size(); i++) {
                builder.addPart(parts.get(i));
            }
        }
    }

    //获取mime type
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
