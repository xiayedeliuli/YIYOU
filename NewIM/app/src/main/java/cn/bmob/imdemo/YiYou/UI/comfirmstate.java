package cn.bmob.imdemo.YiYou.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.baidu.mapapi.model.LatLng;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * Created by DELL on 2017/6/1.
 */

public class comfirmstate extends BaseActivity {
    ImageView setback;
    private boolean firstin;
    private MapView mapView;
    BaiduMap mbitmap;
    RelativeLayout dingwei;
    String locationdescribe;
    TextView indexinfo;
    //权限码
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private LocationClient mlocationClient;
    private MyLocationListener mLoactionListener;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comfirmstate);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setback= (ImageView) findViewById(R.id.setback);
        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        indexinfo= (TextView) findViewById(R.id.indexinfo);
        final User user = BmobUser.getCurrentUser(User.class);
        indexinfo.setText(user.getIndex()+" 仙林校区");
        mapView= (MapView) findViewById(R.id.ditu);
        dingwei= (RelativeLayout) findViewById(R.id.dingwei);
        dingwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=user.getIndex()){
                    if(locationdescribe.contains(user.getIndex())){
                        //通过校区认证，更新验证状态，并跳转
                        comfirm();
                    }else{
                        Toast.makeText(comfirmstate.this,"您当前位置不在校区附近，请到达后重试",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        mbitmap=mapView.getMap();
        mbitmap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        MapStatusUpdate factory=MapStatusUpdateFactory.zoomTo(17.0f);
        mbitmap.setMapStatus(factory);
        //允许定位
        mbitmap.setMyLocationEnabled(true);


        //百度地图定位的首次进入标识
        firstin = true;
        //使用百度地图，安卓6.0需要动态获取权限
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            initlocation();
        }

    }

    //这是定位用到的监听器
    private class MyLocationListener implements BDLocationListener {
        //在监听器中获得用户地理位置，并设置初次进入时的定位中心点

        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            //记录经纬度
            //弹一个地址的面包片
            if (firstin) {
                firstin = false;
                LatLng latlng=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latlng);
                mbitmap.animateMapStatus(msu);
                locationdescribe=bdLocation.getLocationDescribe();
                Toast.makeText(comfirmstate.this, "" + bdLocation.getLocationDescribe(), Toast.LENGTH_LONG).show();
            }
            //这是用户位置数据,显示在地图上
            MyLocationData data=new MyLocationData.Builder()//
                    .accuracy(bdLocation.getRadius())//精度
                    .latitude(bdLocation.getLatitude())//纬度
                    .longitude(bdLocation.getLongitude())//经度
                    .build();
            mbitmap.setMyLocationData(data);

            Log.d("xz","监听中");



        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    //定位方法
    public void initlocation() {
        //在设置client的相关属性，绑定监听器并开启
        mlocationClient = new LocationClient(getApplicationContext());
        mLoactionListener = new MyLocationListener();
        //注册监听器
        mlocationClient.registerLocationListener(mLoactionListener);
        //以下是设置client的属性
        LocationClientOption option = new LocationClientOption();
        //坐标类型
        option.setCoorType("bd09ll");
        //返回当前位置
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        //打开GPS
        option.setOpenGps(true);
        //每过多久进行一次监听
        option.setScanSpan(3000);
        mlocationClient.setLocOption(option);
        //开启监听器
        if (!mlocationClient.isStarted()) {
            mlocationClient.start();
        }
    }

    //动态获取权限的方法
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(comfirmstate.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        } else {
            initlocation();
        }
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initlocation();
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationClient.unRegisterLocationListener(mLoactionListener);
    }



//    //这个方法用来验证输入的学号是否正确
//    private void httpcomfirm() throws UnsupportedEncodingException {
//        String loginUrl="http://210.28.81.11/default2.aspx";
//        OkHttpClient client = new OkHttpClient();
//        RequestBody builder=new FormBody.Builder()
//        .add("__VIEWSTATE", "dDwyODE2NTM0OTg7Oz59BfBdGVwXTJYGdIzOYwNIZiVDhA==")
//        .add("txtUserName", "2920140112")
//        .add("TextBox2", "b1132688174")
//        .add("txtSecretCode", "").add("RadioButtonList1", URLEncoder.encode("学生", "gbk"))
//        .add("Button1", "")
//        .add("hidPdrs", "")
//        .add("hidsc", "")
//        .add("lbLanguage", "").build();
//        final Request request = new Request.Builder()
//                .url(loginUrl)
//                .post(builder)
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String str = response.body().string();
//                int ss=response.code();
//                Log.d("url",""+str);
//                Log.d("url",""+ss);
//            }
//
//        });
//
//
//
//    }

    private void comfirm(){
        User user=new User();
        user.setObjectId(sp.getString("objetid11", ""));
        user.setConfirmState(true);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(comfirmstate.this,"验证成功，请上传一张本人真实照片",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent();
                    intent.setClass(comfirmstate.this,changehead.class);
                    intent.putExtra("changehead",false);
                    startActivity(intent);
                }
            }
        });
    }
}
