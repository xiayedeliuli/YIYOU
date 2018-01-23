package cn.bmob.imdemo.YiYou.Mate;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.YiYou.Mate.MateDialog.MateDialog;
import cn.bmob.imdemo.YiYou.Mate.MateDialog.mClicklistener;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.ChangeFragment;
import cn.bmob.imdemo.YiYou.UI.changehead;
import cn.bmob.imdemo.YiYou.UI.comfirmstate;
import cn.bmob.imdemo.YiYou.UI.matchinfo;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.LastInfo;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.imdemo.YiYou.mylogin.MyApplication;
import cn.bmob.imdemo.ui.ChatActivity;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;
import cn.iwgang.countdownview.CountdownView;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * Created by DELL on 2017/5/21.
 */

@SuppressLint("ValidFragment")
public class MateFragment extends android.support.v4.app.Fragment {
    private RelativeLayout CatBillboard_Relayout;
    private ImageView Billboard_img;
    ListView listView;
    RelativeLayout noinfotx;
    CountdownView countdownView;
    LinearLayout MateStatus_Llayout;
    ImageView ICONset;
    ImageView tc;
    LinearLayout lunbo;
    LinearLayout twoiconlay;
    User user;
    ImageView dianji;
    TextView title;
    TextView description;
    //动态注册广播接收器
    private IntentFilter intentFilter;
    private MyBroadReceiver broadReceiver;
    ImageView three;
    TextView Nearbynumber_tex;
    ChangeFragment changeFragment;
    private MateAnimation mateAnimation;
    View rootview;
    //数据保存类
    private MateContains mateContains;
     BmobRealTimeData rtd;


    public MateFragment(ChangeFragment changeFragment) {
        this.changeFragment = changeFragment;
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.matefragment,container, false);
        initInflate(rootview);
        if(MyApplication.isshowtx2){
            noinfotx.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            CatBillboard_Relayout.setVisibility(View.GONE);
            MyApplication.isshowtx2=false;
        }
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user= BmobUser.getCurrentUser(User.class);
        mateAnimation=new MateAnimation(CatBillboard_Relayout,rootview);
        //这是匹配信息的界面
       if(!MyApplication.isshowtx2) {
           querylistview(false);
       }
        MyApplication.isblack=true;
        dianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().equals("开始匹配")){
                    if(!MyApplication.isblack){
                        dianji.setImageResource(R.drawable.dianji_leftbai);
                    }else{
                        dianji.setImageResource(R.drawable.dianji_left363636);
                    }
                    title.setText("正在进行");
                    description.setText("去看看您和小伙伴的约定，不能违约哦！");
                    CatBillboard_Relayout.setVisibility(View.GONE);

                }else{
                    if(!MyApplication.isblack){
                        dianji.setImageResource(R.drawable.dianjibai);
                    }else{
                        dianji.setImageResource(R.drawable.dianji363636);
                    }
                    title.setText("开始匹配");
                    description.setText("还没有小伙伴，赶快去匹配吧！");
                    CatBillboard_Relayout.setVisibility(View.VISIBLE);
                }

            }
        });
        //动态注册广播接收器
        intentFilter = new IntentFilter();
        intentFilter.addAction("android");
        broadReceiver = new MyBroadReceiver();
        getActivity().registerReceiver(broadReceiver, intentFilter);
        //退出匹配的点击方法
        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //切换fragment
                SendCancelBrocast();
                MateMethod.ResetMate();
            }
        });
        //匹配优先度的设定
        ICONset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchDialog();
            }
        });
        MateMethod.ResetMate();
        //正常匹配时候的动画
        normalAnimantion();
        QueryOnlineNumbers();
        //匹配的监听方法
        MateMonitor();
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment.change(v.getId());
            }
        });
    }





    //对话框
    public void startdialog() {
        MateDialog mateDialog = new MateDialog(getContext());
        mateDialog.setCliklistener(new mClicklistener(MateFragment.this));
        mateDialog.show();
    }
    private void MatchDialog(){
        final SharedPreferences.Editor editor=sp.edit();
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        final AlertDialog dialog=builder.create();
        final String[] arr={"项目优先","性别优先"};
        builder.setTitle("匹配优先度设定");
        int size=0;
        if(sp.getString("SecondChoice","").equals("项目")){
            size=0;
        }else{
            size=1;
        }
        builder.setSingleChoiceItems(arr,size, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putString("SecondChoice", arr[i].substring(0,2));
                editor.apply();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(),"设定成功，将于下次匹配开始生效",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }



    //设置匹配界面的背景图片
    private void matchimageview(ImageView imageView,String items){
        if(items.equals("图书馆")){
            imageView.setBackgroundResource(R.mipmap.tushuguan1);
        }else if(items.equals("电影院")) {
            imageView.setBackgroundResource(R.mipmap.dianyingyuan2);
        }else if(items.equals("KTV")){
            imageView.setBackgroundResource(R.mipmap.ktv1);
        }else if(items.equals("约牌")){
            imageView.setBackgroundResource(R.mipmap.yuepai2);
        }else if(items.equals("散步")){
            imageView.setBackgroundResource(R.mipmap.sanbu1);
        }else if(items.equals("羽毛球")){
            imageView.setBackgroundResource(R.mipmap.yumaoqiu1);
        }else if(items.equals("乒乓球")){
            imageView.setBackgroundResource(R.mipmap.pingpangqiu1);
        }else if(items.equals("篮球")){
            imageView.setBackgroundResource(R.mipmap.lanqiu1);
        }else if(items.equals("下午茶")){
            imageView.setBackgroundResource(R.mipmap.xiawucha1);
        }

    }
    //设置匹配信息界面的文字
    private void matchTEXT(TextView textView,String item,String space,TextView textView2,Long date){
        String itemspace=item+"-"+space;
        textView.setText(itemspace);
        textView2.setText(MyUtils.getStringDate2(date));
    }

    public class fragment1adapter extends BaseAdapter{
        List<LastInfo> list;
        LayoutInflater layoutInflater;
        Context context;

        public fragment1adapter(Context context,List<LastInfo> list) {
            this.context=context;
            this.list=list;
            this.layoutInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
                return list.size();
        }

        public String user(int i){
            if(list.get(i).getAID().equals(user.getObjectId())){
                return "A";
            }else{
                return "B";
            }
        }

        public LastInfo getlist(int i){
            return list.get(i);
        }


        @Override
        public  Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

                ViewHolder holder=null;
                if(convertView==null){
                    convertView =layoutInflater.inflate(R.layout.zuizhongxuqiu,null);
                    holder=new ViewHolder();
                    holder.imageView= (ImageView) convertView.findViewById(R.id.imageview3);
                    holder.namespace= (TextView) convertView.findViewById(R.id.pipeixiangmu3);
                    holder.time= (TextView) convertView.findViewById(R.id.pipeishijian3);
                    holder.countdownView= (CountdownView) convertView.findViewById(R.id.counttime3);
                    holder.circleImageView= (CircleImageView) convertView.findViewById(R.id.pipeitou3);
                    holder.yinzhang= (ImageView) convertView.findViewById(R.id.yinzhang);
                    convertView.setTag(holder);
                }else
                {
                    holder= (ViewHolder) convertView.getTag();
                }
                //图片
                matchimageview(holder.imageView,list.get(i).getLastItem());
                //两个textview
                matchTEXT(holder.namespace,list.get(i).getLastItem(),list.get(i).getLastSpace(),holder.time,list.get(i).getLastTime());
                //时间
                Long date=list.get(i).getLastTime()-System.currentTimeMillis();
                if(date>0){
                    holder.countdownView.start(date); // 毫秒
                }else{
                    holder.countdownView.start(1);
                }
                //头像
                if(list.get(i).getAID().equals(user.getObjectId())){
                    new ImageLoader().showImageByThread(holder.circleImageView,list.get(i).getBavater());
                }else{
                    new ImageLoader().showImageByThread(holder.circleImageView,list.get(i).getAavater());
                }

                return convertView;
            }
        }
        public class ViewHolder
        {
            ImageView yinzhang;
            ImageView imageView;
            TextView namespace;
            TextView time;
            CountdownView countdownView;
            CircleImageView circleImageView;

        }


    //listview的展示方法
private void listviewclick(List<LastInfo> mylist){
    final fragment1adapter myadapter=new fragment1adapter(getContext(),mylist);
    listView.setAdapter(myadapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(MyApplication.isblack){
                Intent  intent=new Intent();
                intent.setClass(getContext(),matchinfo.class);
                intent.putExtra("data",myadapter.getlist(i));
                intent.putExtra("hr",myadapter.user(i));
                startActivity(intent);
            }else{
                Toast.makeText(getContext(),"匹配阶段无法进入其他匹配界面",Toast.LENGTH_SHORT).show();
            }

        }
    });

}

    //牌子上下移动的动画
    public void normalAnimantion(){
       mateAnimation.startTranslateAnimation();
       Billboard_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果已经开始即时匹配，提示一下是否覆盖匹配需求，并设置JX匹配为""，让对方无法跳转
                if(MyApplication.isJXpipei){
                    final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("你已向小伙伴发送过匹配需求，是否重新开始随机匹配");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.create().cancel();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User user = BmobUser.getCurrentUser(User.class);
                            //如果被冻结了
                            if(MyApplication.dongjie){
                                Toast.makeText(getContext(),"您的账号已被冻结，请联系客服处理",Toast.LENGTH_SHORT).show();
                            }else{
                                //如果是第一次
                                if(sp.getBoolean("isfirstin",false)){
                                    //只需要验证下地理位置即可
                                    if(user.isConfirmState()){
                                        startdialog();
                                    }else{
                                        tocomfirm(getContext(),"鉴于您首次匹配，仅需验证校区地理位置即可匹配");
                                    }
                                }else
                                //不是首次匹配
                                {
                                    if(user.isConfirmState()&MyApplication.studentcomfirm){
                                        startdialog();
                                    }else if(user.isConfirmState()&!MyApplication.studentcomfirm&user.getRealavatar()
                                            .length()>0&user.getStudentid().length()>0&user.getStudentkey().length()>0){
                                        Toast.makeText(getContext(),"您提交过验证信息，请稍等客服处理,验证成功后可开始匹配",Toast.LENGTH_SHORT).show();
                                    } else{
                                        tocomfirm(getContext(),"您需要验证校区位置和学号后方可匹配");
                                    }
                                }
                            }


                        }
                    });
                    builder.create().show();
                }else {
                    User user = BmobUser.getCurrentUser(User.class);
                    //如果被冻结了
                    if(MyApplication.dongjie){
                        Toast.makeText(getContext(),"您的账号已被冻结，请联系客服处理",Toast.LENGTH_SHORT).show();
                    }else{
                        //如果是第一次
                        if(sp.getBoolean("isfirstin",false)){
                            //只需要验证下地理位置即可
                            if(user.isConfirmState()){
                                startdialog();
                            }else{
                                startdialog();
//                                tocomfirm(getContext(),"鉴于您首次匹配，仅需验证校区地理位置即可匹配");
                            }
                        }else
                        //不是首次匹配
                        {
                            if(user.isConfirmState()&MyApplication.studentcomfirm){
                                startdialog();
                            }else if(user.isConfirmState()&!MyApplication.studentcomfirm&user.getRealavatar()
                                    .length()>0&user.getStudentid().length()>0&user.getStudentkey().length()>0){
                                Toast.makeText(getContext(),"您提交过验证信息，请稍等客服处理,验证成功后可开始匹配",Toast.LENGTH_SHORT).show();
                            } else{
                                tocomfirm(getContext(),"您需要验证校区位置和学号后方可匹配");
                            }
                        }
                    }
                }
            }
        });
    }






    //发送广播，让主界面刷新fragment的方法
    public void  SendCancelBrocast(){
        Intent intent = new Intent("android.text_change");
        intent.putExtra("text", "刷新");
        getActivity().sendBroadcast(intent);
    }


    //查询listview并刷新他的方法-正在进行的项目
     private void querylistview(Boolean sds){
         if(sds){
         dianji.setImageResource(R.drawable.dianji_left363636);
             title.setText("正在进行");
             description.setText("去看看您和小伙伴的约定，不能违约哦！");
             CatBillboard_Relayout.setVisibility(View.GONE);
         }
         final Long date=System.currentTimeMillis()-1200000;
         final User user = BmobUser.getCurrentUser(User.class);
         BmobQuery<LastInfo> eq1 = new BmobQuery<LastInfo>();
         eq1.addWhereEqualTo("AID", user.getObjectId());
         eq1.addWhereNotEqualTo("isDELETE",true);
         eq1.addWhereGreaterThanOrEqualTo("LastTime",date);
         BmobQuery<LastInfo> eq2 = new BmobQuery<LastInfo>();
         eq2.addWhereEqualTo("BID", user.getObjectId());
         eq2.addWhereNotEqualTo("isDELETE",true);
         eq2.addWhereGreaterThanOrEqualTo("LastTime",date);
         List<BmobQuery<LastInfo>> queries = new ArrayList<BmobQuery<LastInfo>>();
         queries.add(eq1);
         queries.add(eq2);
         BmobQuery<LastInfo> mainQuery = new BmobQuery<LastInfo>();
         mainQuery.or(queries);
         mainQuery.findObjects(new FindListener<LastInfo>() {
             @Override
             public void done(List<LastInfo> list, BmobException e) {
                 if(e==null){
                     if(list.size()==0){
                         noinfotx.setVisibility(View.VISIBLE);
                         listView.setVisibility(View.GONE);
                     }else{
                         noinfotx.setVisibility(View.GONE);
                         listView.setVisibility(View.VISIBLE);
                         listView.setDivider(null);
                         listviewclick(list);
                     }
                 }else{
                     Log.d("xzf","查询失败"+e.getErrorCode()+e.getMessage());
                 }
             }
         });
     }

    //广播接收器
    public class MyBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            if(data.getString("te").equals("刷")){
                querylistview(true);
            }else if(data.getString("te").equals("新的匹配")){
                countdownView.start(300000);
            }

        }


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



    @Override
    public void onResume() {
        super.onResume();
        Log.d("xzf","在前台");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("xzf","暂停");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            Log.d("xzf","不可见");
        }else{
            Log.d("xzf","可见");
        }
    }

    @Override
    public void onDestroy() {
        if(rtd.isConnected()){

        }
        super.onDestroy();
    }

    //查询在线人数
    private void QueryOnlineNumbers(){
        BmobQuery<User> query=new BmobQuery<>();
        query.addWhereEqualTo("index",user.getIndex());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    Nearbynumber_tex.setText("附近小伙伴:"+ list.size());
                }
            }
        });
    }

    /*匹配监听方法*/
        private void MateMonitor(){
        rtd = new BmobRealTimeData();
        rtd.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {
                BmobQuery<Mate> query=new BmobQuery<Mate>();
                query.getObject(sp.getString("Mate", ""), new QueryListener<Mate>() {
                    @Override
                    public void done(Mate user, BmobException e) {
                        if(e==null){
                            if(user.getMateUserFlag()&&null!=user.getMateUserActivity()){
                                Log.d("xzf","Mate查询方法");
                                String[] Info=new String[3];
                                System.arraycopy(user.getMateUserActivity(),0,Info,0,3);
                                Log.d("xzf","1:"+Info[0]+"  /2:"+Info[1]+"  /3："+Info[2]);
                                    Log.d("xzf","Mate会话跳转");
                                    //进行会话的跳转
                                    BmobIMUserInfo MateInfo = new BmobIMUserInfo( Info[0], Info[1],Info[2]);
                                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(MateInfo, false, null);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("c", c);
                                    startActivity(ChatActivity.class, bundle, false);
                                   //匹配成功对表的修改
                                    MateMethod.ResetMate();
                            }
                        }
                    }
                });
            }

            @Override
            public void onConnectCompleted(Exception ex) {
                if(rtd.isConnected()){
                    rtd.subRowUpdate("Mate",sp.getString("Mate", ""));
                    Log.d("xzf", "Mate监听连接成功:"+rtd.isConnected());
                }else{
                    Log.d("xzf", "Mate监听连接失败:"+ex.getMessage());
                }
            }
        });
    }





    public void startActivity(Class<? extends Activity> target, Bundle bundle, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), target);
        if (bundle != null)
            intent.putExtra(getActivity().getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            getActivity().finish();
    }

    public void initInflate(View myview){
        CatBillboard_Relayout= (RelativeLayout) myview.findViewById(R.id.CatBillboard_Relayout);
        Billboard_img= (ImageView) myview.findViewById(R.id.Billboard_img);
        listView= (ListView) myview.findViewById(R.id.fragment1listview);
        noinfotx= (RelativeLayout) myview.findViewById(R.id.noinfotx);
        countdownView= (CountdownView) myview.findViewById(R.id.countdownView);
        MateStatus_Llayout= (LinearLayout) myview.findViewById(R.id.MateStatus_Llayout);
        dianji= (ImageView) myview.findViewById(R.id.RightHand_img);
        ICONset= (ImageView) myview.findViewById(R.id.set);
        Nearbynumber_tex= (TextView) myview.findViewById(R.id.Nearbynumber_tex);
        twoiconlay= (LinearLayout) myview.findViewById(R.id.twoiconlay);
        lunbo= (LinearLayout) myview.findViewById(R.id.lunbo);
        title= (TextView) myview.findViewById(R.id.title);
        description= (TextView) myview.findViewById(R.id.description);
        tc= (ImageView) myview.findViewById(R.id.tc);
        three= (ImageView) myview.findViewById(R.id.opendrawlayout_img);
        CatBillboard_Relayout= (RelativeLayout) myview.findViewById(R.id.CatBillboard_Relayout);
        mateContains=new MateContains();
    }

    public MateContains getMateContains() {
        return mateContains;
    }

    public MateAnimation getMateAnimation() {
        return mateAnimation;
    }
}

