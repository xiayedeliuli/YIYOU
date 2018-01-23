package cn.sharesdk.onekeyshare;

import android.content.Context;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by DELL on 2017/9/17.
 */

public class onekeysharecallback implements PlatformActionListener {
    private  Context context;

    public onekeysharecallback(Context context) {
        this.context=context;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//        if(QZone.NAME.equals(platform.getName())){
//            Toast.makeText(context,"给你加一分",Toast.LENGTH_SHORT).show();
//        }else if(SinaWeibo.NAME.equals(platform.getName())){
//            Toast.makeText(context,"给你加一分",Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
