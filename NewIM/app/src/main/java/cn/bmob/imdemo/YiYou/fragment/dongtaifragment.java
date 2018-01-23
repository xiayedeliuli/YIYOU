package cn.bmob.imdemo.YiYou.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.UI.candelete3;
import cn.bmob.imdemo.YiYou.UI.comfirmstate;
import cn.bmob.imdemo.YiYou.UI.newinfo;
import cn.bmob.imdemo.YiYou.UI.userinfomation;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;
import cn.bmob.imdemo.YiYou.Utils.ImageLoaderAsync;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.ListInfo;
import cn.bmob.imdemo.YiYou.mylogin.MyApplication;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;



/**
 * Created by DELL on 2017/4/20.
 */

public class dongtaifragment extends android.support.v4.app.Fragment  {
    private  ListView listView;
    private Context context;
    private List<ListInfo> listlist;
    private MyListAdapter my;
    private RelativeLayout jia;
    private TextView d1;
    private TextView d2;
    private TextView d3;
    int nowint;
    ImageView d1gone;
    ImageView d2gone;
    ImageView d3gone;
    SwipeRefreshLayout swipeRefreshLayout;
    String[] urls;
    String[] urls2;
    private boolean isfirstin;
    private boolean mfirstin;
    User user;
    private int mstart,mend,total;
    private ImageLoaderAsync imageLoaderAsync;
    private boolean mpanduan=false;
    private boolean npanduan=true;
    private boolean scrollbu=true;
    private Integer listsize=50;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dongtaifragment, null);
        listlist=new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.list1);
        context = view.getContext();
        user = BmobUser.getCurrentUser(User.class);
        jia= (RelativeLayout) view.findViewById(R.id.jia);
        d1= (TextView) view.findViewById(R.id.d1);
        d2= (TextView) view.findViewById(R.id.d2);
        d3= (TextView) view.findViewById(R.id.d3);
        d1gone= (ImageView) view.findViewById(R.id.d1gone);
        d2gone= (ImageView) view.findViewById(R.id.d2gone);
        d3gone= (ImageView) view.findViewById(R.id.d3gone);
        imageLoaderAsync=new ImageLoaderAsync(listView);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listsize=0;
                mfirstin=true;
                initate(nowint);
            }
        });
        //初次进入刷新界面
        if(null!=getArguments().getSerializable("dongtai")){
            List<ListInfo> list=(List<ListInfo>) getArguments().getSerializable("dongtai");
            if(list.size()>0){
                 listlist=(List<ListInfo>) getArguments().getSerializable("dongtai");
                 Log.d("xzf6",""+listlist.size());
                 setUrls(listlist);
                 mfirstin=true;
                 listView.setOnScrollListener(myscrolistener);
                 my = new MyListAdapter(listlist, context,listView);
                 listView.setAdapter(my);
            }
        }
        //动态注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("fragment");
        BroadReceiver broadReceiver = new BroadReceiver();
        getActivity().registerReceiver(broadReceiver, intentFilter);
        nowint=1;
        isfirstin=false;
//        mfirstin=true;
//        initate(1);
        d1.setEnabled(false);
        jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.dongjie){
                    Toast.makeText(getContext(),"您的账号已被冻结，请联系客服处理",Toast.LENGTH_LONG).show();
                }else{
                    User user = BmobUser.getCurrentUser(User.class);
                    if(user.isConfirmState()){
                        Intent intent=new Intent();
                        intent.setClass(context,newinfo.class);
                        startActivity(intent);
                    }else{
                        tocomfirm(getContext());
                    }
                }

            }
        });

        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listsize=0;
                mfirstin=true;
                swipeRefreshLayout.setRefreshing(true);
                listView.setOnScrollListener(noscrolistener);
                nowint=1;
                jia.setVisibility(View.VISIBLE);
                d1.setEnabled(false);
                d2.setEnabled(true);
                d3.setEnabled(true);
                initate(nowint);
                d1gone.setBackgroundResource(R.color.base_color_text_black);
                d2gone.setBackgroundResource(R.color.myorange);
                d3gone.setBackgroundResource(R.color.myorange);
            }
        });
        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=user.getFollow()){
                    if(user.getFollow().size()==0){
                        Toast.makeText(getContext(),"您还未有关注对象，请先关注其他小伙伴的动态",Toast.LENGTH_SHORT).show();
                    }else{
                        listsize=0;
                        mfirstin=true;
                        swipeRefreshLayout.setRefreshing(true);
                        listView.setOnScrollListener(noscrolistener);
                        nowint=2;
                        jia.setVisibility(View.GONE);
                        d1.setEnabled(true);
                        d2.setEnabled(false);
                        d3.setEnabled(true);
                        initate(nowint);
                        d1gone.setBackgroundResource(R.color.myorange);
                        d2gone.setBackgroundResource(R.color.base_color_text_black);
                        d3gone.setBackgroundResource(R.color.myorange);
                    }
                }else{
                    Toast.makeText(getContext(),"您还未有关注对象，请先关注其他小伙伴的动态",Toast.LENGTH_SHORT).show();
                }

            }
        });
        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listsize=0;
                mfirstin=true;
                swipeRefreshLayout.setRefreshing(true);
                listView.setOnScrollListener(noscrolistener);
                nowint=3;
                jia.setVisibility(View.GONE);
                d1.setEnabled(true);
                d2.setEnabled(true);
                d3.setEnabled(false);
                initate(nowint);
                d1gone.setBackgroundResource(R.color.myorange);
                d2gone.setBackgroundResource(R.color.myorange);
                d3gone.setBackgroundResource(R.color.base_color_text_black);
            }
        });
        return view;
    }


    //listview的动态刷新方法
    public void initate(final int i){
        final User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<ListInfo> myquery =new BmobQuery<ListInfo>();
        myquery.setLimit(50);
        myquery.setSkip(listsize);
        myquery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        myquery.order("-createdAt");
        if(i==3){
            myquery.addWhereEqualTo("userid",user.getObjectId());
        }
        myquery.findObjects(new FindListener<ListInfo>() {
            @Override
            public void done(final List<ListInfo> list, BmobException e) {
                if(e==null){
                    if(list.size()>0){
                            if(listsize==0){
                                listlist.clear();
                            }
                            if(i==2){
                                for(ListInfo s:list){
                                    if(user.getFollow().contains(s.getUserid())){
                                        listlist.add(s);
                                    }
                                }
                            }else{
                                listlist.addAll(list);
                            }
                            setUrls(listlist);
                            listsize=listlist.size();
                            if(!isfirstin){
                                my.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                                listView.setOnScrollListener(myscrolistener);
                            }else{
                                my = new MyListAdapter(listlist, context,listView);
                                listView.setAdapter(my);
                                swipeRefreshLayout.setRefreshing(false);
                                listView.setOnScrollListener(myscrolistener);
                                isfirstin=false;
                            }
                    }else{
                        if(nowint==3&listsize==0){
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(),"您还没有发表过动态",Toast.LENGTH_SHORT).show();
                        }else{
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(),"已经到底了",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Log.d("zpf","查询失败"+e.getErrorCode()+e.getMessage());
                }
            }
        });
    }

    //广播接收器
    public class BroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            if(null!=data.getString("frag")){
                if(data.getString("frag").equals("上传")){
                    //单例刷新
                    if(null!=data.getSerializable("frag2")){
                        ListInfo listInfo= (ListInfo) data.getSerializable("frag2");
                        listlist.add(0,listInfo);
                        setUrls(listlist);
                        my.notifyDataSetChanged();
                    }

                }else if(data.getString("frag").equals("完成")){
                    //数据上传完收到通知后刷新数据
                    Handler handler=new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"动态上传成功",Toast.LENGTH_SHORT).show();
                            mfirstin=true;
                            //查询第一条数据，加到原来的list中，再刷新
                            BmobQuery<ListInfo> myquery=new BmobQuery<ListInfo>();
                            myquery.setLimit(1);
                            myquery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
                            myquery.order("-createdAt");
                            myquery.findObjects(new FindListener<ListInfo>() {
                                @Override
                                public void done(List<ListInfo> list, BmobException e) {
                                    if(e==null){
                                        for(ListInfo m:list){
                                            if(!m.getObjectId().equals(listlist.get(0).getObjectId())){
                                                listlist.remove(0);
                                                listlist.add(0,m);
                                                setUrls(listlist);
                                                my.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }
                            });

                        }
                    });
                }
            }
        }
    }


     private class MyListAdapter extends BaseAdapter{
        private List<ListInfo> mylist;
        LayoutInflater inflater;

        //为了防止控件的复用，设置一个hashmap里存入每一项的postion和值，来进行判断
        HashMap<Integer,Boolean> isCheck;
        HashMap<Integer,Boolean> isSelect;

        private MyListAdapter(List<ListInfo> mylist, Context context, ListView listView) {
            this.inflater = LayoutInflater.from(context);
            this.mylist = mylist;
            //初始化这个类是保证只有一个缓存类被创建
            //hashmap的初始化
            isCheck = new HashMap<>();
            isSelect = new HashMap<>();
            initData();
        }

        private void initData() {
            for(int i = 0;i< mylist.size();i++){
                isCheck.put(i,false);
                isSelect.put(i,false);
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
                havePictureView = inflater.inflate(R.layout.item3, null);
                viewholde = new viewholder();
                viewholde.layous = (RelativeLayout) havePictureView.findViewById(R.id.layous);
                viewholde.name = (TextView) havePictureView.findViewById(R.id.name);
                viewholde.infosex = (ImageView) havePictureView.findViewById(R.id.infosex);
                viewholde.index = (TextView) havePictureView.findViewById(R.id.indexs);
                viewholde.info = (TextView) havePictureView.findViewById(R.id.infos);
                viewholde.zan = (TextView) havePictureView.findViewById(R.id.zan2);
                viewholde.za = (TextView) havePictureView.findViewById(R.id.za2);
                viewholde.jpg = (ImageView) havePictureView.findViewById(R.id.jpg);
                viewholde.ping = (TextView) havePictureView.findViewById(R.id.ping2);
                viewholde.XC = (CircleImageView) havePictureView.findViewById(R.id.XC);
                viewholde.zanlay = (LinearLayout) havePictureView.findViewById(R.id.zan);
                viewholde.zalay = (LinearLayout) havePictureView.findViewById(R.id.za);
                viewholde.smile = (ImageView) havePictureView.findViewById(R.id.zan1);
                viewholde.angry = (ImageView) havePictureView.findViewById(R.id.za1);
                viewholde.time = (TextView) havePictureView.findViewById(R.id.time);
                viewholde.pinglay = (LinearLayout) havePictureView.findViewById(R.id.ping);
                havePictureView.setTag(viewholde);
                convertView = havePictureView;
            } else {
                viewholde = (viewholder) convertView.getTag();
            }
            viewholde.zan.setTag(position);
            viewholde.jpg.setImageResource(R.color.text_gray);
            viewholde.name.setText(mylist.get(position).getName());
            if (null != mylist.get(position).getName()) {
                viewholde.index.setText(mylist.get(position).getIndex());
            }
            viewholde.info.setText(mylist.get(position).getInfo());
            if (null != mylist.get(position).getCreatedAt()) {
                viewholde.time.setText(MyUtils.time(mylist.get(position).getCreatedAt()));
            } else {
                viewholde.time.setText(MyUtils.time(mylist.get(position).getSystem()));
            }
            if (mylist.get(position).getZan() > 0) {
                viewholde.zan.setText(mylist.get(position).getZan().toString());
            } else {
                viewholde.zan.setText("赞");
            }
            if (mylist.get(position).getZa() > 0) {
                viewholde.za.setText(mylist.get(position).getZa().toString());
            } else {
                viewholde.za.setText("砸");
            }
            if (null != mylist.get(position).getPing()) {
                if (mylist.get(position).getPing() > 0) {
                    viewholde.ping.setText(mylist.get(position).getPing().toString());
                }
            } else {
                viewholde.ping.setText("评");
            }
            if (mylist.get(position).getInfosex() != null & mylist.get(position).getInfosex().equals("女")) {
                viewholde.infosex.setBackgroundResource(R.drawable.listinfogirl);
            } else {
                viewholde.infosex.setBackgroundResource(R.drawable.listinfoboy);
            }
            //为了解决图片复用的加载问题
            //设置一个身份标识,把这一项需要的图片和url地址进行绑定。
            //因为复用机制，如果是上一张图片，则url和当前的url不同
            String myXC = mylist.get(position).getUserimage();
            viewholde.XC.setTag(myXC + position);
            imageLoaderAsync.showImageByAsync(viewholde.XC, myXC);

            String firsturl = mylist.get(position).getFirsturl();
            if (firsturl == null || firsturl.equals("")) {
                viewholde.jpg.setVisibility(View.GONE);
            } else {
                viewholde.jpg.setVisibility(View.VISIBLE);
                viewholde.jpg.setTag(firsturl + position);
                imageLoaderAsync.showImageByAsync(viewholde.jpg, firsturl);
            }
            final int zan = mylist.get(position).getZan() + 1;
            final int za = mylist.get(position).getZa() + 1;
            if (null != isCheck.get(position)) {
                if (isCheck.get(position)) {
                    viewholde.zan.setText("" + zan);
                    viewholde.smile.setImageResource(R.drawable.zan2bian);
                } else {
                    viewholde.smile.setImageResource(R.drawable.zan2);
                    if (mylist.get(position).getZan() == 0) {
                        viewholde.zan.setText("赞");
                    } else {
                        viewholde.zan.setText(mylist.get(position).getZan().toString());
                    }
                }
            }
            if (null != isSelect.get(position)) {
                if (isSelect.get(position)) {
                    viewholde.za.setText("" + za);
                    viewholde.angry.setImageResource(R.drawable.za2bian);
                } else {
                    viewholde.angry.setImageResource(R.drawable.za2);
                    if (mylist.get(position).getZa() == 0) {
                        viewholde.za.setText("砸");
                    } else {
                        viewholde.za.setText(mylist.get(position).getZa().toString());
                    }
                }
            }
            final viewholder finalViewholde = viewholde;
            viewholde.zanlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCheck.get(position)) {
                        isCheck.put(position, false);
                        finalViewholde.smile.setImageResource(R.drawable.zan2);
                        int myzan = zan - 1;
                        if (myzan > 0) {
                            finalViewholde.zan.setText("" + myzan);
                        } else {
                            finalViewholde.zan.setText("赞");
                        }
                        ListInfo listInfo = new ListInfo();
                        listInfo.increment("zan", -1);
                        listInfo.update(mylist.get(position).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                            }
                        });
                    } else {
                        isCheck.put(position, true);
                        finalViewholde.smile.setImageResource(R.drawable.zan2bian);
                        finalViewholde.zan.setText("" + zan);
                        ListInfo listInfo = new ListInfo();
                        listInfo.increment("zan", 1);
                        listInfo.update(mylist.get(position).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                            }
                        });
                    }
                }
            });
            viewholde.zalay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelect.get(position)) {
                        isSelect.put(position, false);
                        finalViewholde.angry.setImageResource(R.drawable.za2);
                        int myza = za - 1;
                        if (myza > 0) {
                            finalViewholde.za.setText("" + myza);
                        } else {
                            finalViewholde.za.setText("砸");
                        }
                        ListInfo listInfo = new ListInfo();
                        listInfo.increment("za", -1);
                        listInfo.update(mylist.get(position).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                            }
                        });
                    } else {
                        isSelect.put(position, true);
                        finalViewholde.angry.setImageResource(R.drawable.za2bian);
                        finalViewholde.za.setText("" + za);
                        ListInfo listInfo = new ListInfo();
                        listInfo.increment("za", 1);
                        listInfo.update(mylist.get(position).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                            }
                        });
                    }
                }
            });

            viewholde.pinglay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null == mylist.get(position).getObjectId()) {
                        Toast.makeText(getContext(), "您的说说正在上传中，暂时无法发表评论，请刷新后重试", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent();
                    intent.setClass(context, candelete3.class);
                    intent.putExtra("nowint", nowint);
                    intent.putExtra("object", mylist.get(position));
                    startActivity(intent);
                }
            });

            viewholde.XC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mylist.get(position).getUserid()) {
                        Intent intent = new Intent();
                        intent.setClass(getContext(), userinfomation.class);
                        intent.putExtra("objectid", mylist.get(position).getUserid());
                        intent.putExtra("manageid", mylist.get(position).getUseridmanage());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "对方ID不合法", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return convertView;
        }

        private class viewholder {
            TextView za;
            TextView zan;
            TextView name;
            TextView info;
            TextView index;
            ImageView jpg;
            TextView ping;
            CircleImageView XC;
            ImageView infosex;
            LinearLayout zanlay;
            LinearLayout zalay;
            LinearLayout pinglay;
            RelativeLayout layous;
            ImageView smile;
            ImageView angry;
            TextView time;
        }
    }

    private void setUrls(List<ListInfo> mylist){
        //这里保存的是图片的集合
        urls=new String[mylist.size()];
        for(int i=0;i<mylist.size();i++){
            urls[i]=mylist.get(i).getFirsturl();
        }
        //这个保存的是头像的集合
        urls2=new String[mylist.size()];
        for(int i=0;i<mylist.size();i++){
            urls2[i]=mylist.get(i).getUserimage();
        }

    }

    private void tocomfirm(final Context context){
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("您只需验证一下校区地理位置即可发送动态");
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
                intent.setClass(context,comfirmstate.class);
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    AbsListView.OnScrollListener noscrolistener =new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    AbsListView.OnScrollListener myscrolistener =new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState==SCROLL_STATE_IDLE) {
                //开始加载任务
                imageLoaderAsync.loadimage(mstart, mend,urls,urls2);
                if (mpanduan) {
                    endAnim();
                    mpanduan = false;
                }
            }else if(scrollState==SCROLL_STATE_TOUCH_SCROLL){
                if ((mend) == total&scrollbu) {
                    View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == listView.getHeight()) {
                        Log.d("ListView", "##### 滚动到底部 ######");
                        initate(nowint);
                        scrollbu=false;
                        swipeRefreshLayout.setRefreshing(true);
                    }
                }
                if(mend<total){
                    scrollbu=true;
                }
                //停止加载任务
                imageLoaderAsync.cancellALLTask();
            }
            else{
                if(npanduan){
                    startAnim();
                    npanduan=false;}
                //停止加载任务
                imageLoaderAsync.cancellALLTask();

            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //第一个可见元素和最后一个可见元素
            mstart=firstVisibleItem;
            mend=firstVisibleItem+visibleItemCount;
            total=totalItemCount;
            //这个是初始化的预加载
            if(mfirstin&&visibleItemCount>0&!swipeRefreshLayout.isRefreshing()){
                imageLoaderAsync.loadimage(mstart,mend,urls,urls2);
                mfirstin=false;
            }
        }
    };


    private void startAnim(){
        Log.d("xzx","开始动画");
        ObjectAnimator animator;
        animator = ObjectAnimator.ofFloat(jia,"translationX",0,250f).setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mpanduan=true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void endAnim() {
        Log.d("xzx","停止动画");
        ObjectAnimator animator;
        animator = ObjectAnimator.ofFloat(jia, "translationX", 250f, 0f).setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                npanduan=true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }


}






