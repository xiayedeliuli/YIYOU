package cn.bmob.imdemo.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.YiYou.Mate.Mate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author smile
 * @project AnimationUtil
 * @date 2016-03-01-14:55
 */
public class Util {
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    //这个方法随便丢给他几个不同类型的参数都可以插入进list集合
    public static List<Object> inserList(Object...args){
        List<Object> list=new ArrayList<>();
        for(Object arg:args){
            list.add(arg);
        }
        return list;
    }

    //写一个可以更新所有bmob表的方法，第一步是判断是哪个表，根据key来判断
    public static  boolean UpdateBmobform(String bmobformname, List<Object> list,String ID){;
        switch (bmobformname){
            case "Mate":
                Mate mate=new Mate();
                if(null!=list.get(0)&&!list.get(0).equals("")){
                    mate.setCertainMateUser((String[]) list.get(0));
                }
                else if(null!=list.get(1)&&!list.get(1).equals("")){
                    mate.setWhetherMatching((Boolean) list.get(1));
                }else if(null!=list.get(2)&&!list.get(2).equals("")){
                    mate.setSex((String) list.get(2));
                }else if(null!=list.get(3)&&!list.get(3).equals("")){
                    mate.setMateItem((String) list.get(3));
                }else if(null!=list.get(4)&&!list.get(4).equals("")){
                    mate.setMateSex((String) list.get(4));
                }else if(null!=list.get(5)&&!list.get(5).equals("")){
                    mate.setBelongToUerID((String) list.get(5));
                }else if(null!=list.get(6)&&!list.get(6).equals("")){
                    mate.setSynchronizedID((String) list.get(6));
                }else if(null!=list.get(7)&&!list.get(7).equals("")){
                    mate.setMateUserActivity((String[]) list.get(7));
                }else if(null!=list.get(8)&&!list.get(8).equals("")){
                    mate.setMateUserFlag((Boolean) list.get(8));
                }
                mate.update(ID, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e!=null){
                            Log.d("xzf",e.getErrorCode()+"  "+e.getMessage());
                        }
                    }
                });
            case "User":
                break;
        }
        return  false;
    }



}
