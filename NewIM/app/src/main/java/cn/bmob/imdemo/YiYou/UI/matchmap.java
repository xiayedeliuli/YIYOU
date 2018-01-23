package cn.bmob.imdemo.YiYou.UI;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.LastInfo;
import cn.bmob.imdemo.YiYou.bean.complain;
import cn.bmob.imdemo.ui.ChatActivity;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by DELL on 2017/6/1.
 */

public class matchmap extends BaseActivity {
    private MapView mMapView;
    BaiduMap mbitmap;
    private double mlattitude;
    private double mlongtitude;
    private boolean isfirstin = true;
    LinearLayout matchmaplay;
    CountdownView count;
    List<LatLng> pts;
    RelativeLayout top;
    FrameLayout fra;
    RelativeLayout relay;
    boolean isIsfirstin;
    RelativeLayout arrive;
    private LocationClient mlocationClient;
    private MyLocationListener mLoactionListener;
    ImageView mylocation;
    RelativeLayout circle;
    boolean AorB ;
    LastInfo lastInfo;
    ImageView mysetmap;
    TextView mytexs;
    TextView julipipei;
    RelativeLayout location;
    RelativeLayout phone3;
    RelativeLayout news;


    protected void onCreate(Bundle savedInstanceState) {
        //去除标题栏与状态栏
        matchmap.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchmap);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        phone3= (RelativeLayout) findViewById(R.id.phone3);
        news= (RelativeLayout) findViewById(R.id.news);
        location= (RelativeLayout) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latlng=pts.get(0);
                MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latlng);
                mbitmap.animateMapStatus(msu);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String objectid;
                if(AorB){
                    objectid=lastInfo.getBID();
                }else{
                    objectid=lastInfo.getAID();
                }
                BmobQuery<User> query=new BmobQuery<User>();
                query.getObject(objectid, new QueryListener<User>() {
                    @Override
                    public void done(User myUse, BmobException e) {
                        if(e==null){
                            BmobIMUserInfo MateInfo = new BmobIMUserInfo(myUse.getObjectId(), myUse.getUsername(), myUse.getAvatar());
                            BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(MateInfo, false, null);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("c", c);
                            startActivity(ChatActivity.class, bundle, false);
                        }
                    }
                });

            }
        });
        phone3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String objectid;
                if(AorB){
                    objectid=lastInfo.getBID();
                }else{
                    objectid=lastInfo.getAID();
                }
                BmobQuery<User> query=new BmobQuery<User>();
                query.getObject(objectid, new QueryListener<User>() {
                    @Override
                    public void done(User myUse, BmobException e) {
                        if(e==null){
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + myUse.getMobilePhoneNumber());
                            intent.setData(data);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        //实时监测是否已经取消
        final Intent intent=getIntent();
        lastInfo= (LastInfo) intent.getSerializableExtra("data");
        BmobQuery<LastInfo> query=new BmobQuery<>();
        query.getObject(lastInfo.getObjectId(), new QueryListener<LastInfo>() {
            @Override
            public void done(LastInfo lastInfo, BmobException e) {
               if(e==null) {
                   if (lastInfo.getDELETE()) {
                       finish();
                       toast("本次匹配已经取消");
                   }
               }
            }
        });
        isIsfirstin=true;
        mylocation= (ImageView) findViewById(R.id.mylocation);
        count= (CountdownView) findViewById(R.id.count);
        relay= (RelativeLayout) findViewById(R.id.relay);
        fra= (FrameLayout) findViewById(R.id.fra);
        julipipei= (TextView) findViewById(R.id.julipipei);
        matchmaplay= (LinearLayout) findViewById(R.id.matchmaptoplay);
        mytexs= (TextView) findViewById(R.id.mytexs);
        arrive= (RelativeLayout) findViewById(R.id.arrive);
        circle= (RelativeLayout) findViewById(R.id.circle);
        top= (RelativeLayout) findViewById(R.id.top);
        mMapView = (MapView) findViewById(R.id.bmapview);
        mysetmap= (ImageView) findViewById(R.id.mysetmap);
        if(getIntent().getBooleanExtra("isarrive",false)){
            circle.setBackgroundResource(R.drawable.layout_circlr_success);
            mytexs.setText("确认成功");
            circle.setEnabled(false);
        }
        AorB = intent.getBooleanExtra("hr",false);
        Log.d("xzf9",AorB+"");
        final Long time=lastInfo.getLastTime()-900000;
        if(System.currentTimeMillis()<time){
            //匹配尚未开始
            count.setVisibility(View.GONE);
            julipipei.setText("匹配尚未开始");
        }else{
            count.setVisibility(View.VISIBLE);
            count.start(lastInfo.getLastTime()-System.currentTimeMillis());
            julipipei.setText("距离剩余时间");
        }

        pts=MyUtils.getlatlang(getIntent().getStringExtra("space"));
        top.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                                       int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                                       fra.measure(w, h);
                                       int height = fra.getMeasuredHeight();
                                       if (isIsfirstin) {
                                           final ObjectAnimator anim4 = ObjectAnimator.ofFloat(fra, "translationY", 0f, height);
                                           anim4.setDuration(500);
                                           anim4.start();
                                           final ObjectAnimator anim5 = ObjectAnimator.ofFloat(relay, "translationY", 0f, height);
                                           anim5.setDuration(500);
                                           anim5.start();
                                           ObjectAnimator anim3 = ObjectAnimator.ofFloat(top, "rotationX", 0, 180);
                                           anim3.setDuration(500);
                                           anim3.start();
                                           ObjectAnimator anim2 = ObjectAnimator.ofFloat(location, "translationY", 0f, height);
                                           anim2.setDuration(500);
                                           anim2.start();
                                           ObjectAnimator anim1 = ObjectAnimator.ofFloat(news, "translationY", 0f, height);
                                           anim1.setDuration(500);
                                           anim1.start();
                                           ObjectAnimator anim0 = ObjectAnimator.ofFloat(phone3, "translationY", 0f, height);
                                           anim0.setDuration(500);
                                           anim0.start();
                                           isIsfirstin = false;
                                       } else {
                                           final ObjectAnimator anim4 = ObjectAnimator.ofFloat(fra, "translationY", height, 0f);
                                           anim4.setDuration(500);
                                           anim4.start();
                                           final ObjectAnimator anim5 = ObjectAnimator.ofFloat(relay, "translationY", height, 0f);
                                           anim5.setDuration(500);
                                           anim5.start();
                                           ObjectAnimator anim3 = ObjectAnimator.ofFloat(top, "rotationX", 180, 360);
                                           anim3.setDuration(500);
                                           anim3.start();
                                           ObjectAnimator anim2 = ObjectAnimator.ofFloat(location, "translationY", height, 0f);
                                           anim2.setDuration(500);
                                           anim2.start();
                                           ObjectAnimator anim1 = ObjectAnimator.ofFloat(news, "translationY", height, 0f);
                                           anim1.setDuration(500);
                                           anim1.start();
                                           ObjectAnimator anim0 = ObjectAnimator.ofFloat(phone3, "translationY", height, 0f);
                                           anim0.setDuration(500);
                                           anim0.start();
                                           isIsfirstin = true;
                                       }
                                   }
                               });

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng=new LatLng(mlattitude,mlongtitude);
                //判断是否在多边形区域内
                boolean ty=com.baidu.mapapi.utils.SpatialRelationUtil.isPolygonContainsPoint(pts,latLng);
                if(ty){
                    final Long nowtime=System.currentTimeMillis()-lastInfo.getLastTime();
                    if(Math.abs(nowtime)<900000){
                        Toast.makeText(matchmap.this,"恭喜您确认成功",Toast.LENGTH_SHORT).show();
                        circle.setBackgroundResource(R.drawable.layout_circlr_success);
                        mytexs.setText("确认成功");
                        LastInfo la=new LastInfo();
                        if(AorB){
                            la.setAarrive(true);
                        }else{
                            la.setBarrive(true);
                        }
                        la.update(lastInfo.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    if(!lastInfo.getJXpipei()){
                                        //获得一积分
                                        toast("恭喜您确认成功,获得1积分");
                                        final String objectid;
                                        if(AorB){
                                            objectid=lastInfo.getAID();
                                        }else{
                                            objectid=lastInfo.getBID();
                                        }
                                        //获取积分的方法
                                        incremejifen(objectid);
                                    }
                                }else{
                                    circle.setBackgroundResource(R.drawable.layout_circlr_nopress);
                                    mytexs.setText("我已到达");
                                    Toast.makeText(matchmap.this,"系统问题，请重新点击"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(matchmap.this,"请在约定时间前后15分钟内点击",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(matchmap.this,"请您到达黄色指定区域内确认",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mysetmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightydialog();
            }
        });
        arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mbitmap = mMapView.getMap();
        //基础地图
        mbitmap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //50米
        MapStatusUpdate factory = MapStatusUpdateFactory.zoomTo(18.0f);
        mbitmap.setMapStatus(factory);

        //允许定位
        mbitmap.setMyLocationEnabled(true);
      //构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(4, 0xAA00FF00))
                .fillColor(0xAAFFFF00);
      //在地图上添加多边形Option，用于显示
        mbitmap.addOverlay(polygonOption);
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation;
                animation=new RotateAnimation(0.0f, 360.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(500);
                mylocation.clearAnimation();
                mylocation.startAnimation(animation);

                //回到我的位置
                LatLng latlng=new LatLng(mlattitude,mlongtitude);
                MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latlng);
                mbitmap.animateMapStatus(msu);
            }
        });
        initlocation();
    }

   private class MyLocationListener implements BDLocationListener {
        //在监听器中获得用户地理位置，并设置初次进入时的定位中心点

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //这是用户位置数据
            MyLocationData data=new MyLocationData.Builder()//
                    .accuracy(0)//精度
                    .latitude(bdLocation.getLatitude())//纬度
                    .longitude(bdLocation.getLongitude())//经度
                    .build();
            mbitmap.setMyLocationData(data);
            //记录经纬度
            mlattitude=bdLocation.getLatitude();
            mlongtitude=bdLocation.getLongitude();
            if(isfirstin){
                //下面三行代码是使地图的中心点在我的位置，即我定位的经纬度
                //传入经度和纬度
                LatLng latlng=new LatLng(mlattitude,mlongtitude);
                MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latlng);
                mbitmap.animateMapStatus(msu);
                isfirstin=false;
//                Toast.makeText(matchmap.this,""+bdLocation.getLocationDescribe(),Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public void initlocation(){
        //在设置client的相关属性，绑定监听器并开启
        mlocationClient=new LocationClient(getApplicationContext());
        mLoactionListener=new MyLocationListener();
        //注册监听器
        mlocationClient.registerLocationListener(mLoactionListener);
        //以下是设置client的属性
        LocationClientOption option=new LocationClientOption();
        //坐标类型
        option.setCoorType("bd09ll");
        //返回当前位置
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        //打开GPS
        option.setOpenGps(true);
        //每过多久进行一次监听
        option.setScanSpan(2000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 高精度
        mlocationClient.setLocOption(option);
        //开启监听器
        if(!mlocationClient.isStarted()){
            mlocationClient.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!mlocationClient.isStarted()){
            mlocationClient.unRegisterLocationListener(mLoactionListener);
            mlocationClient.stop();
        }

    }

    private void rightydialog(){
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.show();
        Window window=dialog.getWindow();
        if(window!=null){
            window.setContentView(R.layout.rightydialog);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.AnimBottom);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.findViewById(R.id.quxiao).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            window.findViewById(R.id.jubao).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    jubaodialog();
                }
            });
            window.findViewById(R.id.qiehuan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    if(mbitmap.getMapType()==BaiduMap.MAP_TYPE_NORMAL){
                        mbitmap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    }else{
                        mbitmap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    }
                }
            });
            window.findViewById(R.id.chexiao).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    BmobQuery<LastInfo> query=new BmobQuery<>();
                    query.getObject(lastInfo.getObjectId(), new QueryListener<LastInfo>() {
                        @Override
                        public void done(LastInfo lastInfo, BmobException e) {
                            if(e==null){
                                Long nowtime=System.currentTimeMillis();
                                Long lasttime=lastInfo.getLastTime();
                                    if(AorB){
                                        if(lastInfo.getBcancel()){
                                            //删除数据.对方已取消
                                            LastInfo lastIn=new LastInfo();
                                            lastIn.setDELETE(true);
                                            lastIn.update(lastInfo.getObjectId(), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        finish();
                                                        Toast.makeText(matchmap.this,"取消成功",Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(matchmap.this,"系统问题，请点击重试",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            //对方未取消
                                            LastInfo lastI=new LastInfo();
                                            lastI.setAcancel(true);
                                            lastI.update(lastInfo.getObjectId(),new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        Toast.makeText(matchmap.this,"您已取消，请等待对方取消，若双方未全部取消，则匹配仍成立",Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(matchmap.this,"系统问题，请点击重试",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }else{
                                        if(lastInfo.getAcancel()){
                                            //删除数据.对方已取消
                                            LastInfo lastIn=new LastInfo();
                                            lastIn.setDELETE(true);
                                            lastIn.update(lastInfo.getObjectId(), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        finish();
                                                        Toast.makeText(matchmap.this,"取消成功",Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(matchmap.this,"系统问题，请点击重试",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            //对方未取消
                                            LastInfo lastI=new LastInfo();
                                            lastI.setBcancel(true);
                                            lastI.update(lastInfo.getObjectId(),new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        Toast.makeText(matchmap.this,"您已取消，请等待对方取消，若双方未全部取消，则匹配仍成立",Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(matchmap.this,"系统问题，请点击重试",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }

                                    }

                            }else{
                                Toast.makeText(matchmap.this,"系统出了点小状况，请稍微重试9"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

        }
    }

    private void jubaodialog(){
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.show();
        Window window=dialog.getWindow();
        if(window!=null){
            window.setContentView(R.layout.jubaodialog);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.AnimBottom);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.findViewById(R.id.quxiao2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            window.findViewById(R.id.complain1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    BmobQuery<LastInfo> query=new BmobQuery<>();
                    query.getObject(lastInfo.getObjectId(), new QueryListener<LastInfo>() {
                        @Override
                        public void done(LastInfo lastInfo, BmobException e) {
                            if(e==null){
                                Long nowtime=System.currentTimeMillis();
                                if((lastInfo.getLastTime()-nowtime)>900000){
                                    Toast.makeText(matchmap.this,"匹配尚未开始，请耐心等候",Toast.LENGTH_SHORT).show();
                                }else if(Math.abs((lastInfo.getLastTime()-nowtime))<900000){
                                    Toast.makeText(matchmap.this,"尚未超出约定时间，请耐心等候",Toast.LENGTH_SHORT).show();
                                }else if((nowtime-lastInfo.getLastTime())>900000){
                                    Toast.makeText(matchmap.this,"您已投诉成功，客服稍后介入处理",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(matchmap.this,"系统出了点小状况，请稍微重试10"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            });
            window.findViewById(R.id.complain2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    complain("照片、资料作假");
                }
            });
            window.findViewById(R.id.complain3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    complain("诈骗/托");
                }
            });
            window.findViewById(R.id.complain4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    complain("恶意骚扰，不文明语言");
                }
            });



        }
    }

    private void complain(final String text){
        BmobQuery<LastInfo> query=new BmobQuery<>();
        query.getObject(lastInfo.getObjectId(), new QueryListener<LastInfo>() {
            @Override
            public void done(LastInfo lastInfo, BmobException e) {
                if(e==null){
                    if(AorB){
                        complain complain = new complain();
                        complain.setType("投诉");
                        complain.setUSERID(lastInfo.getAID());
                        complain.setUSERimage(lastInfo.getAavater());
                        complain.setUSERname(lastInfo.getAname());
                        complain.setWascomplainedUSERID(lastInfo.getBID());
                        complain.setWascomplainedAvater(lastInfo.getBavater());
                        complain.setCompliainTEXT(text);
                        complain.setCompleted(false);
                        complain.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    Toast.makeText(matchmap.this,"您已投诉成功，客服稍后介入处理",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(matchmap.this,"11系统出了点小状况，请稍候重试"+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        complain complain = new complain();
                        complain.setType("投诉");
                        complain.setCompleted(false);
                        complain.setUSERimage(lastInfo.getBavater());
                        complain.setUSERname(lastInfo.getBname());
                        complain.setUSERID(lastInfo.getBID());
                        complain.setWascomplainedUSERID(lastInfo.getAID());
                        complain.setWascomplainedAvater(lastInfo.getAavater());
                        complain.setCompliainTEXT(text);
                        complain.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    Toast.makeText(matchmap.this,"您已投诉成功，客服稍后介入处理",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(matchmap.this,"11系统出了点小状况，请稍候重试"+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            }
        });

    }

    private void incremejifen(final String objectid){

        BmobQuery<User> userBmobQuery=new BmobQuery<User>();
        userBmobQuery.getObject(objectid, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null) {
                    if(user.getAweek()>0){
                       jifen(objectid);
                    }else{
                        toast("增加积分失败，一周最多通过匹配获得7积分");
                    }
                }
            }
        });

    }

    private void jifen(final String objectid){
        User user1=new User();
        user1.increment("mywallet");
        user1.increment("aweek",-1);
        user1.update(objectid, new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }





}
