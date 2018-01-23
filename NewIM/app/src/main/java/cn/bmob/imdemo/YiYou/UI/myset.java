package cn.bmob.imdemo.YiYou.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.bean.ManagementTable;
import cn.bmob.imdemo.YiYou.bean.MessageKefu;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by DELL on 2017/6/1.
 */

public class myset extends BaseActivity {
    RelativeLayout clear;
    RelativeLayout update;
    RelativeLayout joinus;
    ImageView setback;
    ProgressDialog pd;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myset);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        clear= (RelativeLayout) findViewById(R.id.clear);
        update= (RelativeLayout) findViewById(R.id.update);
        joinus= (RelativeLayout) findViewById(R.id.joinus);
        setback= (ImageView) findViewById(R.id.setback);
        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearUserInfo();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobQuery<MessageKefu> query=new BmobQuery<MessageKefu>();
                query.getObject("gCBXYYYk", new QueryListener<MessageKefu>() {
                    @Override
                    public void done(MessageKefu messageKefu, BmobException e) {
                        if(e==null){
                            if(null!=messageKefu.getUrl()){
                                if(messageKefu.getUrl().length()>2){
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri content_url = Uri.parse(messageKefu.getUrl());
                                    intent.setData(content_url);
                                    startActivity(intent);
                                }else{
                                    toast("敬请期待");
                                }
                            }else{
                                toast("敬请期待");
                            }
                        }
                    }
                });
            }
        });
        joinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               BmobQuery<MessageKefu> query=new BmobQuery<MessageKefu>();
                query.getObject("pLuUSSSz", new QueryListener<MessageKefu>() {
                    @Override
                    public void done(MessageKefu messageKefu, BmobException e) {
                        if(e==null){
                            if(null!=messageKefu.getUrl()){
                                if(messageKefu.getUrl().length()>2){
                                    Intent intent=new Intent();
                                    intent.setClass(myset.this, webview.class);
                                    intent.putExtra("url",messageKefu.getUrl().toString());
                                    startActivity(intent);
                                }else{
                                    toast("敬请期待");
                                }
                            }else{
                                toast("敬请期待");
                            }
                        }
                    }
                });
            }
        });


    }

    //清楚数据的方法
    private void clearUserInfo(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("用户数据将清楚。再次使用需重新注册,是否确定");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pd=new ProgressDialog(myset.this);
                pd.setTitle("任务执行中");
                pd.setMessage("正在进行，请稍候");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);//在setProgressStyle属性里可以设置圆形框和进度条
                pd.setCancelable(true);
                pd.show();
                final SharedPreferences sp=getSharedPreferences("xzf",MODE_PRIVATE);
                final String objectid=sp.getString("objetid11","error");
                BmobQuery<ManagementTable> userBmobQuery=new BmobQuery<ManagementTable>();
                userBmobQuery.getObject(sp.getString("manage","error"), new QueryListener<ManagementTable>() {
                    @Override
                    public void done(ManagementTable user, BmobException e) {
                        if(e==null){
                            if(user.getBeoff()){
                                pd.cancel();
                                toast("您的账户已被封禁，无权清楚用户数据，请联系客服处理");
                            }else if(user.getDongjie()){
                                pd.cancel();
                                toast("您的账户已被冻结，无权清楚用户数据，请联系客服处理");
                            }else{
                                final User user2=new User();
                                user2.delete(objectid,new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            MyUser user1=new MyUser();
                                            user1.setAreJianting(true);
                                            user1.setObjectId(sp.getString("userMateID",""));
                                            user1.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        SharedPreferences.Editor editor=sp.edit();
                                                        editor.remove("objetid11");
                                                        editor.remove("userMateID");
                                                        editor.remove("SecondChoice");
                                                        editor.remove("manage");
                                                        editor.remove("isfirstin");
                                                        editor.apply();
                                                        pd.cancel();
                                                        Toast.makeText(myset.this,"数据已清楚成功,请重启应用",Toast.LENGTH_LONG).show();
                                                    }else{
                                                        Toast.makeText(myset.this,"失败："+e.getMessage()+","+e.getErrorCode(),Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(myset.this,"失败："+e.getMessage()+","+e.getErrorCode(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                            }
                        }
                    }
                });

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

}
