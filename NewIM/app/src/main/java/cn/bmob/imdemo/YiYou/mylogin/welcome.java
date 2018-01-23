package cn.bmob.imdemo.YiYou.mylogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.YiYou.UI.mymainactivity;
import cn.bmob.imdemo.YiYou.UI.webview;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.imdemo.YiYou.bean.MessageKefu;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by DELL on 2017/4/15.
 */

public class welcome extends BaseActivity {
    public static SharedPreferences sp;
    private String objectid;
    ImageView welcome;
    boolean is;
    String id;
    String name;
    String avater;
    RelativeLayout r;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏与状态栏
        welcome.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        is=false;
        r= (RelativeLayout) findViewById(R.id.r);
        welcome= (ImageView) findViewById(R.id.welcome);
        //创建一个sharepreferfence存储用户的手机号码，唯一标号
        sp=getSharedPreferences("xzf",MODE_PRIVATE);
        //①如果是第一次，取到默认值：启动注册界面
        //②如果是第二次，取到手机号码，直接启动
        objectid=sp.getString("objetid11","error");
        //判断有无网络连接，无连接finish
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // 检查网络连接，如果无网络可用，就不需要进行连网操作等
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !manager.getBackgroundDataSetting()) {
            //无网络连接
            Toast.makeText(welcome.this,"请检查您的网络连接状态",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            BmobQuery<MessageKefu> query=new BmobQuery<>();
            query.getObject("5SPOWWWe", new QueryListener<MessageKefu>() {
                @Override
                public void done(final MessageKefu messageKefu, BmobException e) {
                    if(e==null){
                        if(null!=messageKefu.getUserimage()&null!=messageKefu.getUrl()){
                            if(messageKefu.getUserimage().length()>2&messageKefu.getUrl().length()>2){
                                r.setVisibility(View.VISIBLE);
                                new ImageLoader().showImageByThread(welcome,messageKefu.getUserimage());
                                welcome.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(messageKefu.getUrl().length()>0){
                                            is=true;
                                            Intent intent=new Intent();
                                            intent.setClass(cn.bmob.imdemo.YiYou.mylogin.welcome.this, webview.class);
                                            intent.putExtra("url",messageKefu.getUrl().toString());
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        }
                        if(null!=messageKefu.getText()){
                            if(messageKefu.getText().length()>2){
                                MyApplication.shareurl=messageKefu.getText();
                            }
                        }
                    }
                }
            });
            BmobQuery<MessageKefu> query1=new BmobQuery<>();
            query1.getObject("0c491b80de", new QueryListener<MessageKefu>() {
                @Override
                public void done(MessageKefu messageKefu, BmobException e) {
                    if(e==null){
                        id=messageKefu.getKefuid();
                        name=messageKefu.getKefuname();
                        avater=messageKefu.getKefuavater();
                        MyApplication.iskefu=messageKefu.getIskefu();
                        if(!messageKefu.getIskefu()){
                            MyApplication.url=messageKefu.getUrl();
                        }
                    }
                }
            });
            //有网络连接
            //在这个时间函数里判断是否第一次登陆
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if(null!=objectid){
                        if(objectid.equals("error")){
                            if(!is){
                                //启动注册界面
                                Intent intent=new Intent();
                                intent.setClass(welcome.this, login_0.class);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            if(!is){
                                Intent intent=new Intent();
                                intent.setClass(welcome.this,mymainactivity.class);
                                intent.putExtra("id",id);
                                intent.putExtra("name",name);
                                intent.putExtra("avater",avater);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            },3000);
        }

    }
}
