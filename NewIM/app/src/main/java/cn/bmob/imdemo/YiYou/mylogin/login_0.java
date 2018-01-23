package cn.bmob.imdemo.YiYou.mylogin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;

/**
 * Created by DELL on 2017/4/16.
 */

public class login_0 extends BaseActivity{
    ViewPager login_0;
    RelativeLayout y1;
    RelativeLayout y2;
    RelativeLayout y3;
    RelativeLayout y4;
    LinearLayout r1;
    RelativeLayout r2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beoff);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        y1= (RelativeLayout) findViewById(R.id.y1);
        y2= (RelativeLayout) findViewById(R.id.y2);
        y3= (RelativeLayout) findViewById(R.id.y3);
        y4= (RelativeLayout) findViewById(R.id.y4);
        r2= (RelativeLayout) findViewById(R.id.r2);
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(cn.bmob.imdemo.YiYou.mylogin.login_0.this, login_1.class);
                startActivity(intent);
                finish();
            }
        });
        r1= (LinearLayout) findViewById(R.id.r1);
        login_0= (ViewPager) findViewById(R.id.login_0);
        login_0.setAdapter(new viewpageadpter());
        login_0.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                  if(position==0){
                      r1.setVisibility(View.VISIBLE);
                      r2.setVisibility(View.GONE);
                      y1.setBackgroundResource(R.drawable.yindao2);
                      y2.setBackgroundResource(R.drawable.yindao);
                      y3.setBackgroundResource(R.drawable.yindao);
                      y4.setBackgroundResource(R.drawable.yindao);
                  }else if(position==1){
                      r1.setVisibility(View.VISIBLE);
                      r2.setVisibility(View.GONE);
                      y2.setBackgroundResource(R.drawable.yindao2);
                      y1.setBackgroundResource(R.drawable.yindao);
                      y3.setBackgroundResource(R.drawable.yindao);
                      y4.setBackgroundResource(R.drawable.yindao);
                  }else if(position==2){
                      r1.setVisibility(View.VISIBLE);
                      r2.setVisibility(View.GONE);
                      y3.setBackgroundResource(R.drawable.yindao2);
                      y2.setBackgroundResource(R.drawable.yindao);
                      y1.setBackgroundResource(R.drawable.yindao);
                      y4.setBackgroundResource(R.drawable.yindao);
                  }else{
                      r1.setVisibility(View.GONE);
                      r2.setVisibility(View.VISIBLE);
                  }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    class viewpageadpter extends PagerAdapter
    {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView lv=new ImageView(getApplicationContext());
            lv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if(position==0){
                lv.setImageResource(R.mipmap.yindao1);
            }else if(position==1){
                lv.setImageResource(R.mipmap.yindao4);
            }else if(position==2){
                lv.setImageResource(R.mipmap.yindao2);
            }else{
                lv.setImageResource(R.mipmap.yindao3);
            }
            container.addView(lv);
            return lv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }



}