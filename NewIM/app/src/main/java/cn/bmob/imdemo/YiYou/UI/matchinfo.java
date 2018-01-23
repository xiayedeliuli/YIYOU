package cn.bmob.imdemo.YiYou.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.LastInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by DELL on 2017/6/1.
 */

public class matchinfo extends BaseActivity {
    RelativeLayout realtimelocation;
    TextView matchitem;
    TextView matchspace;
    CircleImageView matchimg;
    TextView matchtime;
    TextView matchname;
    Button buttongo;
    Button arrive;
    Button cancel;
    boolean AorB ;
    ImageView matchsex;
    LinearLayout matchlay;
    LastInfo lastInfo;
    TextView zhengzaijingxing;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchinfo);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        final Intent intent=getIntent();
        AorB = intent.getStringExtra("hr").equals("A");
        lastInfo= (LastInfo) intent.getSerializableExtra("data");
        Toast.makeText(matchinfo.this,"点击下方按钮可以实现到达确认、撤销匹配或举报对方等功能",Toast.LENGTH_LONG).show();
        matchlay= (LinearLayout) findViewById(R.id.matchlay);
        zhengzaijingxing= (TextView) findViewById(R.id.zhengzaijinxing);
        matchsex= (ImageView) findViewById(R.id.matchsex);
        cancel= (Button) findViewById(R.id.cancel);
        arrive= (Button) findViewById(R.id.arrive);
        buttongo= (Button) findViewById(R.id.buttongo);
        matchname= (TextView) findViewById(R.id.matchname);
        matchtime= (TextView) findViewById(R.id.matchtime);
        matchimg= (CircleImageView) findViewById(R.id.matchinfoimg);
        matchitem= (TextView) findViewById(R.id.matchitem);
        matchspace= (TextView) findViewById(R.id.matchspace);
        realtimelocation= (RelativeLayout) findViewById(R.id.realtimelocantion);
        matchitem.setText(lastInfo.getLastItem());
        String space="南京财经大学"+lastInfo.getLastSpace()+"附近";
        matchspace.setText(space);
        Long chatime=System.currentTimeMillis()-1200000;
        if(lastInfo.getLastTime()<chatime){
            zhengzaijingxing.setText("已经结束");
        }else{
            zhengzaijingxing.setText("正在进行");
        }
        matchtime.setText(MyUtils.getStringDate2(lastInfo.getLastTime()));
        MyUtils.matchlay(matchlay,lastInfo.getLastItem());
        realtimelocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(zhengzaijingxing.getText().toString().equals("已经结束")){
                                toast("本次匹配已经结束啦！");
                            }else{
                                Intent  intent=new Intent();
                                intent.setClass(matchinfo.this,matchmap.class);
                                intent.putExtra("space",lastInfo.getLastSpace());
                                intent.putExtra("data",lastInfo);
                                intent.putExtra("hr",AorB);
                                intent.putExtra("xzf",lastInfo.getLastItem());
                                if(arrive.getText().toString().equals("我已到达")){
                                    intent.putExtra("isarrive",true);
                                }
                                if(arrive.getText().toString().equals("双方已到达")){
                                    intent.putExtra("isarrive",true);
                                }
                                startActivity(intent);
                            }
                        }
                    });
                    if(AorB){
                        if(lastInfo.getAarrive()){
                            arrive.setText("我已到达");
                        }
                        if(lastInfo.getBcancel()){
                            cancel.setText("对方已取消");
                        }
                        if(lastInfo.getBsex().equals("男")){
                            matchsex.setImageResource(R.drawable.boy);
                        }
                       new ImageLoader().showImageByThread(matchimg,lastInfo.getBavater());
                        matchname.setText(lastInfo.getBname());
                    }else{
                        if(lastInfo.getBarrive()){
                            arrive.setText("我已到达");
                        }
                        if(lastInfo.getAcancel()){
                            cancel.setText("对方已取消");
                        }
                        if(lastInfo.getAsex().equals("男")){
                            matchsex.setImageResource(R.drawable.boy);
                        }
                        new ImageLoader().showImageByThread(matchimg,lastInfo.getAavater());
                        matchname.setText(lastInfo.getAname());
                    }
        buttongo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zhengzaijingxing.getText().toString().equals("已经结束")){
                    toast("本次匹配已经结束啦！");
                }else{
                    Intent  intent=new Intent();
                    intent.setClass(matchinfo.this,matchmap.class);
                    intent.putExtra("space",lastInfo.getLastSpace());
                    intent.putExtra("data",lastInfo);
                    intent.putExtra("hr",AorB);
                    intent.putExtra("xzf",lastInfo.getLastItem());
                    if(arrive.getText().toString().equals("我已到达")){
                        intent.putExtra("isarrive",true);
                    }
                    if(arrive.getText().toString().equals("双方已到达")){
                        intent.putExtra("isarrive",true);
                    }
                    startActivity(intent);
                }
            }
        });
        matchimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(matchinfo.this,userinfomation.class);
                if(AorB){
                    intent.putExtra("objectid",lastInfo.getBID());
                }else{
                    intent.putExtra("objectid",lastInfo.getAID());
                }
                startActivity(intent);
            }
        });

        BmobQuery<LastInfo> query=new BmobQuery<>();
        query.getObject(lastInfo.getObjectId(), new QueryListener<LastInfo>() {
            @Override
            public void done(LastInfo lastInfo, BmobException e) {
                if(e==null){
                    if(AorB){
                        if(lastInfo.getAarrive()&lastInfo.getBarrive()){
                            arrive.setText("双方已到达");
                        }else if(lastInfo.getAarrive()&!lastInfo.getBarrive()){
                            arrive.setText("我已到达");
                        }else if(!lastInfo.getAarrive()&lastInfo.getBarrive()){
                            arrive.setText("对方已到达");
                        }
                        if(lastInfo.getAcancel()&lastInfo.getBcancel()){
                            cancel.setText("双方已取消");
                        }else if(lastInfo.getAcancel()&!lastInfo.getBcancel()){
                            cancel.setText("我已取消");
                        }else if(!lastInfo.getAcancel()&lastInfo.getBcancel()){
                            cancel.setText("对方已取消");
                        }
                    }else{
                        if(lastInfo.getAarrive()&lastInfo.getBarrive()){
                            arrive.setText("双方已到达");
                        }else if(lastInfo.getAarrive()&!lastInfo.getBarrive()){
                            arrive.setText("对方已到达");
                        }else if(!lastInfo.getAarrive()&lastInfo.getBarrive()){
                            arrive.setText("我已到达");
                        }
                        if(lastInfo.getAcancel()&lastInfo.getBcancel()){
                            cancel.setText("双方已取消");
                        }else if(lastInfo.getAcancel()&!lastInfo.getBcancel()){
                            cancel.setText("对方已取消");
                        }else if(!lastInfo.getAcancel()&lastInfo.getBcancel()){
                            cancel.setText("我已取消");
                        }
                    }
                }
            }
        });
    }
}
