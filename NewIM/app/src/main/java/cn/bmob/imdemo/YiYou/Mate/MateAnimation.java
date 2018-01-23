package cn.bmob.imdemo.YiYou.Mate;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.service.voice.VoiceInteractionService;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by Administrator on 2018/1/18.
 * MateFragment的动画类
 */

public class MateAnimation {
    //开启牌子和牌子上文字上下移动的起点
    private AnimatorSet UpAnimationset;
    private AnimatorSet DownAnimationset;
    private View mMateview;

    //开启牌子和文字上下摆动的启动动画
    public  void startTranslateAnimation() {
       UpAnimationset.start();
    }

    //关闭牌子和文字上下摆动的启动动画
    public void closeTranslateAnimation(){
        mMateview.findViewById(R.id.Billboard_img).clearAnimation();
        mMateview.findViewById(R.id.Nearbynumber_tex).clearAnimation();
        UpAnimationset.removeAllListeners();
        DownAnimationset.removeAllListeners();
    }

    /*初始化*/
    public MateAnimation(RelativeLayout CatBillboard,View MateView) {
        initTranslateAnimagtion(CatBillboard);
        mMateview=MateView;
    }

           /*
            * 初始化匹配页面上牌子和文字上下移动的动画
            */
    public void initTranslateAnimagtion(RelativeLayout CatBillboard) {
        View Billboard_img=CatBillboard.findViewById(R.id.Billboard_img);
        View Nearbynumber_tex=CatBillboard.findViewById(R.id.Nearbynumber_tex);
        ObjectAnimator downAnimation1 = ObjectAnimator.ofFloat(Billboard_img, "translationY", 0, -47f);
        downAnimation1.setDuration(800);
        ObjectAnimator downAnimation2 = ObjectAnimator.ofFloat(Nearbynumber_tex, "translationY", 0, -47f);
        downAnimation2.setDuration(800);
        List<Animator> downAnimationList = new ArrayList<Animator>();
        downAnimationList.add(downAnimation1);
        downAnimationList.add(downAnimation2);
        final AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(downAnimationList);

        ObjectAnimator upAnimation1 = ObjectAnimator.ofFloat(Billboard_img, "translationY", -47f, 0);
        upAnimation1.setDuration(800);
        ObjectAnimator upAnimation2 = ObjectAnimator.ofFloat(Nearbynumber_tex, "translationY", -47f, 0);
        upAnimation2.setDuration(800);
        List<Animator> upAnimationList = new ArrayList<Animator>();
        upAnimationList.add(upAnimation1);
        upAnimationList.add(upAnimation2);
        final AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(upAnimationList);

        set1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                set2.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        set2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                set1.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        UpAnimationset=set1;
        DownAnimationset=set2;
    }


    /*点击匹配的一个动画集合*/
    public  void startMateAnimantion(){
        View Billboard_img=mMateview.findViewById(R.id.Billboard_img);
        View Nearbynumber_tex=mMateview.findViewById(R.id.Nearbynumber_tex);
        View CatHead_img=mMateview.findViewById(R.id.CatHead_img);
        final View MateStatus_Llayout=mMateview.findViewById(R.id.MateStatus_Llayout);

        /*向上的动画组合*/
        final ObjectAnimator anim2=ObjectAnimator.ofFloat(MateStatus_Llayout,"translationY",0f,-142f);
        anim2.setDuration(1000);

        final ObjectAnimator anim3=ObjectAnimator.ofFloat(Nearbynumber_tex,"translationY",0f,-210f);
        anim3.setDuration(1000);

        final ObjectAnimator downanim=ObjectAnimator.ofFloat(Billboard_img,"translationY",-210f,-142f);
        downanim.setDuration(1000);
        downanim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                MateStatus_Llayout.setVisibility(View.VISIBLE);
                CountDownTimer cd=new CountDownTimer(300000,2000) {
                    @Override
                    public void onTick(long l) {
                        String[] arr ={"似乎远远的瞄到了一个靓仔","美女从你身边一闪而过","很庆幸的甩开了一位牛皮糖",
                                "别灰心，正在寻找小伙伴","努力进入下一阶段匹配"};
                        for(int i=0;i<=arr.length;i++){
                            int rand= (int) (Math.random()*5);
                            TextView MateStatus_txt= (TextView) mMateview.findViewById(R.id.MateStatus_txt);
                            if(!arr[rand].equals(MateStatus_txt.getText())){
                                MateStatus_txt.setText(arr[rand]);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onFinish() {

                    }
                };
                cd.start();
                CountdownView countdownView= (CountdownView) mMateview.findViewById(R.id.countdownView);
                countdownView.start(300000);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.2f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.25f);
        final ObjectAnimator scale2 = ObjectAnimator.ofPropertyValuesHolder(CatHead_img, pvhX, pvhY);
        scale2.setDuration(1000);
        scale2.setStartDelay(200);

        final ObjectAnimator scale1 = ObjectAnimator.ofPropertyValuesHolder(Billboard_img, pvhX, pvhY);
        scale1.setDuration(1000);
        scale1.setStartDelay(200);
        scale1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                     downanim.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        final ObjectAnimator anim5=ObjectAnimator.ofFloat(Billboard_img,"translationY",0f,-210f);
        anim5.setDuration(1000);

        ObjectAnimator anim1=ObjectAnimator.ofFloat(CatHead_img,"translationY",0f,-190f);
        anim1.setDuration(1000);
        anim1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                    ChangeLayoutColor(mMateview);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        List<Animator> list1=new ArrayList<Animator>();
        list1.add(anim1);
        list1.add(anim2);
        list1.add(anim3);
        list1.add(anim5);
        list1.add(scale1);
        list1.add(scale2);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(list1);
        animatorSet.start();
    }


    /*改变布局背景控件颜色*/
    public void ChangeLayoutColor(View view){
        view.findViewById(R.id.Nearbynumber_tex).setVisibility(View.GONE);
        ImageView imageView= (ImageView) view.findViewById(R.id.CatHead_img);
        imageView.setImageResource(R.drawable.tmao);
        view.findViewById(R.id.Billboard_img).setBackgroundResource(R.mipmap.mao5);
        TextView title= (TextView) view.findViewById(R.id.title);
        title.setTextColor(Color.WHITE);
        TextView description= (TextView) view.findViewById(R.id.description);
        description.setTextColor(Color.WHITE);
        TextView YIYOU= (TextView) view.findViewById(R.id.YIYOU_txt);
        YIYOU.setTextColor(Color.WHITE);
        view.findViewById(R.id.Toplay).setBackgroundColor(Color.BLACK);
        view.findViewById(R.id.toplay).setBackgroundResource(R.drawable.fragment1bian);
        view.setBackgroundColor(Color.BLACK);
        ImageView opendrawlayout_img= (ImageView) view.findViewById(R.id.opendrawlayout_img);
        opendrawlayout_img.setImageResource(R.drawable.threebai);
        ImageView RightHand_img= (ImageView) view.findViewById(R.id.RightHand_img);
        RightHand_img.setImageResource(R.drawable.dianjibai);

    }
}
