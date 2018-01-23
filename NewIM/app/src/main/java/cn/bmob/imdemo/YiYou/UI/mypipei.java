package cn.bmob.imdemo.YiYou.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.LastInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by DELL on 2017/6/1.
 */

public class mypipei extends BaseActivity {
    LinearLayout running;
    LinearLayout pass;
    LinearLayout allmatch;
    ListView runlistview;
    ListView passlistview;
    ListView alllistview;
    ImageView matchback;
    ImageView matchgone1;
    ImageView matchgone2;
    ImageView matchgone3;
    User user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypipei);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        user= BmobUser.getCurrentUser(User.class);
        final User user = BmobUser.getCurrentUser(User.class);
        runlistview= (ListView) findViewById(R.id.matchlist);
        passlistview= (ListView) findViewById(R.id.passlistview);
        alllistview= (ListView) findViewById(R.id.alllistview);
        matchgone1= (ImageView) findViewById(R.id.matchgone1);
        matchgone2= (ImageView) findViewById(R.id.matchgone2);
        matchgone3= (ImageView) findViewById(R.id.matchgone3);
        running= (LinearLayout) findViewById(R.id.running);
        pass= (LinearLayout) findViewById(R.id.pass);
        allmatch= (LinearLayout) findViewById(R.id.allmatch);
        matchback= (ImageView) findViewById(R.id.matchback);
        matchback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final Long date=System.currentTimeMillis()-1200000;
        //正在进行中
        BmobQuery<LastInfo> eq1 = new BmobQuery<LastInfo>();
        eq1.addWhereEqualTo("AID", user.getObjectId());
        eq1.addWhereGreaterThanOrEqualTo("LastTime",date);
        eq1.addWhereNotEqualTo("isDELETE",true);
        BmobQuery<LastInfo> eq2 = new BmobQuery<LastInfo>();
        eq2.addWhereEqualTo("BID", user.getObjectId());
        eq2.addWhereGreaterThanOrEqualTo("LastTime",date);
        eq2.addWhereNotEqualTo("isDELETE",true);
        List<BmobQuery<LastInfo>> queries = new ArrayList<BmobQuery<LastInfo>>();
        queries.add(eq1);
        queries.add(eq2);
        BmobQuery<LastInfo> mainQuery = new BmobQuery<LastInfo>();
        mainQuery.or(queries);
        mainQuery.findObjects(new FindListener<LastInfo>() {
            @Override
            public void done(final List<LastInfo> list, BmobException e) {
                if(e==null){
                    runlistview.setAdapter(new fragment1adapter(mypipei.this,list));
                    runlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent();
                            intent.setClass(mypipei.this,matchinfo.class);
                            intent.putExtra("data",list.get(position));
                            if(list.get(position).getAID().equals(user.getObjectId())){
                                intent.putExtra("hr","A");
                            }else{
                                intent.putExtra("hr","B");
                            }
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        //已结束
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
                    passlistview.setAdapter(new fragment1adapter(mypipei.this,list));
                    passlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent();
                            intent.setClass(mypipei.this,matchinfo.class);
                            intent.putExtra("data",list.get(position));
                            if(list.get(position).getAID().equals(user.getObjectId())){
                                intent.putExtra("hr","A");
                            }else{
                                intent.putExtra("hr","B");
                            }
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        //全部
        //已结束
        BmobQuery<LastInfo> aeq1 = new BmobQuery<LastInfo>();
        aeq1.addWhereEqualTo("AID", user.getObjectId());
        aeq1.addWhereNotEqualTo("isDELETE",true);
        BmobQuery<LastInfo> aeq2 = new BmobQuery<LastInfo>();
        aeq2.addWhereEqualTo("BID", user.getObjectId());
        aeq2.addWhereNotEqualTo("isDELETE",true);
        List<BmobQuery<LastInfo>> aqueries = new ArrayList<BmobQuery<LastInfo>>();
        aqueries.add(aeq1);
        aqueries.add(aeq2);
        BmobQuery<LastInfo> amainQuery = new BmobQuery<LastInfo>();
        amainQuery.or(aqueries);
        amainQuery.findObjects(new FindListener<LastInfo>() {
            @Override
            public void done(final List<LastInfo> list, BmobException e) {
                if(e==null){
                    alllistview.setAdapter(new fragment1adapter(mypipei.this,list));
                    alllistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent();
                            intent.setClass(mypipei.this,matchinfo.class);
                            intent.putExtra("data",list.get(position));
                            if(list.get(position).getAID().equals(user.getObjectId())){
                                intent.putExtra("hr","A");
                            }else{
                                intent.putExtra("hr","B");
                            }
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runlistview.setVisibility(View.VISIBLE);
                passlistview.setVisibility(View.GONE);
                alllistview.setVisibility(View.GONE);

                matchgone1.setBackgroundResource(R.color.myorange);
                matchgone2.setBackgroundResource(R.color.base_color_text_white);
                matchgone3.setBackgroundResource(R.color.base_color_text_white);
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runlistview.setVisibility(View.GONE);
                passlistview.setVisibility(View.VISIBLE);
                alllistview.setVisibility(View.GONE);

                matchgone1.setBackgroundResource(R.color.base_color_text_white);
                matchgone2.setBackgroundResource(R.color.myorange);
                matchgone3.setBackgroundResource(R.color.base_color_text_white);
            }
        });

        allmatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runlistview.setVisibility(View.GONE);
                passlistview.setVisibility(View.GONE);
                alllistview.setVisibility(View.VISIBLE);

                matchgone1.setBackgroundResource(R.color.base_color_text_white);
                matchgone2.setBackgroundResource(R.color.base_color_text_white);
                matchgone3.setBackgroundResource(R.color.myorange);
            }
        });



    }

    public class fragment1adapter extends BaseAdapter {
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

        @Override
        public Object getItem(int i) {
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
            boolean isA = list.get(i).getAID().equals(user.getObjectId());
            //印章
            long chatime=System.currentTimeMillis()-list.get(i).getLastTime();
            //超过20分钟，且没有确认，是违约单,否则是完成单
            if(chatime>1200000){
                if(isA){
                    if(list.get(i).getAarrive()){
                        holder.yinzhang.setImageResource(R.drawable.yinzhang_wancheng);
                    }else{
                        holder.yinzhang.setImageResource(R.drawable.yinzhang_weiyue);
                    }
                }else{
                    if(list.get(i).getBarrive()){
                        holder.yinzhang.setImageResource(R.drawable.yinzhang_wancheng);
                    }else{
                        holder.yinzhang.setImageResource(R.drawable.yinzhang_weiyue);
                    }
                }
            }else{
                holder.yinzhang.setImageResource(R.drawable.yinzhang_jinxing);
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
            holder.countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {

                }
            });
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


    //设置匹配界面的背景图片
    private void matchimageview(ImageView imageView,String items){
        if(items.equals("图书馆")){
            imageView.setBackgroundResource(R.mipmap.tushuguan1);
        }else if(items.equals("电影院")) {
            imageView.setBackgroundResource(R.mipmap.dianyingyuan2);
        }else if(items.equals("ktv")){
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

}
