package cn.bmob.imdemo.YiYou.myadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.R;

/**
 * Created by DELL on 2017/8/15.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> images=new ArrayList<>();

    public GridViewAdapter(Context context,List<String> images) {
           this.images=images;
           this.context=context;
    }

    @Override
    public int getCount() {
        if (null != images) {
            return images.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 获得容器
            convertView = LayoutInflater.from(this.context).inflate(R.layout.gridviewadapter, parent,false);

            // 初始化组件
            viewHolder.gridimage = (ImageView) convertView.findViewById(R.id.gridimage);
            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 给组件设置资源
        Log.d("xzf",""+images.get(position));
        Bitmap bitmap=BitmapFactory.decodeFile(images.get(position));
        viewHolder.gridimage.setImageBitmap(bitmap);
        viewHolder.gridimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return convertView;
    }

class ViewHolder {
    private ImageView gridimage;
}
}

