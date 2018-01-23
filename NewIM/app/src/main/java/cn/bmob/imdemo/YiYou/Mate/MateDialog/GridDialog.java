package cn.bmob.imdemo.YiYou.Mate.MateDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.imdemo.R;

/**
 * Created by Administrator on 2018/1/23.
 */

public class GridDialog extends Dialog {
    private View rootview;
    List<Map<String,Object>> list=new ArrayList<>();
    private GridView gridView;
    private OnGridViewClick monGridViewClick;

    public GridDialog(@NonNull Context context) {
        super(context);
        init();
        initdata();
        initGridview();
    }

    private void init(){
        LayoutInflater inflater=LayoutInflater.from(getContext());
        rootview=inflater.inflate(R.layout.griddialog,null);
        Window window=this.getWindow();
        window.setContentView(rootview);

    }

    private void initdata(){
         Map<String,Object> map0=new HashMap<>();
         map0.put("img",R.drawable.icon1);
         map0.put("text","图书馆");
         list.add(map0);
        Map<String,Object> map1=new HashMap<>();
        map1.put("img",R.drawable.icon2);
        map1.put("text","下午茶");
        list.add(map1);
        Map<String,Object> map2=new HashMap<>();
        map2.put("img",R.drawable.icon3);
        map2.put("text","电影院");
        list.add(map2);
        Map<String,Object> map3=new HashMap<>();
        map3.put("img",R.drawable.icon4);
        map3.put("text","KTV");
        list.add(map3);
        Map<String,Object> map4=new HashMap<>();
        map4.put("img",R.drawable.icon5);
        map4.put("text","篮球");
        list.add(map4);
        Map<String,Object> map5=new HashMap<>();
        map5.put("img",R.drawable.icon6);
        map5.put("text","乒乓球");
        list.add(map5);
        Map<String,Object> map6=new HashMap<>();
        map6.put("img",R.drawable.icon7);
        map6.put("text","约牌");
        list.add(map6);
        Map<String,Object> map7=new HashMap<>();
        map7.put("img",R.drawable.icon8);
        map7.put("text","散步");
        list.add(map7);
        Map<String,Object> map8=new HashMap<>();
        map8.put("img",R.drawable.icon9);
        map8.put("text","羽毛球");
        list.add(map8);
    }

    public void initGridview(){
        gridView= (GridView) rootview.findViewById(R.id.MateGirdView);
        mGridDialogAdapter adapter=new mGridDialogAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   String MateItem= (String) list.get(i).get("text");
                   Integer MateIcon= (Integer) list.get(i).get("img");
                   monGridViewClick.Click(MateItem,MateIcon);
            }
        });
    }

    class mGridDialogAdapter extends BaseAdapter{

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
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            viewholder holder=null;
            if(convertView==null){
                //初始化一个布局反射器
                LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.griditem,null);
                holder=new viewholder();

                //将视图对象的两个控件实例化出来，绑定到holder对象上，再将holder对象绑定到视图上
                holder.logo= (ImageView) convertView.findViewById(R.id.griditem_img);
                holder.title= (TextView) convertView.findViewById(R.id.griditem_txt);
                convertView.setTag(holder);

            }else{
                holder= (viewholder) convertView.getTag();
            }
            holder.logo.setImageResource((Integer) list.get(position).get("img"));
            holder.title.setText((String) list.get(position).get("text"));

            return convertView;
        }
        public  class viewholder{
            ImageView logo;
            TextView  title;
        }
    }

    public interface OnGridViewClick{
        void Click(String item,Integer icon);
    }
    public void setOnGridViewClick(OnGridViewClick onGridViewClick){
        this.monGridViewClick=onGridViewClick;
    }
}
