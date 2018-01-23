package cn.bmob.imdemo.YiYou.UI;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.orhanobut.logger.Logger;
import com.yalantis.ucrop.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.db.NewFriendManager;
import cn.bmob.imdemo.event.RefreshEvent;
import cn.bmob.imdemo.model.UserModel;
import cn.bmob.imdemo.YiYou.ChangeFragment;
import cn.bmob.imdemo.YiYou.List.DrawerLayoutList;
import cn.bmob.imdemo.YiYou.Myservice;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.LastInfo;
import cn.bmob.imdemo.YiYou.bean.ListInfo;
import cn.bmob.imdemo.YiYou.bean.ManagementTable;
import cn.bmob.imdemo.YiYou.bean.MessageKefu;
import cn.bmob.imdemo.YiYou.bean.pinglun;
import cn.bmob.imdemo.YiYou.fragment.dongtaifragment;
import cn.bmob.imdemo.YiYou.Mate.MateFragment;
import cn.bmob.imdemo.YiYou.fragment.fragment2;
import cn.bmob.imdemo.YiYou.fragment.fragment3;
import cn.bmob.imdemo.YiYou.myadapter.MyDrawlayoutAdapter;
import cn.bmob.imdemo.YiYou.mylogin.MyApplication;
import cn.bmob.imdemo.ui.ChatActivity;
import cn.bmob.imdemo.ui.fragment.ContactFragment;
import cn.bmob.imdemo.ui.fragment.ConversationFragment;
import cn.bmob.imdemo.util.IMMLeaks;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * Created by DELL on 2017/5/20.
 */

public class mymainactivity extends BaseActivity implements View.OnTouchListener,OnClickListener{
    private ConversationFragment conversationFragment;
    private MateFragment fragment1;
    private cn.bmob.imdemo.YiYou.fragment.fragment3 fragment3;
    private cn.bmob.imdemo.YiYou.fragment.fragment2 fragment2;
    private ContactFragment contactFragment;
    private ListView listView;
    private DrawerLayout drawerLayout;
    //下面是手势侧滑需要使用的，先提前声明了
    private GestureDetector gestureDetector;
    private int FLING_MIN_DISTANCE = 100;
    private int FLING_MIN_VELOCITY = 200;
    private ImageView backg;
    private cn.bmob.imdemo.YiYou.fragment.dongtaifragment dongtaifragment;
    private TextView pipeitx;
    private TextView duorentx;
    private TextView dongtaitx;
    private TextView xiaoxitx;
    private RelativeLayout container;


    private boolean isright;
    //动态注册广播接收器
    private IntentFilter intentFilter;
    private BroadReceiver broadReceiver;
    LinearLayout pipeilay;
    LinearLayout xiaoxilay;
    LinearLayout dongtailay;
    RelativeLayout mylay;
    ImageView pipei;
    ImageView dongtai;
    ImageView xiaoxi;
    ImageView my;
    CircleImageView xiaoyuandian;
    Boolean firstin;
    Boolean panduan=false;
    Boolean renshupanduan=false;
    private ChangeFragment changeFragmentinterface;





    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化百度的SDK
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.drawlayout);
        MyApplication.currentfragment = new Fragment();
        //状态栏透明
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        final User user = BmobUser.getCurrentUser(User.class);
        //检测下有没有被封号
        BmobQuery<ManagementTable> query = new BmobQuery<ManagementTable>();
        query.getObject(sp.getString("manage", ""), new QueryListener<ManagementTable>() {
            @Override
            public void done(ManagementTable user1, BmobException e) {
                if (e == null) {
                    if (user1.getBeoff()) {
                        finish();
                        toast("您的账号已被封禁，有任何问题请联系QQ2759143677");
                    } else{
                        if(user1.getDongjie()){
                            MyApplication.dongjie=true;
                        }else{
                            MyApplication.dongjie=false;
                        }
                        if(user1.getStudentcomfirm()){
                            MyApplication.studentcomfirm=true;
                        }else{
                            MyApplication.studentcomfirm=false;
                        }
                        onloginclick(user.getUsername());
                    }
                }
            }
        });
        //监测下有没有违约
        final Long date=System.currentTimeMillis()-1200000;
        BmobQuery<LastInfo> peq1 = new BmobQuery<LastInfo>();
        peq1.addWhereEqualTo("AID", user.getObjectId());
        peq1.addWhereLessThan("LastTime",date);
        peq1.addWhereNotEqualTo("isDELETE",true);
        BmobQuery<LastInfo> peq2 = new BmobQuery<LastInfo>();
        peq2.addWhereEqualTo("BID", user.getObjectId());
        peq2.addWhereLessThan("LastTime",date);
        peq2.addWhereNotEqualTo("isDELETE",true);
        List<BmobQuery<LastInfo>> pqueries = new ArrayList<BmobQuery<LastInfo>>();
        pqueries.add(peq1);
        pqueries.add(peq2);
        BmobQuery<LastInfo> pmainQuery = new BmobQuery<LastInfo>();
        pmainQuery.or(pqueries);
        pmainQuery.findObjects(new FindListener<LastInfo>() {
            @Override
            public void done(final List<LastInfo> list, BmobException e) {
                if(e==null){
                    Boolean first=true;
                     for(LastInfo la:list){
                         if(la.getAID().equals(user.getObjectId())){
                             if(!la.getAarrive()&first){
                                 dongjiezhanghao();
                                 first=false;
                                 panduan=true;
                             }
                         }else{
                             if(!la.getBarrive()&first){
                                 dongjiezhanghao();
                                 first=false;
                                 panduan=true;
                             }
                         }
                     }
                }
            }
        });

            Log.d("xzf","在线人数加1");
            firstin=true;
            MessageKefu messageKefu=new MessageKefu();
            messageKefu.increment("nownumber",1);
            messageKefu.update("Mw4Hcccm", new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e!=null){
                        Log.d("xzf",""+e.getErrorCode()+e.getMessage());
                    }
                }
            });



        queryFragment2(false);
        querydongtaiFragment();
        //这个判断值用来确定是否要刷新fragment
        MyApplication.shuaxin = false;
        //控制是否开启匹配任务
        MyApplication.StartMatch = false;
        //控制匹配成功是否允许跳转
        MyApplication.MatchSuccessed = true;
        //这个判断值是判断用户进入的聊天界面是匹配的还是正常好友的
        MyApplication.isMateChatActivity = false;
        isright = true;
        MyApplication.isshowtx2=false;
        MyApplication.ischatnow=false;
        MyApplication.isblack=true;
        MyApplication.huihua=false;
        MyApplication.aremills=false;
        //动态注册广播接收器
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.text_change");
        broadReceiver = new BroadReceiver();
        registerReceiver(broadReceiver, intentFilter);
        pipei= (ImageView) findViewById(R.id.pipei);
        dongtai= (ImageView) findViewById(R.id.dongtai);
        xiaoxi= (ImageView) findViewById(R.id.xiaoxi);
        my= (ImageView) findViewById(R.id.my);
        pipeitx = (TextView) findViewById(R.id.pipeitx);
        duorentx = (TextView) findViewById(R.id.duorentx);
        dongtaitx = (TextView) findViewById(R.id.dongtaitx);
        xiaoxitx = (TextView) findViewById(R.id.xiaoxitx);
        pipeilay= (LinearLayout) findViewById(R.id.pipeilay);
        xiaoxilay= (LinearLayout) findViewById(R.id.xiaoxilay);
        dongtailay= (LinearLayout) findViewById(R.id.dongtailay);
        mylay= (RelativeLayout) findViewById(R.id.mylay);
        xiaoyuandian= (CircleImageView) findViewById(R.id.xiaoyuandian);
        container= (RelativeLayout) findViewById(R.id.fragment_container);
        changeFragmentinterface=new ChangeFragment() {
            @Override
            public void change(int flag) {
                switch (flag){
                    case R.id.tv_title1:
                        contactfragment();
                        break;
                    case R.id.tv_title:
                        setConversationFragment();
                        break;
                    case R.id.tv_tongzhi:
                        fragment5();
                        break;
                    case R.id.opendrawlayout_img:
                        drawerLayout.openDrawer(listView);
                        break;
                }
            }
        };

        pipei.setEnabled(false);
        mylay.setOnClickListener(this);
        xiaoxilay.setOnClickListener(this);
        dongtailay.setOnClickListener(this);
        pipeilay.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawlayout);
        listView = (ListView) findViewById(R.id.listview);
        //connect server
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Logger.i("connect success");
                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                Log.d("xzf", status.getMsg());
            }
        });
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());

        jianting();
        //默认显示的fragment
        fragment1();
        //侧滑listview的设置
        MyDrawlayoutAdapter adapter = new MyDrawlayoutAdapter(mymainactivity.this, user.getUsername(), user.getQianming(), user.getAvatar(),sp.getString("background",""));
        adapter.setList(DrawerLayoutList.getlist());
        listView.setAdapter(adapter);

        //侧滑的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //在用户不可见的情况下关闭侧滑
                new CountDownTimer(501,488) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        drawerLayout.closeDrawer(listView);
                    }
                }.start();
                switch (i) {
                    case 1:
                        final User user = BmobUser.getCurrentUser(User.class);
                        if(user.isConfirmState()&user.getRealavatar().length()>0
                                &user.getStudentid().length()>0&user.getStudentkey().length()>0){
                            Toast.makeText(mymainactivity.this, "您已填写过验证资料", Toast.LENGTH_SHORT).show();
                        }else if(user.isConfirmState()) {
                            Intent intent4 = new Intent();
                            intent4.setClass(mymainactivity.this, changehead.class);
                            intent4.putExtra("changehead",false);
                            startActivity(intent4);
                        }else if(!user.isConfirmState()){
                            Intent intent4 = new Intent();
                            intent4.setClass(mymainactivity.this, comfirmstate.class);
                            startActivity(intent4);
                        }
                        break;
                    case 2:
                        Intent intent = new Intent();
                        intent.setClass(mymainactivity.this, duihuanjiangpin.class);
                        startActivity(intent);
                        break;

                    case 3:
                         openkefu();
                        break;

                    case 4:
                        Intent intent2 = new Intent();
                        intent2.setClass(mymainactivity.this, mypipei.class);
                        startActivity(intent2);
                        break;

                    case 5:
                        MyUtils.showShare(mymainactivity.this);
                        break;

                    case 6:
                        Intent intent6 = new Intent();
                        intent6.setClass(mymainactivity.this, myset.class);
                        startActivity(intent6);
                        break;
                }
                //下面是用户修改背景图片的点击事件
                backg = (ImageView) listView.getChildAt(0).findViewById(R.id.backg);
                backg.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newOpenPicture();
                    }
                });
            }
        });


        GestureDetector.SimpleOnGestureListener my = new GestureDetector.SimpleOnGestureListener() {
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                Log.e("<--滑动测试-->", "开始滑动");

                if(e1!=null&e2!=null){
                    float x = e1.getX() - e2.getX();
                    float x2 = e2.getX() - e1.getX();
                    if (x > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                        drawerLayout.closeDrawer(listView);
                    } else if (x2 > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                        drawerLayout.openDrawer(listView);
                    }
                    return true;
                }
               return false;
            }
        };
        gestureDetector = new GestureDetector(this, my);
        drawerLayout.setOnTouchListener(this);

        if(getIntent().getBooleanExtra("isquxiaotask",false)){
            Intent intent1 = new Intent("android.text_change");
            intent1.putExtra("text", "关闭任务");
            sendBroadcast(intent1);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //启动service
        Intent startIntent = new Intent(this,Myservice.class);
        startService(startIntent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    private void jianting(){
        final BmobRealTimeData rtd = new BmobRealTimeData();
        rtd.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {
                Log.d("xzff","——user监听方法执行了"+data);
                if(!panduan){
                    fragment2query();
                }else{
                    panduan=false;
                }
                //监测下有没有被封号
//                BmobQuery<ManagementTable> query = new BmobQuery<ManagementTable>();
//                query.getObject(sp.getString("manage", ""), new QueryListener<ManagementTable>() {
//                    @Override
//                    public void done(ManagementTable user1, BmobException e) {
//                        if (e==null) {
//                            if (user1.getBeoff()) {
//                                finish();
//                                toast("您的账号已被封禁，有任何问题请联系QQ2759143677");
//                            } else{
//                                MyApplication.dongjie = user1.getDongjie();
//                                MyApplication.studentcomfirm = user1.getStudentcomfirm();
//                                if(MyApplication.dongjie){
//                                    toast("您的账号已被冻结，请与客服联系");
//                                }
//                            }
//                        }else{
//                            Log.d("xzf",e.getErrorCode()+e.getMessage());
//                        }
//                    }
//                });
                BmobQuery<User> query=new BmobQuery<User>();
                query.getObject(sp.getString("objetid11", ""), new QueryListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if(e==null){
                        }
                    }
                });
            }

            @Override
            public void onConnectCompleted(Exception ex) {
                if(rtd.isConnected()){
//                    rtd.subRowUpdate("ManagementTable",sp.getString("manage", ""));
                    rtd.subRowUpdate("_User",sp.getString("objetid11", ""));
                    Log.d("xzff", "—User监听连接成功:"+rtd.isConnected());
                }else{
                    Log.d("xzff", "—User监听连接失败:"+ex.getMessage());
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        User user = BmobUser.getCurrentUser(User.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Logger.i("connect success");
                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
            Log.d("xzf","在线人数加1");
        if(renshupanduan){
            MessageKefu messageKefu=new MessageKefu();
            messageKefu.increment("nownumber",1);
            messageKefu.update("Mw4Hcccm", new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e!=null){
                        renshupanduan=true;
                        Log.d("xzf",""+e.getErrorCode()+e.getMessage());
                    }else{
                        renshupanduan=false;
                    }
                }
            });
        }

    }

    private void shuaxinpipeifragment() {
        MyApplication.shuaxin = false;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment1);
        fragment1 = new MateFragment(changeFragmentinterface);
        ft.hide(MyApplication.currentfragment).add(R.id.myfragment_container, fragment1).show(fragment1).commit();
        MyApplication.currentfragment = fragment1;

        pipei.setEnabled(false);
        dongtai.setEnabled(true);
        xiaoxi.setEnabled(true);
        my.setEnabled(true);
        pipeilay.setEnabled(false);
        dongtailay.setEnabled(true);
        xiaoxilay.setEnabled(true);
        mylay.setEnabled(true);
        pipeitx.setTextColor(getResources().getColor(R.color.myblack));
        duorentx.setTextColor(getResources().getColor(R.color.colorhui));
        dongtaitx.setTextColor(getResources().getColor(R.color.colorhui));
        xiaoxitx.setTextColor(getResources().getColor(R.color.colorhui));
    }



    public void dongtaifragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (dongtaifragment == null) {
            dongtaifragment = new dongtaifragment();
        }
        if (!dongtaifragment.isAdded()) {
            ft.add(R.id.myfragment_container, dongtaifragment);
        } else {
            ft.show(dongtaifragment);
        }
        ft.hide(MyApplication.currentfragment);
        ft.commit();
        MyApplication.currentfragment = dongtaifragment;

    }

    //切换这个fragment时是隐藏方法
    public void fragment1() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment1 == null) {
            fragment1 = new MateFragment(changeFragmentinterface);
        }
        if (!fragment1.isAdded()) {
            ft.add(R.id.myfragment_container, fragment1);
        } else {
            ft.show(fragment1);
        }
        ft.hide(MyApplication.currentfragment);
        ft.commit();
        MyApplication.currentfragment = fragment1;
    }

    //聊天界面
    public void setConversationFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (conversationFragment == null) {
            conversationFragment = new ConversationFragment(changeFragmentinterface);
        }
        if (!conversationFragment.isAdded()) {
            ft.add(R.id.myfragment_container, conversationFragment);
        } else {
            ft.show(conversationFragment);
        }
        ft.hide(MyApplication.currentfragment);
        ft.commit();
        MyApplication.currentfragment = conversationFragment;
    }

    //我的界面
    public void fragment3() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment3 == null) {
            fragment3 = new fragment3();
        }
        if (!fragment3.isAdded()) {
            ft.add(R.id.myfragment_container, fragment3);
        } else {
            ft.show(fragment3);
        }
        ft.hide(MyApplication.currentfragment);
        ft.commit();
        MyApplication.currentfragment = fragment3;
    }

    //通知界面
    public void fragment5(){
        xiaoyuandian.setImageResource(R.color.base_color_text_white);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment2 == null) {
            fragment2 = new fragment2(changeFragmentinterface);
        }
        if (!fragment2.isAdded()) {
            ft.add(R.id.myfragment_container, fragment2);
        } else {
            ft.show(fragment2);
        }
        ft.hide(MyApplication.currentfragment);
        ft.commit();
        MyApplication.currentfragment = fragment2;
    }

    //好友界面
    public void contactfragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (contactFragment == null) {
            contactFragment = new ContactFragment(changeFragmentinterface);
        }
        if (!contactFragment.isAdded()) {
            ft.add(R.id.myfragment_container, contactFragment);
        } else {
            ft.show(contactFragment);
        }
        ft.hide(MyApplication.currentfragment);
        ft.commit();
        MyApplication.currentfragment = contactFragment;
    }



    @Override
    protected void onResume() {
        super.onResume();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
        if (MyApplication.shuaxin) {
            shuaxinpipeifragment();
            MyApplication.shuaxin=false;
        }
    }


    @Override
    protected void onDestroy() {
        //清理导致内存泄露的资源
        super.onDestroy();
        BmobIM.getInstance().clear();
        //停止service
        Intent intentFour = new Intent(this,Myservice.class);
        stopService(intentFour);
    }



    /**
     * 注册消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(final MessageEvent event) {
        checkRedPoint(event.getMessage().getFromId(),event.getMessage().getContent());
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint("","");
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        log("---主页接收到自定义消息---");
        checkRedPoint("","");
    }

    private void checkRedPoint(String id, final String text) {
        //是否有未读消息
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        if (count > 0) {
            xiaoyuandian.setImageResource(R.color.dialog_color_title);
            shownotifation(id, text);
            MyApplication.huihua=true;
        }

        //是否有好友添加的请求
        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
            xiaoyuandian.setImageResource(R.color.dialog_color_title);
            MyApplication.huihua=true;
        }
    }






    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }


    //打开图片加载器的方法
    public void newOpenPicture() {
        FunctionOptions options = new FunctionOptions.Builder()
                .setMaxSelectNum(1)
                .setImageSpanCount(3)
                .setEnablePreview(true)
                .setCompress(true)
                .create();
        PictureConfig.getInstance().init(options).openPhoto(mymainactivity.this, resultCallback);
    }

    //图片回调方法
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> list) {
            for (LocalMedia my : list) {
                String path = my.getPath();
                Bitmap bitmap= BitmapFactory.decodeFile(path);
                backg.setImageBitmap(bitmap);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("background",path);
                editor.apply();
                if(bitmap.isRecycled()){
                    bitmap.recycle();
                }
            }

        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {
        }
    };





    //这个方法是DEMO自带的登陆方法，需要传入账户名和密码
    public void onloginclick(String username) {

        UserModel.getInstance().login(username, "123456", new LogInListener() {

            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    Toast.makeText(mymainactivity.this, "用户信息更新成功", Toast.LENGTH_SHORT).show();
                    //这是个更新用户状态的方法
                    User user = (User) o;
                    BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        set();
        switch (v.getId()){
            case R.id.pipeilay:
                pipei.setEnabled(false);
                pipeilay.setEnabled(false);
                pipeitx.setTextColor(getResources().getColor(R.color.myorange2));
                fragment1();
                break;
            case R.id.dongtailay:
                dongtai.setEnabled(false);
                dongtailay.setEnabled(false);
                duorentx.setTextColor(getResources().getColor(R.color.myorange2));
                dongtaifragment();
                break;
            case R.id.xiaoxilay:
                xiaoxi.setEnabled(false);
                xiaoxilay.setEnabled(false);
                dongtaitx.setEnabled(false);
                fragment5();
                break;
            case R.id.mylay:
                mylay.setEnabled(false);
                my.setEnabled(false);
                xiaoxitx.setTextColor(getResources().getColor(R.color.myorange2));
                fragment3();
                break;

        }
    }
    public void set(){
        pipei.setEnabled(true);
        dongtai.setEnabled(true);
        xiaoxi.setEnabled(true);
        my.setEnabled(true);
        pipeilay.setEnabled(true);
        dongtailay.setEnabled(true);
        xiaoxilay.setEnabled(true);
        mylay.setEnabled(true);
        pipeitx.setTextColor(getResources().getColor(R.color.colorhui));
        duorentx.setTextColor(getResources().getColor(R.color.colorhui));
        dongtaitx.setTextColor(getResources().getColor(R.color.colorhui));
        xiaoxitx.setTextColor(getResources().getColor(R.color.colorhui));
    }



    //广播接收器
    public class BroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            if(data.getString("text").equals("刷新")){
                shuaxinpipeifragment();
            }else if(data.getString("text").equals("开启客服")) {
                    openkefu();
            }else if(data.getString("text").equals("关闭任务")) {
                //取消所有匹配任务
                Log.d("xzf","原匹配任务结束了");
            }
        }
    }


    private void showRedPoint(){
        xiaoyuandian.setImageResource(R.color.dialog_color_title);
    }

    private void queryFragment2(final boolean isnotice){
        final User user = BmobUser.getCurrentUser(User.class);
        //查询固定的客服消息
        BmobQuery<MessageKefu> messageKefuBmobQuery=new BmobQuery<>();
        messageKefuBmobQuery.getObject("0c491b80de", new QueryListener<MessageKefu>() {
            @Override
            public void done(MessageKefu messageKefu, BmobException e) {
                if(e==null){
                    final pinglun kefupinglun=new pinglun();
                    kefupinglun.setNoteType(3);
                    kefupinglun.setUserimage(messageKefu.getUserimage());
                    kefupinglun.setText(messageKefu.getText());
                    kefupinglun.setUsername(messageKefu.getHead());
                    kefupinglun.setObjectId("teshude");
                    BmobQuery<pinglun> query1=new BmobQuery<>();
                    query1.setLimit(50);
                    query1.addWhereEqualTo("touserid",user.getObjectId());
                    query1.addWhereEqualTo("IsNoticeClick",false);
                    query1.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
                    query1.order("-createdAt");
                    query1.findObjects(new FindListener<pinglun>() {
                        @Override
                        public void done(List<pinglun> list, BmobException e) {
                            if(e==null){
                                    fragment2=new fragment2(changeFragmentinterface);
                                    final List<pinglun> mylist=new ArrayList<>();
                                    mylist.add(kefupinglun);
                                    if(list.size()>0){
                                        if(!list.get(0).getNoticeClick()){
                                            showRedPoint();
                                        }
                                        mylist.addAll(list);
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("MSPANSCOMMIT", (Serializable) mylist);

                                    fragment2.setArguments(bundle);
                                    if(isnotice&!mylist.get(1).getNoticeClick()){
                                        showRedPoint();
                                        //发送广播，实时更新
                                        Intent intent = new Intent("fragment2");
                                        bundle.putString("fragment2", "fragment2");
                                        intent.putExtras(bundle);
                                        sendBroadcast(intent);

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String text = null;
                                                String head=null;
                                                final Intent intent=new Intent();
                                                if(mylist.get(1).getNoteType()==4){
                                                    text="您收到"+mylist.get(1).getZhuti()+"发来的评论消息";
                                                    head="新的评论";
                                                    intent.setClass(getApplicationContext(),candelete3.class);
                                                    intent.putExtra("pinglunid",mylist.get(1).getBelongobjectid());
                                                }else if(mylist.get(1).getNoteType()==2){
                                                    text="您收到"+mylist.get(1).getUsername()+"发来的匹配邀请";
                                                    head="新的邀请";
                                                    intent.putExtra("isquxiaotask",true);
                                                    intent.setClass(getApplicationContext(),mymainactivity.class);
                                                }
                                                Bitmap bitmap= null;
                                                Bitmap mbitmap=null;
                                                try {
                                                    bitmap = MyUtils.getBitmap(mylist.get(1).getUserimage());
                                                    int width = bitmap.getWidth();
                                                    int height = bitmap.getHeight();
                                                    // 设置想要的大小
                                                    int newWidth = 144;
                                                    int newHeight = 144;
                                                    // 计算缩放比例
                                                    float scaleWidth = ((float) newWidth) / width;
                                                    float scaleHeight = ((float) newHeight) / height;
                                                    Matrix matrix = new Matrix();
                                                    matrix.postScale(scaleWidth, scaleHeight);
                                                    mbitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                                                } catch (IOException e1) {
                                                    e1.printStackTrace();
                                                }
                                                BmobNotificationManager.getInstance(mymainactivity.this).showNotification(mbitmap,head,
                                                        text, "",intent);

                                            }
                                        }).start();
                                    }

                            }else{
                                Log.d("xzf","3"+e.getErrorCode()+e.getMessage());
                            }
                        }
                    });

                }else{
                    Log.d("xzf","4"+e.getMessage()+e.getErrorCode());
                }
            }
        });
    }

    private void fragment2query(){
        final User user = BmobUser.getCurrentUser(User.class);
                    BmobQuery<pinglun> query1=new BmobQuery<>();
                    query1.setLimit(1);
                    query1.order("-createdAt");
                    query1.addWhereEqualTo("touserid",user.getObjectId());
                    query1.addWhereEqualTo("IsNoticeClick",false);
                    query1.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
                    query1.findObjects(new FindListener<pinglun>() {
                        @Override
                        public void done(List<pinglun> list, BmobException e) {
                            if(e==null){
                                for(final pinglun ps:list) {
                                    showRedPoint();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("MSPANSCOMMI", ps);
                                        Intent intent = new Intent("fragment2");
                                        bundle.putString("fragment2", "fragment2");
                                        intent.putExtras(bundle);
                                        sendBroadcast(intent);
                                        //发送广播，实时更新

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String text = null;
                                                String head=null;
                                                final Intent intent=new Intent();
                                                if(ps.getNoteType()==4){
                                                    text="您收到"+ps.getZhuti()+"发来的评论消息";
                                                    head="新的评论";
                                                    intent.setClass(getApplicationContext(),candelete3.class);
                                                    intent.putExtra("pinglunid",ps.getBelongobjectid());
                                                }else if(ps.getNoteType()==2){
                                                    text="您收到"+ps.getUsername()+"发来的匹配邀请";
                                                    head="新的邀请";
                                                    intent.putExtra("isquxiaotask",true);
                                                    intent.setClass(getApplicationContext(),mymainactivity.class);
                                                }
                                                Bitmap bitmap= null;
                                                Bitmap mbitmap=null;
                                                try {
                                                    bitmap = MyUtils.getBitmap(ps.getUserimage());
                                                    int width = bitmap.getWidth();
                                                    int height = bitmap.getHeight();
                                                    // 设置想要的大小
                                                    int newWidth = 144;
                                                    int newHeight = 144;
                                                    // 计算缩放比例
                                                    float scaleWidth = ((float) newWidth) / width;
                                                    float scaleHeight = ((float) newHeight) / height;
                                                    Matrix matrix = new Matrix();
                                                    matrix.postScale(scaleWidth, scaleHeight);
                                                    mbitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                                                } catch (IOException e1) {
                                                    e1.printStackTrace();
                                                }
                                                BmobNotificationManager.getInstance(mymainactivity.this).showNotification(mbitmap,head,
                                                        text, "",intent);

                                            }
                                        }).start();
                                }

                            }else{
                                Log.d("xzf","3"+e.getErrorCode()+e.getMessage());
                            }
                        }
                    });
            }

    private void querydongtaiFragment(){
        BmobQuery<ListInfo> myquery =new BmobQuery<ListInfo>();
        myquery.setLimit(50);
        myquery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        myquery.order("-createdAt");
        myquery.findObjects(new FindListener<ListInfo>() {
            @Override
            public void done(final List<ListInfo> list, BmobException e) {
                if(e==null){
                    List<ListInfo> listlist=new ArrayList<>();
                    listlist.addAll(list);
                    dongtaifragment=new dongtaifragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dongtai", (Serializable)listlist);
                    dongtaifragment.setArguments(bundle);
                }else{
                    Log.d("zpf","查询失败"+e.getErrorCode()+e.getMessage());
                }
            }
        });
    }



    //有未读会话时候的推送，以及推送的跳转意图
    private void shownotifation(String id, final String text){
        showRedPoint();
        BmobQuery<User> query=new BmobQuery<>();
        query.getObject(id, new QueryListener<User>() {
            @Override
            public void done(final User user, BmobException e) {
                if(e==null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent();
                            BmobIMUserInfo MateInfo = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                            BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(MateInfo, false, null);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("c", c);
                            intent.setClass(mymainactivity.this, ChatActivity.class);
                            intent.putExtra(getPackageName(), bundle);

                            Bitmap bitmap= null;
                            Bitmap mbitmap=null;
                            try {
                                bitmap = MyUtils.getBitmap(user.getAvatar());
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                // 设置想要的大小
                                int newWidth = 144;
                                int newHeight = 144;
                                // 计算缩放比例
                                float scaleWidth = ((float) newWidth) / width;
                                float scaleHeight = ((float) newHeight) / height;
                                Matrix matrix = new Matrix();
                                matrix.postScale(scaleWidth, scaleHeight);
                                mbitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            BmobNotificationManager.getInstance(mymainactivity.this).showNotification(mbitmap,user.getUsername(),
                                    text, "",intent);

                        }
                    }).start();
                }
            }
        });
    }



    private void openkefu(){
        if(null!=getIntent().getStringExtra("id")){
            BmobIMUserInfo info = new BmobIMUserInfo(getIntent().getStringExtra("id"),getIntent().getStringExtra("name"),getIntent().getStringExtra("avater"));
            BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, false, null);
            Bundle bundle = new Bundle();
            bundle.putSerializable("c", c);
            startActivity(ChatActivity.class, bundle, false);
        }else{
            BmobIMUserInfo info = new BmobIMUserInfo("d3dbe660d0","人工客服","http://bmob-cdn-10845.b0.upaiyun.com/2017/09/13/e98cd9a920384a36b146651305dc4222.png");
            BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, false, null);
            Bundle bundle = new Bundle();
            bundle.putSerializable("c", c);
            startActivity(ChatActivity.class, bundle, false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("xzf","主界面暂停，在线人数减1");
            MessageKefu messageKefu=new MessageKefu();
            messageKefu.increment("nownumber",-1);
            messageKefu.update("Mw4Hcccm", new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e!=null){
                        renshupanduan=false;
                        Log.d("xzf",""+e.getErrorCode()+e.getMessage());
                    }else{
                        renshupanduan=true;
                    }
                }
            });
    }



    //冻结账号
    private void dongjiezhanghao(){
        ManagementTable manage=new ManagementTable();
        manage.setIsfenghao(true);
        manage.setDongjie(true);
        manage.update(sp.getString("manage", ""), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    MyApplication.dongjie=true;
                }
            }
        });
    }





}
