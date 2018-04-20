package easyokhttp.easyokhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.gac.net.easy.request.EasyGetBuilder;
import com.gac.net.easy.response.GsonResponseHandler;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button bt_get;
    TextView tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_get = (Button)findViewById(R.id.bt_get);
        tv_content = (TextView)findViewById(R.id.tv_content);
        bt_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGet();
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
