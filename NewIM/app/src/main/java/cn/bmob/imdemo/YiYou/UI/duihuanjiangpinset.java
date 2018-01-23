package cn.bmob.imdemo.YiYou.UI;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.Utils.ImageLoaderAsync2;
import cn.bmob.imdemo.YiYou.bean.complain;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/6/1.
 */

public class duihuanjiangpinset extends BaseActivity {
    ListView mylistview;
    TextView jilu;
    ImageView dhsetbacks;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duihuanjiangpinset);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mylistview= (ListView) findViewById(R.id.dhlistview2);
        dhsetbacks= (ImageView) findViewById(R.id.dhsetbacks);
        dhsetbacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        jilu= (TextView) findViewById(R.id.jilu);
        final User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<complain> query1=new BmobQuery<>();
        query1.addWhereEqualTo("type","兑换");
        query1.addWhereEqualTo("USERID",user.getObjectId());
        query1.order("createdAt");
        query1.findObjects(new FindListener<complain>() {
            @Override
            public void done(List<complain> list, BmobException e) {
                if(e==null){
                    mylistview.setAdapter(new adapter(getApplicationContext(),list,mylistview));
                    jilu.setText("共"+list.size()+"条记录");
                }
            }
        });
    }

    public class adapter extends BaseAdapter implements AbsListView.OnScrollListener {
        List<complain> list;
        LayoutInflater layoutInflater;
        Context context;
        private ImageLoaderAsync2 imageLoaderAsync;
        private int mstart,mend;
        //通过第一项和最后一项，获取当前可见项的所有图片的url地址
        String[] urls;
        String[] tags;
        private boolean mFirstIn;

        public adapter(Context context,List<complain> list,ListView listView) {
            this.context=context;
            this.list=list;
            this.layoutInflater=LayoutInflater.from(context);
            urls=new String[list.size()];
            tags=new String[list.size()];
            for(int i=0;i<list.size();i++){
                urls[i]=list.get(i).getExchangeUrl();
            }
            for(int i=0;i<list.size();i++){
                tags[i]=list.get(i).getObjectId();
            }
            mFirstIn=true;
            imageLoaderAsync=new ImageLoaderAsync2(mylistview,getApplicationContext());
            listView.setOnScrollListener(this);
        }


        @Override
        public int getCount() {
            return list.size();

        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        //下面两个方法禁止listview的点击事件
        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView =layoutInflater.inflate(R.layout.item_dhjilu,null);
                holder.dh_time= (TextView) convertView.findViewById(R.id.dh_time);
                holder.name_dh= (TextView) convertView.findViewById(R.id.name_dh);
                holder.url_dh= (ImageView) convertView.findViewById(R.id.url_dh);
                holder.dh_txt= (TextView) convertView.findViewById(R.id.dhtxt);
                holder.beizhu_dh= (TextView) convertView.findViewById(R.id.beizhu_dh);
                holder.integral_dh= (TextView) convertView.findViewById(R.id.integral_dh);
                convertView.setTag(holder);
            }else
            {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.name_dh.setText(list.get(i).getCompliainTEXT().substring(5));
            holder.url_dh.setTag(list.get(i).getObjectId());
            imageLoaderAsync.showImageByAsync(holder.url_dh,list.get(i).getExchangeUrl());
            holder.dh_time.setText(list.get(i).getCreatedAt().substring(0,10));
            holder.beizhu_dh.setText(list.get(i).getBeizhu_dh());
            holder.integral_dh.setText(list.get(i).getIntegral()+"积分");
            if(list.get(i).getCompleted()){
                holder.dh_txt.setText("兑换成功");
                holder.dh_txt.setTextColor(getResources().getColor(R.color.orange));
            }else{
                holder.dh_txt.setText("正在处理");
                holder.dh_txt.setTextColor(getResources().getColor(R.color.green2));
            }
            return convertView;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState==SCROLL_STATE_IDLE) {
                //开始加载任务
                imageLoaderAsync.loadimage(mstart, mend,urls,tags);
                Log.d("xzf6","滑动停止");
            } else{
                //停止加载任务
                imageLoaderAsync.cancellALLTask();
                Log.d("xzf6","开始滑动");
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

    }
    public class ViewHolder
    {
        TextView dh_time;
        TextView dh_txt;
        TextView name_dh;
        TextView beizhu_dh;
        TextView integral_dh;
        ImageView url_dh;
    }
}
