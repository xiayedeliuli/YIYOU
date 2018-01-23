package cn.bmob.imdemo.YiYou.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.ParentWithNaviFragment;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.ChangeFragment;
import cn.bmob.imdemo.YiYou.UI.candelete3;
import cn.bmob.imdemo.YiYou.UI.userinfomation;
import cn.bmob.imdemo.YiYou.UI.webview;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;
import cn.bmob.imdemo.YiYou.Utils.ImageLoaderAsync2;
import cn.bmob.imdemo.YiYou.bean.ListInfo;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.imdemo.YiYou.bean.pinglun;
import cn.bmob.imdemo.YiYou.mylogin.MyApplication;
import cn.bmob.imdemo.YiYou.mylogin.welcome;
import cn.bmob.imdemo.ui.ChatActivity;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * Created by DELL on 2017/5/21.
 */

@SuppressLint("ValidFragment")
public class fragment2 extends ParentWithNaviFragment {
    TextView conservation;
    TextView contact;
    ListView listView;
    User user;
    noticeadpter my;
    List<pinglun> mylist;
    CircleImageView fragment2yuandian;
    //动态注册广播接收器
    private IntentFilter intentFilter;
    private MyBroadReceiver broadReceiver;
    //通过第一项和最后一项，获取当前可见项的所有图片的url地址
    String[] urls;
    String[] tags;
    private boolean mFirstIn;
    ChangeFragment changeFragment;

    public fragment2(ChangeFragment changeFragment) {
        this.changeFragment = changeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment2, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        conservation = (TextView) rootView.findViewById(R.id.tv_title);
        contact = (TextView) rootView.findViewById(R.id.tv_title1);
        listView = (ListView) rootView.findViewById(R.id.listv_tongzhi);
        fragment2yuandian= (CircleImageView) rootView.findViewById(R.id.fragment2yuandian);
        listView.setDivider(null);
        user = BmobUser.getCurrentUser(User.class);
        if(null!= getArguments().getSerializable("MSPANSCOMMIT")){
            List<pinglun> list=(List<pinglun>) getArguments().getSerializable("MSPANSCOMMIT");
            if(list.size()>0){
                mylist=(List<pinglun>) getArguments().getSerializable("MSPANSCOMMIT");
                setmaps(mylist);
                my=new noticeadpter(mylist,getContext(),listView);
                listView.setAdapter(my);
            }
        }else{
            Toast.makeText(getContext(),"数据初始化失败，请重试",Toast.LENGTH_SHORT).show();
        }
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //动态注册广播接收器
        intentFilter = new IntentFilter();
        intentFilter.addAction( "fragment2");
        broadReceiver = new MyBroadReceiver();
        getActivity().registerReceiver(broadReceiver, intentFilter);
    }

    @Override
    protected String title() {
        return "会话";
    }

    @Override
    protected String title1() {
        return "好友";
    }

    @Override
    protected ChangeFragment minterface() {
        return changeFragment;
    }


    private class noticeadpter extends BaseAdapter implements AbsListView.OnScrollListener  {
        private List<pinglun> mylist;
        LayoutInflater inflater;
        //为了防止控件的复用，设置一个hashmap里存入每一项的postion和值，来进行判断
        HashMap<Integer, Boolean> isCheck;
        private ImageLoaderAsync2 imageLoaderAsync;
        private int mstart,mend;

        public noticeadpter(List<pinglun> mylist, Context context,ListView listView) {
            this.inflater = LayoutInflater.from(context);
            this.mylist = mylist;
            //hashmap的初始化
            isCheck = new HashMap<>();
            initData();
            //初始化这个类是保证只有一个缓存类被创建
            imageLoaderAsync=new ImageLoaderAsync2(listView,getContext());
            mFirstIn=true;
            //一定要注册接口
            listView.setOnScrollListener(this);
        }

        private void initData() {
            for (int i = 0; i < mylist.size(); i++) {
                isCheck.put(i, false);
            }
        }

        @Override
        public int getCount() {
            return mylist.size();
        }

        @Override
        public Object getItem(int position) {
            return mylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View havePictureView = null;
            viewholder viewholde = null;
            if (convertView == null) {
                viewholde=new viewholder();
                havePictureView = inflater.inflate(R.layout.item_tongzhi, null);
                viewholde.infohead= (TextView) havePictureView.findViewById(R.id.infohead);
                viewholde.infotext= (TextView) havePictureView.findViewById(R.id.infotext);
                viewholde.infoimg= (CircleImageView) havePictureView.findViewById(R.id.infoimg);
                viewholde.bd= (LinearLayout) havePictureView.findViewById(R.id.bd);
                havePictureView.setTag(viewholde);
                convertView = havePictureView;
            } else {
                viewholde = (viewholder) convertView.getTag();
            }

            if(mylist.get(position).getNoteType()!=3){
                if(mylist.get(position).getNoticeClick()){
                    viewholde.infohead.setTextColor(getResources().getColor(R.color.color_99));
                    viewholde.infotext.setTextColor(getResources().getColor(R.color.color_99));
                }else{
                    if(null!=isCheck.get(position)){
                        if(isCheck.get(position)){
                            viewholde.infohead.setTextColor(getResources().getColor(R.color.color_99));
                            viewholde.infotext.setTextColor(getResources().getColor(R.color.color_99));
                        }else{
                            viewholde.infohead.setTextColor(getResources().getColor(R.color.coco));
                            viewholde.infotext.setTextColor(getResources().getColor(R.color.coco));
                        }
                    }
                }
            }else{
                viewholde.infohead.setTextColor(getResources().getColor(R.color.orange));
                viewholde.infotext.setTextColor(getResources().getColor(R.color.orange2));
            }
            viewholde.infoimg.setTag(mylist.get(position).getObjectId());
            imageLoaderAsync.showImageByAsync(viewholde.infoimg,mylist.get(position).getUserimage());
            if(mylist.get(position).getNoteType()==4){
                viewholde.infotext.setText(mylist.get(position).getZhuti()+": "+mylist.get(position).getText());
                viewholde.infohead.setText("新的评论");
            }else if(mylist.get(position).getNoteType()==3){
                viewholde.infotext.setText(mylist.get(position).getText());
                viewholde.infohead.setText(mylist.get(position).getUsername());
            }else if(mylist.get(position).getNoteType()==2){
                viewholde.infotext.setText("您收到一条来自"+mylist.get(position).getUsername()+"的匹配邀请");
                viewholde.infohead.setText("匹配邀请");
            }
            final viewholder finalViewholde = viewholde;
            viewholde.bd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mylist.get(position).getNoteType()==3){
                        if(MyApplication.iskefu){
                            sendbroadcast();
                        }else{
                            Intent intent=new Intent();
                            intent.setClass(getContext(), webview.class);
                            if(null!=MyApplication.url){
                                intent.putExtra("url",MyApplication.url.toString());
                            }
                            startActivity(intent);
                        }
                        //设置被点击过的为灰色,并保存本次点击事件
                        if(mylist.get(position).getNoteType()!=3){
                            isCheck.put(position,true);
                            finalViewholde.infohead.setTextColor(getResources().getColor(R.color.color_99));
                            finalViewholde.infotext.setTextColor(getResources().getColor(R.color.color_99));
                            //保存点击事件为true
                            pinglun pinglun=new pinglun();
                            pinglun.setNoticeClick(true);
                            pinglun.update(mylist.get(position).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {}
                            });
                        }
                    }else if(mylist.get(position).getNoteType()==4){
                        Intent intent=new Intent();
                        intent.setClass(getContext(),candelete3.class);
                        startActivity(intent);
                        //...
                        BmobQuery<ListInfo> query=new BmobQuery<ListInfo>();
                        query.getObject(mylist.get(position).getBelongobjectid(), new QueryListener<ListInfo>() {
                            @Override
                            public void done(ListInfo listInfo, BmobException e) {
                                if(e==null){
                                    Intent intent = new Intent("and");
                                    intent.putExtra("text", "刷新评论界面");
                                    intent.putExtra("listinfo",listInfo);
                                    getActivity().sendBroadcast(intent);

                                    //设置被点击过的为灰色,并保存本次点击事件
                                    if(mylist.get(position).getNoteType()!=3){
                                        isCheck.put(position,true);
                                        finalViewholde.infohead.setTextColor(getResources().getColor(R.color.color_99));
                                        finalViewholde.infotext.setTextColor(getResources().getColor(R.color.color_99));
                                        //保存点击事件为true
                                        pinglun pinglun=new pinglun();
                                        pinglun.setNoticeClick(true);
                                        pinglun.update(mylist.get(position).getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {}
                                        });
                                    }
                                }
                            }
                        });
                    }else if(mylist.get(position).getNoteType()==2&!mylist.get(position).getIsjX()){
                        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        BmobQuery<MyUser> query=new BmobQuery<MyUser>();
                        query.getObject(mylist.get(position).getTomyuserid(), new QueryListener<MyUser>() {
                            @Override
                            public void done(final MyUser myUser, BmobException e) {
                                if(e==null){
                                    if(null!=myUser.getJXpipei()){
                                        if(myUser.getJXpipei().equals(user.getObjectId())){
                                            //可以进行匹配
                                            builder.setMessage("是否开始匹配");
                                            builder.setNegativeButton("稍等", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    builder.create().cancel();
                                                }
                                            });
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if((System.currentTimeMillis()-myUser.getMillis()<60000)){
                                                        if(!MyApplication.isblack){
                                                            Toast.makeText(getContext(),"请先取消当前匹配任务",Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            //设置对方的myuser，让对方跳转
                                                            User user = BmobUser.getCurrentUser(User.class);
                                                            MyUser myUser1=new MyUser();
                                                            myUser1.setAreJianting(true);
                                                            myUser1.setWasMated("即兴匹配");
                                                            myUser1.setJXpipei2(user.getObjectId());
                                                            myUser1.setJXpipei2avatr(user.getAvatar());
                                                            myUser1.setJXpipei2name(user.getUsername());
                                                            myUser1.setOtherObjectId(sp.getString("userMateID", ""));
                                                            myUser1.update(mylist.get(position).getTomyuserid(), new UpdateListener() {
                                                                @Override
                                                                public void done(BmobException e) {
                                                                    if(e==null){
                                                                        //自己跳转
                                                                        MyApplication.isJXpipei=true;
                                                                        MyApplication.OtherObjectId=mylist.get(position).getTomyuserid();
                                                                        MyApplication.isMateChatActivity = true;
                                                                        BmobIMUserInfo MateInfo = new BmobIMUserInfo(mylist.get(position).getUserid(), mylist.get(position).getUsername(),mylist.get(position).getUserimage());
                                                                        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(MateInfo, false, null);
                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putSerializable("c", c);
                                                                        Intent intent=new Intent();
                                                                        intent.setClass(getContext(),ChatActivity.class);
                                                                        intent.putExtra(getActivity().getPackageName(),bundle);
                                                                        startActivity(intent);

                                                                        //如果是首次匹配，把首次的标识去掉
                                                                        if(sp.getBoolean("isfirstin",false)){
                                                                            SharedPreferences.Editor editor= welcome.sp.edit();
                                                                            editor.putBoolean("isfirstin",false);
                                                                            editor.apply();
                                                                        }

                                                                        //跳转完了设置不可跳转
                                                                        pinglun pinglun=new pinglun();
                                                                        pinglun.setIsjX(true);
                                                                        pinglun.update(mylist.get(position).getObjectId(), new UpdateListener() {
                                                                            @Override
                                                                            public void done(BmobException e) {
                                                                                if(e!=null){
                                                                                    Log.d("xzf",""+e.getErrorCode()+e.getMessage());
                                                                                }
                                                                            }
                                                                        });

                                                                        //设置被点击过的为灰色,并保存本次点击事件
                                                                        if(mylist.get(position).getNoteType()!=3){
                                                                            isCheck.put(position,true);
                                                                            finalViewholde.infohead.setTextColor(getResources().getColor(R.color.color_99));
                                                                            finalViewholde.infotext.setTextColor(getResources().getColor(R.color.color_99));
                                                                            //保存点击事件为true
                                                                            pinglun pinglun2=new pinglun();
                                                                            pinglun2.setNoticeClick(true);
                                                                            pinglun2.update(mylist.get(position).getObjectId(), new UpdateListener() {
                                                                                @Override
                                                                                public void done(BmobException e) {}
                                                                            });
                                                                        }
                                                                    }else{
                                                                        finalViewholde.infohead.setTextColor(getResources().getColor(R.color.coco));
                                                                        finalViewholde.infotext.setTextColor(getResources().getColor(R.color.coco));
                                                                        Log.d("bmob",""+e.getErrorCode()+e.getMessage());
                                                                        Toast.makeText(getContext(),"未知错误，请重新点击",Toast.LENGTH_SHORT).show();

                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }else{
                                                        Toast.makeText(getContext(),"对方当前不在线，请稍候重试",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            builder.create().show();
                                        }else{
                                            builder.setTitle("提示");
                                            builder.setMessage("对方已更换匹配人选，本次匹配取消");
                                            builder.create().show();
                                            Toast.makeText(getContext(),"对方已更换匹配人选，本次匹配取消",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }else{
                                    Log.d("xzf",e.getErrorCode()+e.getMessage());
                                }
                            }
                        });
                    }else if(mylist.get(position).getNoteType()==2&mylist.get(position).getIsjX()){
                        Toast.makeText(getContext(),"你点击的匹配已经完成",Toast.LENGTH_SHORT).show();

                        //设置被点击过的为灰色,并保存本次点击事件
                        if(mylist.get(position).getNoteType()!=3){
                            isCheck.put(position,true);
                            finalViewholde.infohead.setTextColor(getResources().getColor(R.color.color_99));
                            finalViewholde.infotext.setTextColor(getResources().getColor(R.color.color_99));
                            //保存点击事件为true
                            pinglun pinglun=new pinglun();
                            pinglun.setNoticeClick(true);
                            pinglun.update(mylist.get(position).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {}
                            });
                        }
                    }
                }
            });
            viewholde.infoimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mylist.get(position).getNoteType()!=3){
                        //头像
                        if(null!=mylist.get(position).getUserid()){
                            Intent intent=new Intent();
                            intent.setClass(getContext(),userinfomation.class);
                            intent.putExtra("objectid",mylist.get(position).getUserid());
                            startActivity(intent);
                        }else{
                            Toast.makeText(getContext(),"对方ID不合法",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            return convertView;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState==SCROLL_STATE_IDLE) {
                //开始加载任务
                imageLoaderAsync.loadimage(mstart, mend,urls,tags);
            }else {
                //停止加载任务
                imageLoaderAsync.cancellALLTask();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //第一个可见元素和最后一个可见元素
            mstart=firstVisibleItem;
            mend=firstVisibleItem+visibleItemCount;
            Log.d("zpf","mstart:"+mstart);
            Log.d("zpf","mend:"+mend);
            //这个是初始化的预加载
            if(mFirstIn&&visibleItemCount>0){
                imageLoaderAsync.loadimage(mstart,mend,urls,tags);
                mFirstIn=false;
            }
        }

        private class viewholder {
             CircleImageView infoimg;
             TextView  infohead;
             TextView  infotext;
             LinearLayout bd;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendbroadcast(){
        Intent intent = new Intent("android.text_change");
        intent.putExtra("text", "开启客服");
        getActivity().sendBroadcast(intent);
    }

    //广播接收器
    public class MyBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            if (data.getString("fragment2").equals("fragment2")) {
                //接收广播，刷新界面
                if(null!=data.getSerializable("MSPANSCOMMI")){
                    pinglun pinglun=(pinglun) data.getSerializable("MSPANSCOMMI");
                    //防止一条数据被刷新两次
                    if(mylist.get(1)==pinglun&pinglun.getNoteType()==2){
                        Log.d("xzf","已刷新过");
                    }else{
                        mylist.add(1,(pinglun) data.getSerializable("MSPANSCOMMI"));
                        setmaps(mylist);
                        my.notifyDataSetChanged();
                        mFirstIn=true;
                    }
                }
            }
        }
    }

    private void setmaps(List<pinglun> mylist){
        urls=new String[mylist.size()];
        tags=new String[mylist.size()];
        for(int i=0;i<mylist.size();i++){
            urls[i]=mylist.get(i).getUserimage();
            tags[i]=mylist.get(i).getObjectId();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){

        }else{
            if(MyApplication.huihua){
                fragment2yuandian.setVisibility(View.VISIBLE);
            }else{
                fragment2yuandian.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MyApplication.huihua){
            fragment2yuandian.setVisibility(View.VISIBLE);
        }else{
           fragment2yuandian.setVisibility(View.GONE);
        }
    }

}
