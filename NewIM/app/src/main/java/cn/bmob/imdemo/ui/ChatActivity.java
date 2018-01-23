package cn.bmob.imdemo.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.orhanobut.logger.Logger;
import com.yalantis.ucrop.entity.LocalMedia;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.adapter.ChatAdapter;
import cn.bmob.imdemo.adapter.OnRecyclerViewListener;
import cn.bmob.imdemo.base.ParentWithNaviActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.MyTimeDialog.DateTimePickerDialog;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.LastInfo;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.imdemo.YiYou.mylogin.MyApplication;
import cn.bmob.imdemo.util.Util;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;
import cn.iwgang.countdownview.CountdownView;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**聊天界面
 * @author :smile
 * @project:ChatActivity
 * @date :2016-01-25-18:23
 */
public class ChatActivity extends ParentWithNaviActivity implements ObseverListener,MessageListHandler{


    @Bind(R.id.ll_chat)
    LinearLayout ll_chat;

    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;

    @Bind(R.id.rc_view)
    RecyclerView rc_view;

    @Bind(R.id.edit_msg)
    EditText edit_msg;

    @Bind(R.id.btn_chat_add)
    Button btn_chat_add;
    @Bind(R.id.btn_chat_emo)
    Button btn_chat_emo;
    @Bind(R.id.btn_speak)
    Button btn_speak;
    @Bind(R.id.btn_chat_voice)
    Button btn_chat_voice;
    @Bind(R.id.btn_chat_keyboard)
    Button btn_chat_keyboard;
    @Bind(R.id.btn_chat_send)
    Button btn_chat_send;

    @Bind(R.id.layout_more)
    LinearLayout layout_more;
    @Bind(R.id.layout_add)
    LinearLayout layout_add;
    @Bind(R.id.layout_emo)
    LinearLayout layout_emo;

    // 语音有关
    @Bind(R.id.layout_record)
    RelativeLayout layout_record;
    @Bind(R.id.tv_voice_tips)
    TextView tv_voice_tips;
    @Bind(R.id.iv_record)
    ImageView iv_record;
    private Drawable[] drawable_Anims;// 话筒动画
    BmobRecordManager recordManager;
    private ImageView seximg;
    ImageView timeicon;
    LinearLayout xiangmu;
    AlertDialog dialog;
    boolean ble;
    ImageView mimg;
    Handler handler;
    Dialog mydialog;
    boolean flag;
    ChatAdapter adapter;
    protected LinearLayoutManager layoutManager;
    BmobIMConversation c;
    ImageView frienddiv;
    LinearLayout friendlay;
    ImageView friendfdi;
    LinearLayout quedingpipei;
    BmobRealTimeData rtd;
    LinearLayout zhaopian;
    TextView otherItem;
    TextView otherDate;
    TextView otherSure;
    TextView myItem;
    TextView myDate;
    TextView mySure;
    String otherURL;
    Long OtherDate;
    String item;
    String item2;
    LinearLayout matchino;
    RelativeLayout ffcc33;
    TextView myspace;
    TextView otherspce;
    ImageView ffcc332;
    Boolean isjx;

    @Override
    protected String title() {
        return c.getConversationTitle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        MyApplication.ensure=false;
        matchino= (LinearLayout) findViewById(R.id.matchino);
        myItem= (TextView) findViewById(R.id.myitem);
        myDate= (TextView) findViewById(R.id.myDate);
        mySure= (TextView) findViewById(R.id.mysure);
        zhaopian= (LinearLayout) findViewById(R.id.zhaopian);
        otherItem= (TextView) findViewById(R.id.otheritem);
        otherDate= (TextView) findViewById(R.id.otherdate);
        otherSure= (TextView) findViewById(R.id.othersure);
        seximg= (ImageView) findViewById(R.id.canzhao);
        mimg= (ImageView) findViewById(R.id.mimg);
        frienddiv= (ImageView) findViewById(R.id.frienddiv);
        friendlay= (LinearLayout) findViewById(R.id.friendlay);
        friendfdi= (ImageView) findViewById(R.id.friendfdi);
        quedingpipei= (LinearLayout) findViewById(R.id.quedingpipei);
        ffcc33= (RelativeLayout) findViewById(R.id.ffcc33);
        ffcc332= (ImageView) findViewById(R.id.ffcc332);
        myspace= (TextView) findViewById(R.id.myspace);
        otherspce= (TextView) findViewById(R.id.otherspace);
        c = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getBundle().getSerializable("c"));
        initNaviView();
        initSwipeLayout();
        initVoiceView();
        initBottomView();
        //设置匹配跳转的时候跳转到匿名还有监听方法
        if(MyApplication.isMateChatActivity){
            MyApplication.ischatnow=true;
            //删除指定会话
            BmobIM.getInstance().deleteConversation(c);
            toast("如您发现对方使用非真实照片，请您积极向客服反应！核实后您会获得对方赔付的奖励");
            if(null==MyApplication.myappdate){
                MyApplication.myappdate=System.currentTimeMillis();
            }
            if(null==OtherDate){
                OtherDate=System.currentTimeMillis();
            }
            Log.d("bmob4",""+MyApplication.OtherObjectId);
            if(MyApplication.OtherObjectId.length()>0){
                Log.d("bmob4",""+"这个方法执行了没");
                BmobQuery<MyUser> query=new BmobQuery<MyUser>();
                query.getObject(MyApplication.OtherObjectId, new QueryListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if(e==null){
                            Log.d("xzf",""+MyApplication.isJXpipei);
                            if(!MyApplication.isJXpipei){
                                item=MyApplication.item;
                                myItem.setText(MyApplication.item);
                                item2=myUser.getItem();
                                otherItem.setText(item2);
                                otherDate.setText(MyUtils.getStringDate2(myUser.getDate()));
                                if(null!=MyApplication.myappdate){
                                    myDate.setText(MyUtils.getStringDate2(MyApplication.myappdate));
                                }
                                tv_title.setText("(匿名)");
                                isjx=false;
                                //还要设置地点
                            }else{
                                //跳转完了初始化条件
                                MyUser myUse=new MyUser();
                                myUse.setWasMated("尚未匹配");
                                myUse.setJXpipei("");
                                myUse.setJXpipei2("");
                                myUse.setItem("待选");
                                myUse.setSpace("集合地点");
                                myUser.setAreJianting(true);
                                myUse.setDate(System.currentTimeMillis());
                                myUse.setEnsure(false);
                                myUse.setOnback(false);
                                myUse.update(sp.getString("userMateID", ""),new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e!=null){
                                            Log.d("bmob",""+e.getErrorCode()+e.getMessage());
                                        }
                                    }
                                });
                                isjx=true;
                                myItem.setText("待选");
                                otherItem.setText("待选");
                                myspace.setText("集合地点");
                                otherspce.setText("集合地点");
                                myDate.setText(MyUtils.getStringDate2(System.currentTimeMillis()));
                                otherDate.setText(MyUtils.getStringDate2(System.currentTimeMillis()));
                            }
                            if(null!=myUser.getUserRealAvater()){
                                otherURL=myUser.getUserRealAvater();
                            }else{
                                otherURL="1";
                            }
                            if(myUser.getUsersex().equals("男")){
                                seximg.setImageResource(R.drawable.boy);
                            }else{
                                seximg.setImageResource(R.drawable.girl);
                            }
                        }else{
                            Log.d("bmob4",""+e.getErrorCode()+e.getMessage());
                        }
                    }
                });
            }
            friendlay.setVisibility(View.VISIBLE);
            friendfdi.setVisibility(View.VISIBLE);
            matchino.setVisibility(View.VISIBLE);
            ffcc33.setBackgroundResource(R.color.color_theme);
            ffcc332.setBackgroundResource(R.color.color_theme);
            tv_title.setTextColor(getResources().getColor(R.color.base_color_text_white));
            flag=true;
            //监听方法
            jianting();
            //确定按钮
            quedingpipei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setMydialog();
                }
            });
            //时间重新选择
            timeicon= (ImageView) findViewById(R.id.timeicon);
            timeicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
            //项目于的重新选择
            xiangmu= (LinearLayout) findViewById(R.id.xiangmu);
            xiangmu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        ble=true;
                        String arr[]={"篮球","图书馆","约牌","散步","羽毛球","乒乓球","下午茶","电影院","KTV"};
                        Dialog(arr);
                }
            });
            //倒计时控件
            CountdownView mCvCountdownView = (CountdownView)findViewById(R.id.counttime);
            mCvCountdownView.start(1800000); // 毫秒
            mCvCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    Toast.makeText(ChatActivity.this,"聊天到此为止",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            //选择集合地点
            zhaopian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(ChatActivity.this);
                    final String[]arr={"南京财经大学操场","南京财经大学图书馆","南京财经大学校门","南京邮电大学操场","南京邮电大学图书馆",
                            "南京邮电大学校门","南京师范大学操场","南京师范大学图书馆","南京师范大学校门"};
                    builder.setItems(arr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            String space="南"+arr[which].substring(2,3)+arr[which].substring(6);
                            myspace.setText(space);
                            gaoliang1();
                            //保存到服务器上
                            MyUser myUser=new MyUser();
                            myUser.setSpace(arr[which]);
                            myUser.setAreJianting(true);
                            myUser.update(sp.getString("userMateID",""), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(ChatActivity.this,"您选择了"+arr[which],Toast.LENGTH_SHORT).show();
                                    }else{
                                        myspace.setText("意外错误");
                                    }
                                }
                            });
                        }
                    });
                    builder.create().show();
                }
            });
            handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    final Bitmap bitmap= (Bitmap) msg.obj;
                    CountDownTimer cd=new CountDownTimer(2001,2000) {
                        @Override
                        public void onTick(long l) {
                            openimagedialog(bitmap);
                        }

                        @Override
                        public void onFinish() {
                            mydialog.cancel();
                        }
                    };
                    cd.start();
                }
            };
        }


    }

    private void Dialog(final String[] str){
        if(ble){
            AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
            builder.setTitle("请选择项目");
            builder.setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, final int i) {
                    dialog.dismiss();
                    MyUser myUser=new MyUser();
                    myUser.setAreJianting(true);
                    myUser.setItem(str[i]);
                    myUser.update(sp.getString("userMateID",""), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                               if(e==null){
                                   Toast.makeText(ChatActivity.this,"您选择了"+str[i],Toast.LENGTH_SHORT).show();
                                   myItem.setText(str[i]);
                                   MyApplication.item=str[i];
                                   gaoliang1();
                               }
                        }
                    });
                }
            });
            dialog=builder.create();
            dialog.show();
            ble=false;
        }
    }


    public void showDialog() {
        DateTimePickerDialog dialog = new DateTimePickerDialog(ChatActivity.this,
                System.currentTimeMillis());
        /**
         * 实现接口
         */
        dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
            @Override
            public void OnDateTimeSet(android.support.v7.app.AlertDialog dialog, final long date) {
                MyUser mm=new MyUser();
                mm.setAreJianting(true);
                mm.setDate(date);
                mm.update(sp.getString("userMateID",""), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            MyApplication.myappdate=date;
                            myDate.setText(MyUtils.getStringDate2(date));
                            Toast.makeText(ChatActivity.this,"匹配时间设置成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ChatActivity.this,"系统错误，请重新尝试",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
        dialog.show();
    }

    private void initSwipeLayout() {

        sw_refresh.setEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(this, c);
        rc_view.setAdapter(adapter);
        ll_chat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_chat.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                //自动刷新
                queryMessages(null);
            }
        });
        //下拉加载
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);
            }
        });
        //设置RecyclerView的点击事件
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Logger.i("" + position);
            }

            @Override
            public boolean onItemLongClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("是否删除消息");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        c.deleteMessage(adapter.getItem(position));
                        adapter.remove(position);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    private void initBottomView() {
        edit_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                    scrollToBottom();
                }
                return false;
            }
        });
        edit_msg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_voice.setVisibility(View.GONE);
                } else {
                    if (btn_chat_voice.getVisibility() != View.VISIBLE) {
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 初始化语音布局
     *
     * @param
     * @return void
     */
    private void initVoiceView() {
        btn_speak.setOnTouchListener(new VoiceTouchListener());
        initVoiceAnimRes();
        initRecordManager();
    }

    /**
     * 初始化语音动画资源
     *
     * @param
     * @return void
     * @Title: initVoiceAnimRes
     */
    private void initVoiceAnimRes() {
        drawable_Anims = new Drawable[]{
                getResources().getDrawable(R.mipmap.chat_icon_voice2),
                getResources().getDrawable(R.mipmap.chat_icon_voice3),
                getResources().getDrawable(R.mipmap.chat_icon_voice4),
                getResources().getDrawable(R.mipmap.chat_icon_voice5),
                getResources().getDrawable(R.mipmap.chat_icon_voice6)};
    }

    private void initRecordManager() {
        Log.d("voice","初始化了语音管理器");
        // 语音相关管理器
        recordManager = BmobRecordManager.getInstance(this);
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

            @Override
            public void onVolumnChanged(int value) {
                iv_record.setImageDrawable(drawable_Anims[value]);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                Logger.i("voice", "已录音长度:" + recordTime);
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
                    // 需要重置按钮
                    btn_speak.setPressed(false);
                    btn_speak.setClickable(false);
                    // 取消录音框
                    layout_record.setVisibility(View.INVISIBLE);
                    // 发送语音消息
                    sendVoiceMessage(localPath, recordTime);
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            btn_speak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    /**
     * 长按说话
     *
     * @author smile
     * @date 2014-7-1 下午6:10:16
     */
    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(ContextCompat.checkSelfPermission(ChatActivity.this, android.Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(ChatActivity.this,new String[]{
                                android.Manifest.permission.RECORD_AUDIO},1);}
                    if (!Util.checkSdCard()) {
                        Log.d("voice","没有SD卡权限或者没有SD卡");
                        Toast.makeText(ChatActivity.this,"发送语音需要SD卡支持",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Log.d("voice","手指按下，开始录音");
                    try {
                        v.setPressed(true);
                        layout_record.setVisibility(View.VISIBLE);
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(c.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("voice",""+e);
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        tv_voice_tips.setTextColor(Color.RED);
                    } else {
                        tv_voice_tips.setText(getString(R.string.voice_up_tips));
                        tv_voice_tips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    Log.d("voice","手指抬起，停止录音");
                    v.setPressed(false);
                    layout_record.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// 放弃录音
                            recordManager.cancelRecording();
                            Logger.i("voice", "放弃发送语音");
                        } else {
                            int recordTime = recordManager.stopRecording();
                            Log.d("voice","准备发送语音，语音时长"+recordTime);
                            if (recordTime > 0.2) {
                                // 发送语音文件
                                sendVoiceMessage(recordManager.getRecordFilePath(c.getConversationId()), recordTime);
                            } else {// 录音时间过短，则提示录音过短的提示
                                layout_record.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("voice","有异常");
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    Toast toast;

    /**
     * 显示录音时间过短的Toast
     *
     * @return void
     * @Title: showShortToast
     */
    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    @OnClick(R.id.edit_msg)
    public void onEditClick(View view) {
        if (layout_more.getVisibility() == View.VISIBLE) {
            layout_add.setVisibility(View.GONE);
            layout_emo.setVisibility(View.GONE);
            layout_more.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_chat_emo)
    public void onEmoClick(View view) {
        if (layout_more.getVisibility() == View.GONE) {
            showEditState(true);
        } else {
            if (layout_add.getVisibility() == View.VISIBLE) {
                layout_add.setVisibility(View.GONE);
                layout_emo.setVisibility(View.VISIBLE);
            } else {
                layout_more.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.btn_chat_add)
    public void onAddClick(View view) {
        if (layout_more.getVisibility() == View.GONE) {
            layout_more.setVisibility(View.VISIBLE);
            layout_add.setVisibility(View.VISIBLE);
            layout_emo.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            if (layout_emo.getVisibility() == View.VISIBLE) {
                layout_emo.setVisibility(View.GONE);
                layout_add.setVisibility(View.VISIBLE);
            } else {
                layout_more.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.btn_chat_voice)
    public void onVoiceClick(View view) {
        edit_msg.setVisibility(View.GONE);
        layout_more.setVisibility(View.GONE);
        btn_chat_voice.setVisibility(View.GONE);
        btn_chat_keyboard.setVisibility(View.VISIBLE);
        btn_speak.setVisibility(View.VISIBLE);
        hideSoftInputView();
    }

    @OnClick(R.id.btn_chat_keyboard)
    public void onKeyClick(View view) {
        showEditState(false);
    }

    @OnClick(R.id.btn_chat_send)
    public void onSendClick(View view) {
        sendMessage();
    }

    @OnClick(R.id.tv_picture)
    public void onPictureClick(View view) {
//        sendLocalImageMessage();
//        sendOtherMessage();
//        sendVideoMessage();
        sendRemoteImageMessage();
    }

    @OnClick(R.id.tv_camera)
    public void onCameraClick(View view) {
        if(MyApplication.isMateChatActivity){
            if(otherURL.toString().length()<5){
                Toast.makeText(ChatActivity.this, "对方首次匹配，暂未设置真人照片", Toast.LENGTH_SHORT).show();
            }else{
                if(flag){
                    new Thread(){
                        @Override
                        public void run() {
                            Message msg=Message.obtain();
                            try {
                                msg.obj=getBitmap(otherURL);
                                handler.sendMessage(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    Toast.makeText(ChatActivity.this, "3秒后取消显示", Toast.LENGTH_SHORT).show();
                    flag=false;
                }else{
                    Toast.makeText(ChatActivity.this, "您已看过一次，无法再看", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            toast("该功能仅在匹配状态下可使用，快去匹配小伙伴吧");
        }


    }

//    @OnClick(R.id.tv_location)
//    public void onLocationClick(View view) {
//        sendLocationMessage();
//    }

    /**
     * 根据是否点击笑脸来显示文本输入框的状态
     *
     * @param isEmo 用于区分文字和表情
     * @return void
     */
    private void showEditState(boolean isEmo) {
        edit_msg.setVisibility(View.VISIBLE);
        btn_chat_keyboard.setVisibility(View.GONE);
        btn_chat_voice.setVisibility(View.VISIBLE);
        btn_speak.setVisibility(View.GONE);
        edit_msg.requestFocus();
        if (isEmo) {
            layout_more.setVisibility(View.VISIBLE);
            layout_more.setVisibility(View.VISIBLE);
            layout_emo.setVisibility(View.VISIBLE);
            layout_add.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            layout_more.setVisibility(View.GONE);
            showSoftInputView();
        }
    }

    /**
     * 显示软键盘
     */
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(edit_msg, 0);
        }
    }

    /**
     * 发送文本消息
     */
    private void sendMessage() {
        String text = edit_msg.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            toast("请输入内容");
            return;
        }
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        //可设置额外信息
        Map<String, Object> map = new HashMap<>();
        map.put("level", "1");//随意增加信息
        msg.setExtraMap(map);
        c.sendMessage(msg, listener);
    }

    /**
     * 直接发送远程图片地址
     * 这是拍摄按钮调用的方法
     */
    public void sendRemoteImageMessage() {
        newOpenPicture();
    }

    /**
     * 发送本地图片地址
     * 这个方法好像不能用
     */
    public void sendLocalImageMessage() {
        Toast.makeText(ChatActivity.this, "这个按钮要删除掉,换一个拍摄图片的按钮", Toast.LENGTH_SHORT).show();
        //正常情况下，需要调用系统的图库或拍照功能获取到图片的本地地址，开发者只需要将本地的文件地址传过去就可以发送文件类型的消息
//            BmobIMImageMessage image =new BmobIMImageMessage();
//            c.sendMessage(image, listener);
//            //因此也可以使用BmobIMFileMessage来发送文件消息
//            BmobIMFileMessage file =new BmobIMFileMessage();
//            c.sendMessage(file,listener);
    }

    /**
     * 发送语音消息
     *
     * @param local
     * @param length
     * @return void
     * @Title: sendVoiceMessage
     */
    private void sendVoiceMessage(String local, int length) {
        BmobIMAudioMessage audio = new BmobIMAudioMessage(local);
        //可设置额外信息-开发者设置的额外信息，需要开发者自己从extra中取出来
        Map<String, Object> map = new HashMap<>();
        map.put("from", "优酷");
        audio.setExtraMap(map);
        //设置语音文件时长：可选
//        audio.setDuration(length);
        c.sendMessage(audio, listener);
    }

    /**
     * 发送视频文件
     */
    private void sendVideoMessage() {
        BmobIMVideoMessage video = new BmobIMVideoMessage("/storage/sdcard0/bimagechooser/11.png");
        c.sendMessage(video, listener);
    }

    /**
     * 发送地理位置
     */
    public void sendLocationMessage() {
    }


    //URL下载图片的方法
    public static Bitmap getBitmap(String path) throws IOException{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    /**
     * 消息发送监听器
     */
    public MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            Logger.i("onProgress：" + value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
            edit_msg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            edit_msg.setText("");
            scrollToBottom();
            if (e != null) {
                toast(e.getMessage());
            }
        }
    };

    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     *
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg) {
        c.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                sw_refresh.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addMessages(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        Logger.i("聊天页面接收到消息：" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i = 0; i < list.size(); i++) {
            addMessage2Chat(list.get(i));
        }
    }

//    /**接收到聊天消息
//     * @param event
//     */
//    @Subscribe
//    public void onEventMainThread(MessageEvent event){
//        addMessage2Chat(event);
//    }
//
//    @Subscribe
//    public void onEventMainThread(OfflineMessageEvent event){
//        Map<String,List<MessageEvent>> map =event.getEventMap();
//        if(map!=null&&map.size()>0){
//            //只获取当前聊天对象的离线消息
//            List<MessageEvent> list = map.get(c.getConversationId());
//            if(list!=null && list.size()>0){
//                for (int i=0;i<list.size();i++){
//                    addMessage2Chat(list.get(i));
//                }
//            }
//        }
//    }

    /**
     * 添加消息到聊天界面中
     *
     * @param event
     */
    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (c != null && event != null && c.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            if (adapter.findPosition(msg) < 0) {//如果未添加到界面中
                adapter.addMessage(msg);
                //更新该会话下面的已读状态
                c.updateReceiveStatus(msg);
            }
            scrollToBottom();
        } else {
            Logger.i("不是与当前聊天对象的消息");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layout_more.getVisibility() == View.VISIBLE) {
                layout_more.setVisibility(View.GONE);
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    //点击按钮的弹出窗
    public  void  openimagedialog(Bitmap bitmap){
        //建立反射器
        LayoutInflater inflater=LayoutInflater.from(ChatActivity.this);
        final View myview=inflater.inflate(R.layout.myimagebackground,null);
        //设置DIALOG的主题
        mydialog=new Dialog(ChatActivity.this,R.style.Dialog_Fullscreen);
        //设置DIALOG的布局
        ImageView imageView= (ImageView) myview.findViewById(R.id.myimage);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydialog.cancel();
            }
        });
        mydialog.show();
        mydialog.setContentView(myview);
    }

    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //清理资源
        if (recordManager != null) {
            recordManager.clear();
        }
        //更新此会话的所有消息为已读状态
        if (c != null) {
            c.updateLocalCache();
        }
        hideSoftInputView();
        super.onDestroy();
        if(MyApplication.isMateChatActivity){
            //取消监听，释放资源
            if(rtd!=null){
                if(rtd.isConnected()){
                    rtd.unsubRowUpdate("MyUser",MyApplication.OtherObjectId);
                }
            }
            MyApplication.isMateChatActivity=false;
            MyApplication.ischatnow=false;
        }

    }

    //第一个参数：请求的权限，第二个参数：请求码
    public  void xzf_quanxian(String permission,int code){
        boolean panduan=haspermission(permission);
        if(panduan){
            return;
        }else
        {
            requestpermission(code,permission);
        }

    }

    //判断传入的权限是否授予了权限
    public boolean haspermission(String...permissions){
        for(String permission:permissions){
            if(ContextCompat.checkSelfPermission(this,permission)
                    != PackageManager.PERMISSION_GRANTED){
                return false;

            }
        }
        return true;
    }

    //请求权限的方法
    public void requestpermission(int code,String... permissions){
        ActivityCompat.requestPermissions(this,permissions,code);

    }

    //打开图片加载器的方法
    public void newOpenPicture(){
        if(!haspermission(Manifest.permission.CAMERA)){
            xzf_quanxian(Manifest.permission.CAMERA,520);
        }
        FunctionOptions options = new FunctionOptions.Builder().setMaxSelectNum(1).setImageSpanCount(3)
                .setEnablePreview(true).setCompress(true).create();
        PictureConfig.getInstance().init(options).openPhoto(ChatActivity.this, resultCallback);
    }

    //图片回调方法
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback(){
        @Override
        public void onSelectSuccess(List<LocalMedia> list) {
          for(LocalMedia my:list){
              String path=my.getPath();
              Log.d("xzf",""+path);
              BmobIMImageMessage image = new BmobIMImageMessage(path);
              c.sendMessage(image, listener);
          }
        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {
            String path=localMedia.getPath();
            Log.d("xzf",""+path);
            BmobIMImageMessage image = new BmobIMImageMessage(path);
            c.sendMessage(image, listener);
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("xzf6","退出方法执行了");
        if(MyApplication.isMateChatActivity){
            MyApplication.shuaxin=true;
            //因为是匹配的，所以在退出后应该删除会话，不保留在会话列表中
            BmobIM.getInstance().deleteConversation(c);
            //恢复重置
            MyUser myUser = new MyUser();
            myUser.setWasMated("尚未匹配");
            myUser.setOnback(true);
            myUser.setAreJianting(true);
            myUser.update(sp.getString("userMateID", ""), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {

                    } else {
                        Log.d("xzf", "chatactivty哪里出问题了102" +e.getErrorCode());
                    }
                }
            });
        }
    }

    private void jianting(){
        Log.d("bmob",""+MyApplication.OtherObjectId);
        if(MyApplication.OtherObjectId.length()>0){
           rtd= new BmobRealTimeData();
            rtd.start(new ValueEventListener() {
                @Override
                public void onDataChange(JSONObject data) {
                    final BmobQuery<MyUser> query=new BmobQuery<MyUser>();
                    query.getObject(MyApplication.OtherObjectId, new QueryListener<MyUser>() {
                        @Override
                        public void done(final MyUser myUser, BmobException e) {
                            if(e==null){
                                Log.d("bmob4",""+"监听方法启动了");
                                Log.d("xzf","查询"+1);
                                otherItem.setText(myUser.getItem());
                                if(myUser.getSpace().startsWith("南")){
                                    String space="南"+myUser.getSpace().substring(2,3)+myUser.getSpace().substring(6);
                                    otherspce.setText(space);
                                }else{
                                    otherspce.setText(myUser.getSpace());
                                }
                                gaoliang1();
                                OtherDate=myUser.getDate();
                                otherDate.setText(MyUtils.getStringDate2(myUser.getDate()));
                                if(myUser.getOnback()&!myUser.getEnsure()){
                                    Toast.makeText(ChatActivity.this,"对方已退出会话",Toast.LENGTH_SHORT).show();
                                    finish();
                                    //要刷新fragment
                                    MyApplication.shuaxin=true;
                                    MyUtils.rematch(sp.getString("userMateID", ""));
                                }else if(myUser.getEnsure()){
                                    if(MyApplication.ensure){
                                        MyApplication.shuaxin=true;
                                        MyApplication.isshowtx2=true;
                                        finish();
                                        sendbroadcast();
                                        Toast.makeText(ChatActivity.this,"匹配约定已达成，您可在正在进行页面查看",Toast.LENGTH_SHORT).show();
                                    }else{
                                        otherSure.setText("已确认");
                                        Toast.makeText(ChatActivity.this,"对方已确认，请您尽快确认",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Log.d("xzf6",""+MyApplication.OtherObjectId);
                                Log.d("xzf","17chatvitity哪里出问题了"+MyApplication.OtherObjectId);
                            }
                        }
                    });
                }

                @Override
                public void onConnectCompleted(Exception ex) {
                    Log.d("bmob", "连接成功:"+rtd.isConnected());
                    if(rtd.isConnected()){
                        // 监听表更新
                        rtd.subRowUpdate("MyUser",MyApplication.OtherObjectId);
                    }
                }
            });
        }else{
            Log.d("xzf","19chatvitity哪里出问题了"+MyApplication.OtherObjectId);
        }
    }

    private void lastinfo(final String name, final String id, final String avater,final String sex){
           Log.d("xzf6","myapplication.myappdate"+MyApplication.myappdate);
        Log.d("xzf6","OtherDate"+MyApplication.myappdate);
           final User user = BmobUser.getCurrentUser(User.class);
            //我的匹配数据
            LastInfo lastInfo=new LastInfo();
            //通用属性
            lastInfo.setInt(1);
            lastInfo.setLastItem(myItem.getText().toString());
            if(MyApplication.myappdate>=OtherDate){
                lastInfo.setLastTime(MyApplication.myappdate);
            }else{
                lastInfo.setLastTime(OtherDate);
            }
            //双方的属性
            lastInfo.setAID(user.getObjectId());
            lastInfo.setAname(user.getUsername());
            lastInfo.setAsex(user.getSex());
            lastInfo.setAavater(user.getAvatar());
            lastInfo.setAarrive(false);
            lastInfo.setAcancel(false);
            lastInfo.setBID(id);
            lastInfo.setBname(name);
            lastInfo.setBsex(sex);
            lastInfo.setJXpipei(isjx);
            lastInfo.setBavater(avater);
            lastInfo.setBarrive(false);
            lastInfo.setBcancel(false);
            lastInfo.setDELETE(false);
            lastInfo.setLastSpace(myspace.getText().toString());
            lastInfo.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        Log.d("xzf6","成功");
                        sendbroadcast();
                    }else{
                        Log.d("xzf6","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
    }

    private void setMydialog(){
        if(!myItem.getText().toString().equals(otherItem.getText().toString())|myItem.getText().toString().equals("待选")){
            Toast.makeText(ChatActivity.this,"项目不一致，无法确认",Toast.LENGTH_SHORT).show();
        }else if(!myspace.getText().toString().equals(otherspce.getText().toString())|myspace.getText().toString().equals("集合地点")){
            Toast.makeText(ChatActivity.this,"地点不一致，无法确认",Toast.LENGTH_SHORT).show();
        }else{
            if(!myDate.getText().toString().equals(otherDate.getText().toString())){
                Toast.makeText(ChatActivity.this,"项目不一致，最终时间以较晚的为准",Toast.LENGTH_SHORT).show();
            }
            MyUser myUser=new MyUser();
            myUser.setEnsure(true);
            myUser.setAreJianting(true);
            myUser.update(sp.getString("userMateID", ""), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        MyApplication.ensure=true;
                        if(MyApplication.OtherObjectId.length()>0){
                            BmobQuery<MyUser> query2=new BmobQuery<MyUser>();
                            query2.getObject(MyApplication.OtherObjectId, new QueryListener<MyUser>() {
                                @Override
                                public void done(MyUser myUser, BmobException e) {
                                    if(e==null){
                                        if(myUser.getEnsure()){
                                            Log.d("xzf6","setmydialog方法执行了");
                                            MyApplication.shuaxin=true;
                                            MyApplication.isshowtx2=true;
                                            finish();
                                            Toast.makeText(ChatActivity.this,"匹配约定已达成，您可在正在进行页面查看",Toast.LENGTH_SHORT).show();
                                            lastinfo(myUser.getUsername(),myUser.getUserid(),myUser.getUseravatar(),myUser.getUsersex());
                                        }else{
                                            mySure.setText("已确认");
                                            mySure.setTextColor(getResources().getColor(R.color.green));
                                            Toast.makeText(ChatActivity.this,"您已确认，请等待对方确认",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Log.d("xzf","20chatvitity哪里出问题了"+MyApplication.OtherObjectId);
                    }
                }
            });
        }

    }

    //检测自己的项目和对方是不是一样，不然就高亮显示
    private void gaoliang1(){
        if(!myItem.getText().toString().equals(otherItem.getText().toString())){
            myItem.setTextColor(Color.RED);
        }else if(myItem.getText().toString().equals(otherItem.getText().toString())&!myItem.getText().equals("待选")){
            myItem.setTextColor(getResources().getColor(R.color.green));
        }

        if(!myspace.getText().toString().equals(otherspce.getText().toString())){
            myspace.setTextColor(Color.RED);
        }else if(myspace.getText().toString().equals(otherspce.getText().toString())&!myspace.getText().equals("集合地点")){
            myspace.setTextColor(getResources().getColor(R.color.green));
        }
    }




    //达成匹配去刷新已经完成的匹配
    private void sendbroadcast(){
        Intent intent = new Intent("android");
        intent.putExtra("te", "刷");
        sendBroadcast(intent);
    }











}







