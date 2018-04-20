package com.gac.net.easy.response;

import okhttp3.Response;

/**
 * Created by gacmy on 2018/4/19.
 * description:{
 * }
 */

public abstract class CommonResponseHandler implements IResponseHandler {
    @Override
    public void onSuccess(Response response){

    }

    @Override
    public void onFailure(int statusCode, String errorMsg) {

    }

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
