package cn.bmob.imdemo.YiYou.UI;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;

/**
 * Created by DELL on 2017/6/1.
 */

public class myviewpager extends BaseActivity {
    ViewPager viewpager;
    TextView shuzi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        viewpager= (ViewPager) findViewById(R.id.viewpager);
        shuzi= (TextView) findViewById(R.id.shuzi);
        final int [] images={R.mipmap.yumaoqiu1,R.mipmap.pingpangqiu,R.mipmap.lanqiu};
        shuzi.setText(1+"/"+images.length);
        viewpager.setAdapter(new viewpageadpter(images));
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                  int p=position+1;
                  shuzi.setText(p+"/"+images.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    class viewpageadpter extends PagerAdapter
    {
        private int[] images;

        public viewpageadpter(int[] images) {
            this.images=images;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
           ImageView lv=new ImageView(myviewpager.this);
            lv.setImageResource(images[position]);
            lv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(lv);
            return lv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}