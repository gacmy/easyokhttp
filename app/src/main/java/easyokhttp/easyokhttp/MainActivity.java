package easyokhttp.easyokhttp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.gac.net.easy.request.EasyDownloadBuilder;
import com.gac.net.easy.request.EasyGetBuilder;
import com.gac.net.easy.request.EasyPostBuilder;

import com.gac.net.easy.request.EasyRequestBuilder;
import com.gac.net.easy.response.DownloadResponseHandler;
import com.gac.net.easy.response.GsonResponseHandler;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button bt_get;
    TextView tv_content;
    Button bt_https;
    Button bt_download;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_get = (Button)findViewById(R.id.bt_get);
        tv_content = (TextView)findViewById(R.id.tv_content);
        bt_https = (Button)findViewById(R.id.bt_https);
        bt_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGet();
            }
        });
        bt_https.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testHttps();
            }
        });
        bt_download = (Button)findViewById(R.id.bt_download);
        bt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDownloads();
            }
        });
        iv = (ImageView)findViewById(R.id.iv);

    }
    private void testDownloads(){
        new EasyDownloadBuilder().url("http://img55.it168.com/ArticleImages/fnw/2018/0420/3a9d2efd-74a6-4554-866e-d9c16aded091.jpg")
                .fileName("1.jpg")

                .fileDir(getApplicationContext().getCacheDir().getAbsolutePath())
                .enqueue(new DownloadResponseHandler() {

                    @Override
                    public void onFinish(File downloadFile) {
                        if(downloadFile != null){
                            Uri uri = Uri.fromFile(downloadFile);
                            iv.setImageURI(uri);
                        }

                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        Log.e("EasyOkhttp","currentBytes:"+currentBytes + " totalBytes:"+totalBytes);
                    }

                    @Override
                    public void onFailure(String error_msg) {

                    }
                });
    }
    private void testHttps(){
        new EasyPostBuilder().url("https://www.baidu.com").enqueue(new GsonResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, String response) {

            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {

            }
        });
    }

    private void testGet(){
        new EasyGetBuilder().url("http://v.juhe.cn/joke/content/list.php").addParam("sort","desc")
                .addParam("page","1").addParam("pageSize","20").addParam("key","4486e7f3ad2fb806622807a66bc0f492")
        .addParam("time",new Date().getTime()/1000+"").enqueue(new GsonResponseHandler<BaseData<GetDataModel>>() {
            @Override
            public void onSuccess(int statusCode, BaseData<GetDataModel> response) {
                Log.e("EasyOkhttp","content size:"+response.getResult().getData().size());
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {

            }
        });
    }
}
