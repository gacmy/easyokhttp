package com.gac.net.easy.response;


import okhttp3.Response;

/**
 * Created by gacmy on 2018/3/12.
 */

public interface IResponseHandler {
    void onSuccess(Response response);
    void onFailure(int statusCode, String errorMsg);
    void onProgress(long currentBytes, long totalBytes);
}
