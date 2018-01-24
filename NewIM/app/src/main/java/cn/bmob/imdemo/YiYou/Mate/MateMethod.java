package cn.bmob.imdemo.YiYou.Mate;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * 这个类用来进行匹配的相关操作
 * Created by Administrator on 2018/1/19.
 */

public class MateMethod {
    //这个是持续匹配的时间函数(最优模式)
    private static CountDownTimer mostcdTimer;
    private static  CountDownTimer bettersecondcdTimer;
    private static  CountDownTimer betterthirdcdTimer;
    private static String TAG="MateMethod";
    //这个属性表示被我占用了同步锁的用户
    private static String UseOtheUsersSynchronizedID;
    private static  String[] LocalMateUserActivity;

    //开启匹配的方法
    public static  void StartMate(final MateFragment mateFragment){
        Mate mate=new Mate();
        mate.setMateItem(mateFragment.getMateContains().getMateItem());
        mate.setMateSex(mateFragment.getMateContains().getMatesex());
        mate.setWhetherMatching(true);
        mate.setMateUserFlag(false);
        mate.setSynchronizedID("");
        mate.setMateUserActivity(new String[]{"","",""});
        mate.update(sp.getString("Mate", ""), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                     Mate(mateFragment);
                }else{
                    Log.d(TAG, "匹配失败" + e.getErrorCode()+"--"+e.getMessage());
                    /*如果匹配开启失败--数据上传失败*/
                    Toast.makeText(mateFragment.getContext(),"系统错误，请稍候重试",Toast.LENGTH_LONG).show();
                    //初始化数据
                     ResetMate();
                    //重置fragment页面
                    mateFragment.SendCancelBrocast();
                }
            }
        });

    };

    public static void ResetMate(){
        CancelTimerTask();
        Mate mate=new Mate();
        mate.setMateItem("");
        mate.setMateSex("");
        mate.setMateTIme(0L);
        mate.setWhetherMatching(false);
        mate.setMateUserFlag(false);
        mate.setSynchronizedID("");
        mate.setMateUserActivity(new String[]{"","",""});
        mate.update(sp.getString("Mate", ""),new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

    public static void StopMate(){
        CancelTimerTask();
        Mate mate=new Mate();
        mate.setWhetherMatching(false);
        mate.setMateUserFlag(false);
        mate.update(sp.getString("Mate", ""),new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

    public static void Mate(final MateFragment mateFragment){
        mostcdTimer = new CountDownTimer(120000, 2000) {
            @Override
            public void onTick(long l) {
                Log.d(TAG, "第一阶段正在匹配,尚无结果" + l);
                CountTimeStart("项目性别", mateFragment);
            }
            @Override
            public void onFinish() {
                bettersecondcdTimer.start();
            }
        };

        //第二分钟，次级匹配
        bettersecondcdTimer = new CountDownTimer(120000, 2000) {
            @Override
            public void onTick(long l) {
                Log.d(TAG, "第二阶段正在匹配,尚无结果" + l);
                if (sp.getString("SecondChoice", "性别").equals("项目")) {
                    CountTimeStart("仅项目",mateFragment);
                } else {
                    CountTimeStart("仅性别", mateFragment);
                }
            }

            @Override
            public void onFinish() {
                betterthirdcdTimer.start();
            }
        };

        //第三分钟，默认匹配
        betterthirdcdTimer = new CountDownTimer(120000, 2000) {
            @Override
            public void onTick(long l) {
                Log.d(TAG, "第三阶段正在匹配,尚无结果" + l);
                CountTimeStart("无要求匹配", mateFragment);
            }

            @Override
            public void onFinish() {
                //取消匹配任务
                betterthirdcdTimer.cancel();
            }
        };
        mostcdTimer.start();
    }

    private static  void CancelTimerTask(){
        if(null!=mostcdTimer&&null!=bettersecondcdTimer&&null!=betterthirdcdTimer){
            mostcdTimer.cancel();
            bettersecondcdTimer.cancel();
            betterthirdcdTimer.cancel();
        }
    }

    private static void CountTimeStart(final String panduan,final MateFragment mateFragment) {
        BmobQuery<Mate> myquery=new BmobQuery<>();
        myquery.getObject(sp.getString("Mate", ""), new QueryListener<Mate>() {
            @Override
            public void done(final Mate smate, BmobException e) {
                if(e==null){
                    //如果自己的同步锁尚未被占用,则努力查询合适对象，争取占用同步锁
                    if(smate.getSynchronizedID().equals("")){
                        BmobQuery<Mate> query = new BmobQuery<>();
                        query.addWhereNotEqualTo("objectId", sp.getString("Mate", ""));
                        query.addWhereEqualTo("WhetherMatching", true);
                        if (panduan.contains("项目")) {
                            query.addWhereEqualTo("MateItem", mateFragment.getMateContains().getMateItem());
                        }
                        if (panduan.contains("性别")) {
                            query.addWhereEqualTo("sex", mateFragment.getMateContains().getMatesex());
                        }
                        query.findObjects(new FindListener<Mate>() {
                            @Override
                            public void done(List<Mate> list, BmobException e) {
                                if (e == null) {
                                    for(final Mate bean:list){
                                        if(bean.getMateItem().equals(smate.getMateItem())&&
                                                bean.getMateSex().equals(smate.getSex())){
                                            //开始占用双方同步锁
                                            Mate mymate=new Mate();
                                            mymate.setSynchronizedID(sp.getString("Mate", ""));
                                            mymate.update(sp.getString("Mate", ""), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        Mate othermate=new Mate();
                                                        othermate.setSynchronizedID(sp.getString("Mate", ""));
                                                        othermate.update(bean.getObjectId(), new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                                if(e==null){
                                                                    //被自己占用同步锁的那个用户Mate表的ID，注意不是—User表的ID
                                                                    UseOtheUsersSynchronizedID=bean.getObjectId();
                                                                    LocalMateUserActivity=bean.getCertainMateUser();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                            break;
                                        }
                                    }
                                }
                            }
                        });
                    }
                    //如果同步锁被自己占用了,则负责自己和对方的跳转业务
                    else if(smate.getSynchronizedID().equals(sp.getString("Mate", ""))){
                        //验证一下自己占用的对方同步锁是否安全
                        BmobQuery<Mate> query=new BmobQuery<Mate>();
                        query.getObject(UseOtheUsersSynchronizedID, new QueryListener<Mate>() {
                            @Override
                            public void done(final Mate mate, BmobException e) {
                                if(e==null){
                                    //如果没被改变，负责跳转逻辑
                                    if(mate.getSynchronizedID().equals(sp.getString("Mate", ""))){
                                        Mate mate1=new Mate();
                                        mate1.setMateUserFlag(true);
                                        mate1.setMateUserActivity(LocalMateUserActivity);
                                        mate1.update(sp.getString("Mate", ""), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                    Mate mate1=new Mate();
                                                    mate1.setMateUserFlag(true);
                                                    mate1.setMateUserActivity(smate.getCertainMateUser());
                                                    mate1.update(UseOtheUsersSynchronizedID, new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if(e==null){
                                                               CancelTimerTask();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                    //如果被改变了，清空自己占用的自己的同步锁，让匹配重新开始
                                    else{
                                        Mate mate1=new Mate();
                                        mate1.setMateUserFlag(false);
                                        mate1.setSynchronizedID("");
                                        mate1.update(sp.getString("Mate", ""), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {

                                            }
                                        });
                                    }
                                }
                            }
                        });

                    }
                    //如果同步锁被别人占用了，则放弃所有匹配任务,由别人来操作
                    else{
                        CancelTimerTask();
                    }
                }else{
                    Log.i(TAG, e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

}

