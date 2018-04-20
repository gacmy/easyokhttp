package com.gac.net.easy.response;

import java.io.File;
public abstract class DownloadResponseHandler {

    public void onStart(long totalBytes) {

    }

    public void onCancel() {

    }

    public abstract void onFinish(File downloadFile);
    public abstract void onProgress(long currentBytes, long totalBytes);
    public abstract void onFailure(String error_msg);
}