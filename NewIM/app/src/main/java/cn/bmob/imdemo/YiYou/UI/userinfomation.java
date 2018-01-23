package cn.bmob.imdemo.YiYou.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.AddFriendMessage;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.imdemo.YiYou.bean.ManagementTable;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.imdemo.YiYou.bean.pinglun;
import cn.bmob.imdemo.YiYou.mylogin.MyApplication;
import cn.bmob.imdemo.ui.ChatActivity;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * Created by DELL on 2017/6/1.
 */

public class userinfomation extends BaseActivity {
    String objectid;
    private CircleImageView mycircle;
    RelativeLayout im;
    TextView yunodng;
    TextView yinyue;
    TextView shuji;
    TextView dianying;
    TextView shiwu;
    TextView lvxing;
    TextView username1;
    TextView username2;
    TextView userindex;
    TextView userqianming;
    ImageView setback;
    ImageView ellipsis;
    RelativeLayout guanzhu;
    RelativeLayout pipei;
    TextView guanzhutxt;
    TextView pipeitxt;
    ViewPager viewPager;
    TextView ushuzi;
    ImageView infosexf;
    String manageid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfomartion);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mycircle= (CircleImageView)findViewById(R.id.mycircle);
        im= (RelativeLayout) findViewById(R.id.ims);
        yunodng= (TextView)findViewById(R.id.yundong);
        yinyue= (TextView)findViewById(R.id.yinyue);
        shuji= (TextView) findViewById(R.id.shuji);
        dianying= (TextView)findViewById(R.id.dianying);
        shiwu= (TextView) findViewById(R.id.shiwu);
        lvxing= (TextView) findViewById(R.id.lvxing);
        username1= (TextView)findViewById(R.id.username1);
        username2=(TextView)findViewById(R.id.username2);
        userindex=(TextView)findViewById(R.id.userindex);
        userqianming=(TextView)findViewById(R.id.userqianming);
        setback= (ImageView) findViewById(R.id.setback);
        ellipsis= (ImageView) findViewById(R.id.ellipsis);
        guanzhutxt= (TextView) findViewById(R.id.guanzhutxt);
        pipeitxt= (TextView) findViewById(R.id.pipeitxt);
        viewPager= (ViewPager) findViewById(R.id.viewpagers);
        ushuzi= (TextView) findViewById(R.id.ushuzi);
        infosexf= (ImageView) findViewById(R.id.infosexf);
        ellipsis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightydialog();
            }
        });
        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        objectid=getIntent().getStringExtra("objectid");
        guanzhu= (RelativeLayout) findViewById(R.id.guanzhu);
        if(null!=objectid){
            final User use = BmobUser.getCurrentUser(User.class);
            BmobQuery<User> query=new BmobQuery<>();
            query.getObject(objectid, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if(e==null){
                          if(use.getFollow().contains(user.getObjectId())){
                              guanzhutxt.setText("取消关注");
                          }
                    }
                }
            });
            BmobQuery<MyUser> mquery=new BmobQuery<>();
            mquery.getObject(sp.getString("userMateID", ""), new QueryListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if(null!=myUser.getJXpipei()){
                        if(objectid.equals(myUser.getJXpipei())){
                            pipeitxt.setText("已发送请求");
                        }
                    }
                }
            });
        }
        guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(guanzhutxt.getText().toString().equals("取消关注")){
                    toast("你已取消关注对方动态");
                    guanzhutxt.setText("关注TA");
                    Log.d("xzf2",""+objectid);
                    User user=new User();
                    user.removeAll("follow", Collections.singletonList(objectid));
                    user.update(sp.getString("objetid11", ""),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.i("xzf2","查询成功");
                            }else{
                                Log.i("xzf2","失败："+e.getMessage());
                            }
                        }
                    });
                }else {
                    if(sp.getString("objetid11", "").equals(objectid)){
                        toast("您不能关注自己哦");
                    }else{
                        toast("您已关注对方的动态");
                        guanzhutxt.setText("取消关注");
                        User user=new User();
                        user.addUnique("follow",objectid);
                        user.update(sp.getString("objetid11", ""), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                    }
                }
            }
        });

        pipei= (RelativeLayout) findViewById(R.id.pipei);
        pipei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pipeitxt.getText().equals("已发送请求")){
                    toast("您已经向对方发送过一次请求啦，请耐心等候");
                }else{
                    final User user = BmobUser.getCurrentUser(User.class);
                    if(objectid.equals(user.getObjectId())){
                        toast("您不能和自己匹配");
                    }else{
                        //如果被冻结了
                        if(MyApplication.dongjie){
                            Toast.makeText(getApplicationContext(),"您的账号已被冻结，请联系客服处理",Toast.LENGTH_SHORT).show();
                        }else{
                            //如果是第一次
                            if(sp.getBoolean("isfirstin",false)){
                                //只需要验证下地理位置即可
                                if(user.isConfirmState()){
                                      pipei();
                                }else{
                                    tocomfirm(userinfomation.this,"鉴于您首次匹配，仅需验证校区地理位置即可匹配");
                                }
                            }else
                            //不是首次匹配
                            {
                                if(user.isConfirmState()&MyApplication.studentcomfirm){
                                    pipei();
                                }else if(user.isConfirmState()&!MyApplication.studentcomfirm&user.getRealavatar()
                                        .length()>0&user.getStudentid().length()>0&user.getStudentkey().length()>0){
                                    Toast.makeText(userinfomation.this,"您提交过验证信息，请稍等客服处理,验证成功后可开始匹配",Toast.LENGTH_SHORT).show();
                                } else{
                                    tocomfirm(userinfomation.this,"您需要验证校区位置和学号后方可匹配");
                                }
                            }
                        }
                    }
                }
            }
        });
        initate();

    }

    private void initate(){
        BmobQuery<User> bmobQuery=new BmobQuery<>();
        bmobQuery.getObject(objectid, new QueryListener<User>() {
            @Override
            public void done(final User user, BmobException e) {
                if(e==null){
                    manageid=user.getManageid();
                    new ImageLoader().showImageByThread(mycircle,user.getAvatar());;
                    String[] arr=user.getInterest();
                    if(null!=arr){
                        yunodng.setText(arr[0]);
                        yinyue.setText(arr[1]);
                        shuji.setText(arr[2]);
                        dianying.setText(arr[3]);
                        shiwu.setText(arr[4]);
                        lvxing.setText(arr[5]);
                    }
                    if(null!=user.getIndex()){
                        if(user.getIndex().length()>0){
                            username1.setText(user.getIndex());
                        }
                    }
                    if(null!=user.getSex()){
                        if(user.getSex().equals("女")){
                            infosexf.setBackgroundResource(R.drawable.listinfogirl);
                        }else{
                            infosexf.setBackgroundResource(R.drawable.listinfoboy);
                        }
                    }
                    if(null!=user.getIndex()){
                        if(user.getIndex().length()>0){
                            userindex.setText(user.getIndex());
                        }
                    }
                    username2.setText(user.getUsername());
                    if(user.getQianming().length()>0){
                        userqianming.setText(user.getQianming());
                    }else{
                        userqianming.setText("对方很懒，什么也没说");
                    }
                    if(null!=user.getPictures()){
                        if(user.getPictures().size()>0){
                            im.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            ushuzi.setVisibility(View.VISIBLE);
                            viewPager.setAdapter(new viewpageadpter(user.getPictures()));
                            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    int p=position+1;
                                    ushuzi.setText(p+"/"+user.getPictures().size());
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                            ushuzi.setText("1/"+user.getPictures().size());
                        }else{
                            ushuzi.setVisibility(View.GONE);
                            im.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                        }
                    }else{
                        ushuzi.setVisibility(View.GONE);
                        im.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(userinfomation.this,"对方ID不合法",Toast.LENGTH_SHORT).show();
                    Log.d("xzf6",""+e.getErrorCode()+e.getMessage());
                    finish();
                }
            }
        });
    }

    private void rightydialog() {
        final User use = BmobUser.getCurrentUser(User.class);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.rightydialog2);
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
            window.findViewById(R.id.makefriends).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    BmobQuery<User> query=new BmobQuery<User>();
                    query.getObject(objectid, new QueryListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e==null){
                                if(use.getObjectId().equals(user.getObjectId())){
                                    toast("您不能添加自己为好友");
                                }else{
                                    BmobIMUserInfo info=new BmobIMUserInfo(user.getObjectId(),user.getUsername(),user.getAvatar());
                                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true,null);
                                    BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                                    AddFriendMessage msg =new AddFriendMessage();
                                    User currentUser = BmobUser.getCurrentUser(User.class);
                                    msg.setContent("很高兴认识你，认识一下吧?");//给对方的一个留言信息
                                    Map<String,Object> map =new HashMap<>();
                                    map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
                                    map.put("avatar",currentUser.getAvatar());//发送者的头像
                                    map.put("uid",currentUser.getObjectId());//发送者的uid
                                    msg.setExtraMap(map);
                                    conversation.sendMessage(msg, new MessageSendListener() {
                                        @Override
                                        public void done(BmobIMMessage msg, BmobException e) {
                                            if (e == null) {//发送成功
                                                toast("好友请求发送成功，等待验证");
                                            } else {//发送失败
                                                toast("发送失败:" + e.getMessage());
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    });

                }
            });
            window.findViewById(R.id.startchat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    BmobQuery<User> query=new BmobQuery<User>();
                    query.getObject(objectid, new QueryListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e==null){
                                if(user.getObjectId().equals(use.getObjectId())){
                                    toast("您不能和自己对话");
                                }else{
                                    BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                                    //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("c", c);
                                    startActivity(ChatActivity.class,bundle,false);
                                }

                            }
                        }
                    });

                }
            });
        }
    }

    class viewpageadpter extends PagerAdapter
    {
        private List<String> images;

        public viewpageadpter(List<String> images) {
            this.images=images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView lv=new ImageView(getApplicationContext());
            new ImageLoader().showImageByThread(lv,images.get(position));
            lv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(lv);
            return lv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void pipei(){
        final User user = BmobUser.getCurrentUser(User.class);
        pipeitxt.setText("已发送请求");
        Toast.makeText(userinfomation.this,"对方已收到您的匹配邀请，如果您发送新的匹配，本次匹配将会失效",Toast.LENGTH_SHORT).show();
        MyUser myUser=new MyUser();
        myUser.setAreJianting(true);
        myUser.setWasMated("即兴匹配");
        myUser.setJXpipei(objectid);
        myUser.setEnsure(false);
        myUser.setOnback(false);
        myUser.setJXpipei2("");
        myUser.update(sp.getString("userMateID", ""),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e!=null){
                    Log.d("bmob",""+e.getErrorCode()+e.getMessage());
                    Toast.makeText(userinfomation.this,"未知错误，请点击重试",Toast.LENGTH_SHORT).show();
                    pipeitxt.setText("发起匹配");
                }else{
                    MyApplication.JXid=objectid;
                    pinglun pinglun=new pinglun();
                    pinglun.setNoticeClick(false);
                    pinglun.setNoteType(2);
                    pinglun.setIsjX(false);
                    pinglun.setUserid(user.getObjectId());
                    pinglun.setTouserid(objectid);
                    pinglun.setTomyuserid(sp.getString("userMateID", ""));
                    pinglun.setUserimage(user.getAvatar());
                    pinglun.setUsername(user.getUsername());
                    pinglun.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                //这个用来通知更新通知界面
                                    ManagementTable managementTable=new ManagementTable();
                                    managementTable.increment("jishu",1);
                                    managementTable.setIsfenghao(false);
                                    managementTable.update(manageid,new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e!=null){
                                                Log.d("xzf",e.getErrorCode()+e.getMessage()+"");
                                            }
                                        }
                                    });

                            }else{
                                pipeitxt.setText("发起匹配");
                                Toast.makeText(userinfomation.this,"未知错误，请点击重试",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void tocomfirm(final Context context,String text){
        final User user = BmobUser.getCurrentUser(User.class);
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(text);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
                Intent intent=new Intent();
                if(!user.isConfirmState()){
                    intent.setClass(context,comfirmstate.class);
                }else{
                        intent.setClass(context,changehead.class);
                        intent.putExtra("changehead",false);
                }
                startActivity(intent);
            }
        });
        builder.create().show();
    }



}
