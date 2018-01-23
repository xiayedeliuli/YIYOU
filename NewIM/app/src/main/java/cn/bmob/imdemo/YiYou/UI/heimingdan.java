package cn.bmob.imdemo.YiYou.UI;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.YiYou.bean.ListInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by DELL on 2017/6/1.
 */

public class heimingdan extends BaseActivity {
    private ListView listView;
    ImageView heiback;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heimingdan);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        listView= (ListView) findViewById(R.id.heimingdanlist);
        heiback= (ImageView) findViewById(R.id.heiback);
        heiback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        BmobQuery<ListInfo> query=new BmobQuery<>();
        query.addWhereEqualTo("isBlack",true);
        query.findObjects(new FindListener<ListInfo>() {
            @Override
            public void done(List<ListInfo> list, BmobException e) {
                if(e==null){
                    listView.setAdapter(new adapter(heimingdan.this,list));
                }

            }
        });



    }






    public class adapter extends BaseAdapter {
        List<ListInfo> list;
        LayoutInflater layoutInflater;
        Context context;

        public adapter(Context context,List<ListInfo> list) {
            this.context=context;
            this.list=list;
            this.layoutInflater=LayoutInflater.from(context);
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


        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            ViewHolder holder=null;
            if(convertView==null){
                convertView =layoutInflater.inflate(R.layout.heimingdanitem,null);
                holder=new ViewHolder();
                holder.text= (TextView) convertView.findViewById(R.id.heimingdantext);
                convertView.setTag(holder);
            }else
            {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.text.setText(list.get(i).getInfo());
            return convertView;
        }
    }
    public class ViewHolder
    {
        TextView text;
    }
}
