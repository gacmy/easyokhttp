package com.gac.net.easy;

import com.gac.net.easy.request.EasyDownloadBuilder;
import com.gac.net.easy.request.EasyGetBuilder;
import com.gac.net.easy.request.EasyPostBuilder;
import com.gac.net.easy.response.DownloadResponseHandler;
import com.gac.net.easy.response.IResponseHandler;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by gacmy on 2018/4/22.
 * description:{
 * }
 */

public class EasyUtils {
    //get 请求
    public static void get(String url, Map<String,String> params, IResponseHandler handler){
        EasyGetBuilder builder = new EasyGetBuilder();
        builder.url(url).params(params).enqueue(handler);
    }
    //post 请求
    public static void post(String url,Map<String,String> params,IResponseHandler handler){
        EasyPostBuilder builder = new EasyPostBuilder();
        builder.url(url).params(params).enqueue(handler);
    }
    //post json参数请求 出入对象
    public static void postJson(String url,Object obj,IResponseHandler handler){
        EasyPostBuilder builder = new EasyPostBuilder();
        builder.url(url).jsonParams(new Gson().toJson(obj)).enqueue(handler);
    }

    public static void postJson(String url,String json,IResponseHandler handler){
        EasyPostBuilder builder = new EasyPostBuilder();
        builder.url(url).jsonParams(json).enqueue(handler);
    }

    public static void download(String url, String dir, String fileName, DownloadResponseHandler handler){
        new EasyDownloadBuilder().fileDir(dir).url(url).fileName(fileName).enqueue(handler);
    }
}
