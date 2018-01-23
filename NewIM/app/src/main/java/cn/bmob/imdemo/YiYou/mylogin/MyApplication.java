package cn.bmob.imdemo.YiYou.mylogin;

import android.app.Application;
import android.support.v4.app.Fragment;

/**
 * Created by DELL on 2017/4/17.
 */

public class MyApplication extends Application {

    //记录关于自己设定的匹配属性，省得查询
     public static String item;
    public static Long myappdate;
    public static String choicesex;

    //这个flag用来判断匹配弹出的对话和正常对话里的对话
    public static boolean isMateChatActivity;

    //用来区分正常匹配和即兴匹配
    public static boolean isJXpipei;

    //记录自己的即兴匹配对象，防止一直向同一个人发送匹配请求
    public static String JXid;

    //控制黑白界面的切换
    public static boolean isblack;

    //这个数据保存匹配成功的人的Objectid；
    public static String OtherObjectId;

    //现在显示的fragment
    public static Fragment currentfragment;

    //设置一个flag，让主界面不会无缘无故刷新fragment
    public static boolean shuaxin;

    //这个变量是记录自己有没有确认
    public static boolean ensure;

    //用来控制开启匹配任务，在提交数据成功后为真，开启任务后为假，初始化为假
    public  static  boolean  StartMatch;

    //用来控制匹配成功后的跳转，在匹配成功后为假，开启任务后为真，初始化为真
    public static  boolean  MatchSuccessed;

    //这个用来控制在匹配完成后，用来使fragment1的显示模式为：正在匹配
    public static  boolean isshowtx2;

    //是否冻结
    public static boolean dongjie;
    //是否封号
    public static boolean studentcomfirm;

    //用来控制通知栏的客服消息是开启URL还是一个对话
    public static Boolean iskefu;
    public static String url;

    //
    public static String shareurl;

    //是否已有chatactivity在线
    public static Boolean ischatnow;

    public static Boolean huihua;

    public static Boolean aremills;


}
