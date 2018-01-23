package cn.bmob.imdemo.YiYou.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.MessageKefu;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by DELL on 2017/6/1.
 */

public class Integraltask extends BaseActivity {
    ImageView imgjf;
    RelativeLayout go4;
    RelativeLayout go2;
    RelativeLayout go3;
    TextView v2;
    TextView mywallet;
    ImageView dhsetback;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integraltask);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        imgjf= (ImageView) findViewById(R.id.imgjf);
        dhsetback= (ImageView) findViewById(R.id.dhsetback);
        dhsetback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        go4= (RelativeLayout) findViewById(R.id.go4);
        go2= (RelativeLayout) findViewById(R.id.go2);
        go3= (RelativeLayout) findViewById(R.id.go3);
        v2= (TextView) findViewById(R.id.v2);
        mywallet= (TextView) findViewById(R.id.mywallet);
        final User user = BmobUser.getCurrentUser(User.class);
        mywallet.setText("我的积分： "+user.getMywallet());
        go4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送一个广播，取消原来主界面的可能存在的匹配任务
                Intent intent1 = new Intent("android.text_change");
                intent1.putExtra("text", "关闭任务");
                sendBroadcast(intent1);

                Intent intent=new Intent();
                intent.setClass(Integraltask.this,mymainactivity.class);
                startActivity(intent);
            }
        });
        go2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = BmobUser.getCurrentUser(User.class);
                if(user.isConfirmState()&user.getRealavatar().length()>0
                        &user.getStudentid().length()>0&user.getStudentkey().length()>0){
                    Toast.makeText(Integraltask.this, "您已填写过验证资料", Toast.LENGTH_SHORT).show();
                }else if(user.isConfirmState()) {
                    Intent intent4 = new Intent();
                    intent4.setClass(Integraltask.this, changehead.class);
                    intent4.putExtra("changehead",false);
                    startActivity(intent4);
                }else if(!user.isConfirmState()){
                    Intent intent4 = new Intent();
                    intent4.setClass(Integraltask.this, comfirmstate.class);
                    startActivity(intent4);
                }
            }
        });
        go3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("请务必让好友在注册时填写您的邀请码，邀请码可以在分享给好友的内容中查看");
                MyUtils.showShare(getApplicationContext());
            }
        });
        imgjf.setImageResource(R.mipmap.integraltask);
        BmobQuery<MessageKefu> query=new BmobQuery<>();
        query.getObject("6bieJJJb", new QueryListener<MessageKefu>() {
            @Override
            public void done(final MessageKefu messageKefu, final BmobException e) {
                if(e==null){
                    if(null!=messageKefu.getUserimage()){
                        if(messageKefu.getUserimage().length()>0&messageKefu.getUrl().length()>0){
                            new ImageLoader().showImageByThread(imgjf,messageKefu.getUserimage());
                            imgjf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent();
                                    intent.setClass(Integraltask.this, webview.class);
                                    intent.putExtra("url",messageKefu.getUrl().toString());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }
            }
        });

    }
}
