package cn.bmob.imdemo.YiYou.myadapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;

/**
 * Created by DELL on 2017/6/1.
 */

public class MyDrawlayoutAdapter extends BaseAdapter {
    private List<Map<String,Object>> list;
    private LayoutInflater inflater;
    private ImageView backg;
    private  Context mycontext;
    private  String name1;
    private  String name2;
    private TextView textname1;
    private TextView textname2;
    private CircleImageView circleImageView;
    private String avatr;
    String path;

    public MyDrawlayoutAdapter(Context context,String name1,String name2,String avatr,String path) {
        this.avatr=avatr;
        this.name1=name1;
        this.name2=name2;
        mycontext=context;
        this.inflater=LayoutInflater.from(mycontext);
        this.path=path;
    }

    public void setList(List<Map<String,Object>> list){
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;

        if(position==0){
            final View vieww=inflater.inflate(R.layout.item2,null);
            backg= (ImageView) vieww.findViewById(R.id.backg);
            if(null!=path&path.length()>0){
                Bitmap bitmap=BitmapFactory.decodeFile(path);
                if(null!=bitmap){
                    backg.setImageBitmap(bitmap);
                    if(bitmap.isRecycled()){
                        bitmap.recycle();
                    }
                }
            }
            return vieww;
        }



        else {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.item,null);
                holder=new ViewHolder();

                holder.logo= (ImageView) convertView.findViewById(R.id.logo);
                holder.title= (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }

            Map map=list.get(position);
            if(null!=holder.logo){
                holder.logo.setImageResource((Integer) map.get("logo"));
            }
            holder.title.setText((String) map.get("title"));
            return convertView;
        }

    }

    public class ViewHolder {
        ImageView logo;
        TextView title;
    }



  }








