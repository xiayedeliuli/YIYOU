package cn.bmob.imdemo.YiYou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;
import cn.bmob.imdemo.YiYou.UI.changehead;
import cn.bmob.imdemo.YiYou.UI.changeuserinfo;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.v3.BmobUser;

/**
 * Created by DELL on 2017/5/21.
 */

public class fragment3 extends android.support.v4.app.Fragment {
    private CircleImageView mycircle;
    private ViewPager im;
    private ImageView write;
    TextView yunodng;
    TextView yinyue;
    TextView shuji;
    TextView dianying;
    TextView shiwu;
    TextView lvxing;
    TextView username1;
    TextView username2;
    TextView userindex;
    TextView userqianming;
    ImageView open;
    TextView vshuzi;
    RelativeLayout imss;
    ImageView infosexs;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.fragment3,container, false);
        mycircle= (CircleImageView) myview.findViewById(R.id.mycircle);
        write= (ImageView) myview.findViewById(R.id.write);
        im= (ViewPager) myview.findViewById(R.id.im);
        yunodng= (TextView) myview.findViewById(R.id.yundong);
        yinyue= (TextView) myview.findViewById(R.id.yinyue);
        shuji= (TextView) myview.findViewById(R.id.shuji);
        dianying= (TextView) myview.findViewById(R.id.dianying);
        shiwu= (TextView) myview.findViewById(R.id.shiwu);
        lvxing= (TextView) myview.findViewById(R.id.lvxing);
        username1= (TextView) myview.findViewById(R.id.username1);
        username2=(TextView)myview.findViewById(R.id.username2);
        userindex=(TextView)myview.findViewById(R.id.userindex);
        userqianming=(TextView)myview.findViewById(R.id.userqianming);
        open= (ImageView) myview.findViewById(R.id.open);
        vshuzi= (TextView) myview.findViewById(R.id.vshuzi);
        imss= (RelativeLayout) myview.findViewById(R.id.imss);
        infosexs= (ImageView) myview.findViewById(R.id.infosexs);
        return myview;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final  User user=BmobUser.getCurrentUser(User.class);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  User user=BmobUser.getCurrentUser(User.class);
                Intent intent=new Intent();
                intent.setClass(getContext(),changeuserinfo.class);
                intent.putExtra("string",user.getInterest());
                intent.putExtra("username",user.getUsername());
                intent.putExtra("qianming",user.getQianming());
                intent.putStringArrayListExtra("path", (ArrayList<String>) user.getPictures());
                startActivity(intent);
            }
        });
        imss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  User user=BmobUser.getCurrentUser(User.class);
                Intent intent=new Intent();
                intent.setClass(getContext(),changeuserinfo.class);
                intent.putExtra("string",user.getInterest());
                intent.putExtra("username",user.getUsername());
                intent.putExtra("qianming",user.getQianming());
                intent.putStringArrayListExtra("path", (ArrayList<String>) user.getPictures());
                startActivity(intent);
            }
        });
        mycircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  User user=BmobUser.getCurrentUser(User.class);
                Intent intent=new Intent();
                intent.setClass(getContext(),changehead.class);
                intent.putExtra("changehead",true);
                startActivity(intent);
            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        if(null!=user.getPictures()){
            imss.setVisibility(View.GONE);
            im.setVisibility(View.VISIBLE);
            im.setAdapter(new viewpageadpter(user.getPictures()));
            vshuzi.setText("1/"+user.getPictures().size());
        }else{
            im.setVisibility(View.GONE);
            imss.setVisibility(View.VISIBLE);
            vshuzi.setText("");
        }

        im.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                final  User user=BmobUser.getCurrentUser(User.class);
                 int p=position+1;
                if(null!=user.getPictures()){
                    vshuzi.setText(p+"/"+user.getPictures().size());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update(){
        final  User user=BmobUser.getCurrentUser(User.class);
        if(null!=user.getAvatar()){
            new ImageLoader().showImageByThread(mycircle,user.getAvatar());
        }
        String[] arr=user.getInterest();
       if(null!=arr){
           yunodng.setText(arr[0]);
           yinyue.setText(arr[1]);
           shuji.setText(arr[2]);
           dianying.setText(arr[3]);
           shiwu.setText(arr[4]);
           lvxing.setText(arr[5]);
       }
        if(null!=user.getIndex()){
            if(user.getIndex().length()>0){
                username1.setText(user.getIndex());
            }
        }
        if(null!=user.getPictures()){
            vshuzi.setText("1/"+user.getPictures().size());
        }
        if(null!=user.getSex()){
            if(user.getSex().equals("女")){
                infosexs.setBackgroundResource(R.drawable.listinfogirl);
            }else{
                infosexs.setBackgroundResource(R.drawable.listinfoboy);
            }
        }
        username2.setText(user.getUsername());
        if(user.getQianming().length()>0){
            userqianming.setText(user.getQianming());
        }
        if(null!=user.getIndex()){
            if(user.getIndex().length()>0){
                userindex.setText(user.getIndex());
            }
        }
        if(null!=user.getPictures()){
            imss.setVisibility(View.GONE);
            im.setVisibility(View.VISIBLE);
            im.setAdapter(new viewpageadpter(user.getPictures()));
        }
    }



    class viewpageadpter extends PagerAdapter
    {
        private List<String> images;

        public viewpageadpter(List<String> images) {
            this.images=images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView lv=new ImageView(getContext());
            new ImageLoader().showImageByThread(lv,images.get(position));
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
