package cn.bmob.imdemo.YiYou.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.ListInfo;
import cn.bmob.imdemo.YiYou.bean.ManagementTable;
import cn.bmob.imdemo.YiYou.bean.pinglun;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by DELL on 2017/4/21.
 */

public class candelete3 extends AppCompatActivity {
    private ListView youlistview;
    private TextView infos;
    private TextView names;
    private TextView index;
    private ScrollView scrollView;
    CircleImageView XC;
    ImageView infosex;
    TextView candezan;
    TextView candeza;
    TextView canping;
    EditText editText;
    Button voice;
    Button send;
    RelativeLayout shafa;
    private boolean panduan = true;
    private String keti;
    private String tomanageid;
    private String ketitext;
    ListView picture;
    private String ketiID;
    //动态注册广播接收器
    private IntentFilter intentFilter;
    private MyBroadReceiver broadReceiver;
    List<pinglun> listlist=new ArrayList<>();
    newnewadapter my;
    TextView time2;
    TextView nowint;
    String toid;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candelete3);
        if(null!=getIntent().getStringExtra("pinglunid")){
            BmobQuery<ListInfo> listInfoBmobQuery=new BmobQuery<>();
            listInfoBmobQuery.getObject(getIntent().getStringExtra("pinglunid"), new QueryListener<ListInfo>() {
                @Override
                public void done(ListInfo listInfo, BmobException e) {
                    if(e==null) {
                        setthispage(listInfo);
                    }
                }
            });
        }
        scrollView = (ScrollView) findViewById(R.id.srco);
        scrollView.smoothScrollTo(0, 0);
        shafa = (RelativeLayout) findViewById(R.id.shafa);
        editText = (EditText) findViewById(R.id.edit_msg);
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        XC = (CircleImageView) findViewById(R.id.XC);
        picture = (ListView) findViewById(R.id.picturelistview);
        //这个listview是下面的对话框，用的适配器是newnewlistview
        youlistview = (ListView) findViewById(R.id.newnewlistview);
        candezan = (TextView) findViewById(R.id.candezan);
        candeza = (TextView) findViewById(R.id.candeza);
        canping = (TextView) findViewById(R.id.canping);
        infos = (TextView) findViewById(R.id.infos);
        names = (TextView) findViewById(R.id.name);
        index = (TextView) findViewById(R.id.indexs);
        infosex = (ImageView) findViewById(R.id.insex);
        voice = (Button) findViewById(R.id.btn_chat_voice);
        send = (Button) findViewById(R.id.btn_chat_send);
        time2= (TextView) findViewById(R.id.time2);
        nowint= (TextView) findViewById(R.id.nowint);
        //动态注册广播接收器
        intentFilter = new IntentFilter();
        intentFilter.addAction("and");
        broadReceiver = new MyBroadReceiver();
        registerReceiver(broadReceiver, intentFilter);

        //状态栏透明
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //标题文字
        int nowin=getIntent().getIntExtra("nowint",0);
        if(nowin==1){
            nowint.setText("校园");
        }else if(nowin==2){
            nowint.setText("关注");
        }else{
            nowint.setText("我的");
        }

        if(null!=getIntent().getSerializableExtra("object")){
            setthispage((ListInfo) getIntent().getSerializableExtra("object"));
            Log.d("xzf","从动态打开的这个界面");
        }
        editText.setHint("赶快发表您的评论吧");
        voice.setVisibility(View.GONE);
        send.setVisibility(View.VISIBLE);

    }

    //刷新方法
    public void initate(String id) {
        BmobQuery<pinglun> myquery = new BmobQuery<pinglun>();
        myquery.order("createdAt");
        myquery.addWhereEqualTo("belongobjectid", id);
        myquery.findObjects(new FindListener<pinglun>() {
            @Override
            public void done(final List<pinglun> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        shafa.setVisibility(View.VISIBLE);
                        youlistview.setVisibility(View.GONE);
                    } else {
                        listlist.clear();
                        listlist.addAll(list);
                        shafa.setVisibility(View.GONE);
                        youlistview.setVisibility(View.VISIBLE);
                        my=new newnewadapter(getApplicationContext(),listlist);
                        youlistview.setAdapter(my);
                        setListViewHeightBasedOnChildren(youlistview);
                    }

                } else {
                    Log.d("fff", "查询失败" + e.getErrorCode() + e.getMessage());
                }
            }
        });
    }

    //这个方法是用于scrollview在嵌套listview时只显示一行的解决办法
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    private class newnewadapter extends BaseAdapter {
        private List<pinglun> mylist;
        LayoutInflater inflater;

        public newnewadapter(Context context, List<pinglun> mylist) {
            this.inflater = LayoutInflater.from(context);
            this.mylist = mylist;
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
            viewho viewho = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item5, null);
                viewho = new viewho();
                viewho.zhuti = (TextView) convertView.findViewById(R.id.zhuti2);
                viewho.index = (TextView) convertView.findViewById(R.id.index2);
                viewho.text = (TextView) convertView.findViewById(R.id.text);
                viewho.XC = (CircleImageView) convertView.findViewById(R.id.XC);
                viewho.cansex = (ImageView) convertView.findViewById(R.id.cansex);
                viewho.keti = (TextView) convertView.findViewById(R.id.keti);
                viewho.lay = (RelativeLayout) convertView.findViewById(R.id.lay);
                viewho.time3= (TextView) convertView.findViewById(R.id.time3);
                convertView.setTag(viewho);
            } else {
                viewho = (newnewadapter.viewho) convertView.getTag();
            }
            new ImageLoader().showImageByThread(viewho.XC, mylist.get(position).getUserimage());
            viewho.zhuti.setText(mylist.get(position).getZhuti());
            viewho.index.setText("南京财经大学");
            viewho.text.setText(mylist.get(position).getText());
            if(null!=mylist.get(position).getCreatedAt()){
                viewho.time3.setText(MyUtils.time(mylist.get(position).getCreatedAt()));
            }else{
                viewho.time3.setText(MyUtils.time(mylist.get(position).getSystem()));
            }
            if (mylist.get(position).getPanduan()) {
                viewho.keti.setVisibility(View.GONE);
            } else {
                viewho.keti.setVisibility(View.VISIBLE);
                String keti = "回复" + mylist.get(position).getKeti() + ":" + mylist.get(position).getKetitext();
                viewho.keti.setText(keti);
            }
            viewho.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setFocusable(true);
                    editText.requestFocus();
                    InputMethodManager inputManager =
                            (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                    editText.setHint("");
                    keti = mylist.get(position).getZhuti();
                    ketitext = mylist.get(position).getText();
                    panduan = false;
                    ketiID = mylist.get(position).getUserid();
                    tomanageid=mylist.get(position).getManageid();
                }
            });
            viewho.XC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToUserinfomation(mylist.get(position).getUserid());
                }
            });

            return convertView;
        }


        class viewho {
            TextView keti;
            CircleImageView XC;
            TextView zhuti;
            TextView text;
            TextView index;
            ImageView cansex;
            RelativeLayout lay;
            TextView time3;
        }


    }

    private class pictureadapter extends BaseAdapter {
        LayoutInflater inflater;
        String[] arr;


        public pictureadapter(Context context, String[] arr) {
            this.inflater = LayoutInflater.from(context);
            this.arr = arr;
        }

        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Object getItem(int position) {
            return arr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewho viewho = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.pictureitem, null);
                viewho = new viewho();
                viewho.picture = (ImageView) convertView.findViewById(R.id.picturea);
                convertView.setTag(viewho);
            } else {
                viewho = (pictureadapter.viewho) convertView.getTag();
            }
            new ImageLoader().showImageByThread(viewho.picture, arr[position]);
            return convertView;
        }

        class viewho {
            ImageView picture;
        }
    }

    //跳转用户信息界面
    private void ToUserinfomation(String id) {
        Intent intent = new Intent();
        intent.setClass(candelete3.this, userinfomation.class);
        intent.putExtra("objectid", id);
        startActivity(intent);
    }

    //设置本页面信息的方法
    private void setthispage(final ListInfo listInfo) {
        //设置顶部四个信息
        names.setText(listInfo.getName());
        infos.setText(listInfo.getInfo());
        index.setText(listInfo.getIndex());
        new ImageLoader().showImageByThread(XC, listInfo.getUserimage());
        XC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToUserinfomation(listInfo.getUserid());
            }
        });
        if (listInfo.getInfosex() != null & listInfo.getInfosex().equals("女")) {
            infosex.setBackgroundResource(R.drawable.listinfogirl);
        }
        if(null!=listInfo.getCreatedAt()){
            time2.setText(MyUtils.time(listInfo.getCreatedAt()));
        }else{
            time2.setText(MyUtils.time(listInfo.getSystem()));
        }
        candezan.setText("赞 " + listInfo.getZan());
        candeza.setText("砸 " + listInfo.getZa());
        canping.setText("评 " + listInfo.getPing());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "说点什么吧", Toast.LENGTH_SHORT).show();
                } else {
                    if(null!=listInfo.getObjectId()){
                        String info = editText.getText().toString();
                        editText.setText("");
                        //隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        // 隐藏软键盘
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        final User user = BmobUser.getCurrentUser(User.class);
                        pinglun ping = new pinglun();
                        ping.setUserimage(user.getAvatar());
                        ping.setText(info);
                        ping.setZhuti(user.getUsername());
                        ping.setBelongobjectid(listInfo.getObjectId());
                        ping.setSex(user.getSex());
                        ping.setNoticeClick(false);
                        ping.setNoteType(4);
                        ping.setManageid(user.getManageid());
                        ping.setUserid(user.getObjectId());
                        ping.setSystem(MyUtils.getDateTimeFromMillisecond(System.currentTimeMillis()));
                        if (panduan) {
                            toid=listInfo.getUserid();
                            ping.setT0manageid(listInfo.getUseridmanage());
                            ping.setTouserid(listInfo.getUserid());
                            ping.setPanduan(true);
                        } else {
                            toid=ketiID;
                            ping.setT0manageid(tomanageid);
                            ping.setTouserid(ketiID);
                            ping.setPanduan(false);
                            ping.setKeti(keti);
                            ping.setKetitext(ketitext);
                        }
                        //单例刷新
                        listlist.add(ping);
                        int i=listlist.size()-1;
                        Log.d("xzf9",listlist.get(i).getText());
                        if(shafa.getVisibility()==View.GONE){
                            my.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(youlistview);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }else if(shafa.getVisibility()==View.VISIBLE){
                            shafa.setVisibility(View.GONE);
                            youlistview.setVisibility(View.VISIBLE);
                            my=new newnewadapter(getApplicationContext(),listlist);
                            youlistview.setAdapter(my);
                            setListViewHeightBasedOnChildren(youlistview);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }
                        ping.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    //每次保存成功后让panduan恢复初始值？
                                    panduan = true;
                                    //让评论数量+1；//或者每次刷新动态的时候查询一下？
                                    ListInfo listInf = new ListInfo();
                                    listInf.increment("ping", 1);
                                    listInf.setObjectId(listInfo.getObjectId());
                                    listInf.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e!=null){
                                                Log.d("zpf",e.getErrorCode()+e.getMessage()+"");
                                            }
                                        }
                                    });
                                    //这个用来通知更新通知界面
                                    if(!toid.equals(user.getObjectId())){
                                        ManagementTable managementTable=new ManagementTable();
                                        if(panduan){
                                            managementTable.setObjectId(listInfo.getUseridmanage());
                                        }else{
                                            managementTable.setObjectId(tomanageid);
                                        }
                                        managementTable.increment("jishu",1);
                                        managementTable.setIsfenghao(false);
                                        managementTable.update(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e!=null){
                                                    Log.d("zpf",e.getErrorCode()+e.getMessage()+"");
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),"您的说说正在上传中，暂时无法发表评论，请刷新后重试",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //设置图片
        if(null!=listInfo.getArr()){
            picture.setDivider(null);
            picture.setAdapter(new pictureadapter(getApplicationContext(), listInfo.getArr()));
            setListViewHeightBasedOnChildren(picture);
        }
        if(null!=listInfo.getObjectId()){
            initate(listInfo.getObjectId());
        }
    }

    //广播接收器
    public class MyBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            if (data.getString("text").equals("刷新评论界面")) {
             setthispage((ListInfo) data.getSerializable("listinfo"));
            }

        }

    }
}












