package cn.bmob.imdemo.YiYou;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.imdemo.YiYou.mylogin.MyApplication;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * Created by DELL on 2017/10/6.
 */

public class Myservice extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fangfa();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void fangfa(){
//        Toast.makeText(getApplicationContext(),"myservice启动了",Toast.LENGTH_SHORT).show();
        final CountDownTimer cd=new CountDownTimer(600000,60000) {
            @Override
            public void onTick(final long millisUntilFinished) {
                final MyUser myUser=new MyUser();
                myUser.setMillis(System.currentTimeMillis());
                myUser.setAreJianting(false);
                myUser.update(sp.getString("userMateID", ""), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e!=null){
                            Log.d("xzf",e.getErrorCode()+e.getMessage());
                            final MyUser myUser=new MyUser();
                            myUser.setMillis(System.currentTimeMillis());
                            myUser.setAreJianting(false);
                            myUser.update(sp.getString("userMateID", ""), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        MyApplication.aremills=true;
                                    }
                                }
                            });
                        }else{
                            Log.d("xzf","更新成功"+myUser.getMillis());
                            MyApplication.aremills=true;
                        }
                    }
                });
            }

            @Override
            public void onFinish() {
                fangfa();
            }
        };
        cd.start();
    }

}
