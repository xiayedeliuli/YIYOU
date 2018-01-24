package cn.bmob.imdemo.YiYou.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;

import com.baidu.mapapi.model.LatLng;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.UI.changehead;
import cn.bmob.imdemo.YiYou.UI.comfirmstate;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.imdemo.YiYou.mylogin.MyApplication;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeDemo;

/**
 * Created by DELL on 2017/6/1.
 */

public class MyUtils {
    public  static void showShare(Context context) {
        User user = BmobUser.getCurrentUser(User.class);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle("我的邀请码:"+user.getObjectId());
        oks.setTitleUrl("http://fir.im/9e85");
        oks.setText("定个时间，定个地点，就能约出男神女神！不信你来试");
        oks.setImageUrl(MyApplication.shareurl);
        oks.setSite("亦友社交");
        oks.setSiteUrl("http://fir.im/9e85");
        oks.setUrl("http://fir.im/9e85");
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
        oks.show(context);
    }


    //压缩图片的方法
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.WEBP, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.WEBP, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }


    //获得SD卡路径
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        }
        return null;
    }

    //根据路径获取图片并压缩，按比例大小压缩。返回bitmap对象
    public static Bitmap getimage(String srcPath,boolean isnext) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1280f;//这里设置高度为800f
        float ww = 720f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        if(isnext){
            return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        }else{
            return  bitmap;
        }

    }


    //网络url获得图片
    public static Bitmap getBitmap(String path) throws IOException{

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    //dongtaifragment用到的图片保存方法，改变图片名
    public static  File dongtaisaveBitmapFile(Bitmap bitmap, int i,int sum){
        Log.d("path",""+getSDPath()+"/"+i+".webp");
        File file=new File(getSDPath()+"/ListInfo-"+sum+"-"+i+".webp");//将要保存图片的路径
        try {
            Log.d("path","这个方法执行了没");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static String getDateTimeFromMillisecond(Long millisecond){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        return simpleDateFormat.format(date);
    }


    //恢复重置
    public static  void rematch(String ID){
        MyUser myUser=new MyUser();
        myUser.setAreJianting(true);
        myUser.setWasMated("尚未匹配");
        myUser.update(ID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
            }
        });
    }

    //出现错误，恢复匹配的方法
    public static void huifu(String ID){
        MyUser myU=new MyUser();
        myU.setOtherObjectId("");
        myU.setAreJianting(true);
        myU.setWasMated("正在匹配");
        myU.update(ID, new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }



    //设置匹配信息界面的背景图片
    public static void matchlay(LinearLayout layout, String items){
        if(items.equals("图书馆")){
            layout.setBackgroundResource(R.mipmap.tushuguan);
        }else if(items.equals("电影院")) {
            layout.setBackgroundResource(R.mipmap.dianyingyuan);
        }else if(items.equals("KTV")){
            layout.setBackgroundResource(R.mipmap.ktv);
        }else if(items.equals("约牌")){
            layout.setBackgroundResource(R.mipmap.yuepai);
        }else if(items.equals("散步")){
            layout.setBackgroundResource(R.mipmap.sanbu);
        }else if(items.equals("羽毛球")){
            layout.setBackgroundResource(R.mipmap.yumaoqiu);
        }else if(items.equals("乒乓球")){
            layout.setBackgroundResource(R.mipmap.pingpangqiu);
        }else if(items.equals("篮球")){
            layout.setBackgroundResource(R.mipmap.lanqiu);
        }else if(items.equals("下午茶")){
            layout.setBackgroundResource(R.mipmap.xiawucha);
        }

    }

    public static List<LatLng> getlatlang(String index){

        if(index.equals("南财图书馆")){
            List<LatLng> pts=new ArrayList<>();
            LatLng pt1 = new LatLng(32.1143060000,118.9306080000);
            LatLng pt2 = new LatLng(32.1140490000,118.9305540000);
            LatLng pt3 = new LatLng(32.1142520000,118.9312190000);
            LatLng pt4 = new LatLng(32.1144160000,118.9309770000);
            pts.add(pt1);
            pts.add(pt2);
            pts.add(pt3);
            pts.add(pt4);
            return pts;
        }else if(index.equals("南财校门")){
            List<LatLng> pts=new ArrayList<>();
            LatLng pt1 = new LatLng(32.1106780000,118.9327420000);
            LatLng pt2 = new LatLng(32.1105060000,118.9328450000);
            LatLng pt3 = new LatLng(32.1102760000,118.9321040000);
            LatLng pt4 = new LatLng(32.1105170000,118.9320100000);
            pts.add(pt1);
            pts.add(pt2);
            pts.add(pt3);
            pts.add(pt4);
            return pts;
        }else if(index.equals("南财操场")){
            List<LatLng> pts=new ArrayList<>();
            LatLng pt1 = new LatLng(32.1111290000,118.9283220000);
            LatLng pt2 = new LatLng(32.1107040000,118.9283580000);
            LatLng pt3 = new LatLng(32.1106930000,118.9284250000);
            LatLng pt4 = new LatLng(32.1111550000,118.9284300000);
            pts.add(pt1);
            pts.add(pt2);
            pts.add(pt3);
            pts.add(pt4);
            return pts;
        }else if(index.equals("南邮校门")){
            List<LatLng> pts=new ArrayList<>();
            LatLng pt1 = new LatLng(32.1126690000,118.9374830000);
            LatLng pt2 = new LatLng(32.1123670000,118.9374780000);
            LatLng pt3 = new LatLng(32.1123630000,118.9380040000);
            LatLng pt4 = new LatLng(32.1126500000,118.9380080000);
            pts.add(pt1);
            pts.add(pt2);
            pts.add(pt3);
            pts.add(pt4);
            return pts;
        }else if(index.equals("南邮操场")){
            List<LatLng> pts=new ArrayList<>();
            LatLng pt1 = new LatLng(32.1219670000,118.9409310000);
            LatLng pt2 = new LatLng(32.1220440000,118.9408770000);
            LatLng pt3 = new LatLng(32.1218340000,118.9406260000);
            LatLng pt4 = new LatLng(32.1219020000,118.9405760000);
            pts.add(pt1);
            pts.add(pt2);
            pts.add(pt3);
            pts.add(pt4);
            return pts;
        }else if(index.equals("南邮图书馆")){
            List<LatLng> pts=new ArrayList<>();
            LatLng pt1 = new LatLng(32.1182850000,118.9379190000);
            LatLng pt2 = new LatLng(32.1182920000,118.9376220000);
            LatLng pt3 = new LatLng(32.1179480000,118.9376220000);
            LatLng pt4 = new LatLng(32.1179210000,118.9379280000);

            pts.add(pt1);
            pts.add(pt2);
            pts.add(pt3);
            pts.add(pt4);
            return pts;
        }else if(index.equals("南师图书馆")){
            List<LatLng> pts=new ArrayList<>();
            LatLng pt1 = new LatLng(32.1089230000,118.9176130000);
            LatLng pt2 = new LatLng(32.1087900000,118.9176720000);
            LatLng pt3 = new LatLng(32.1089620000,118.9182830000);
            LatLng pt4 = new LatLng(32.1091220000,118.9182200000);

            pts.add(pt1);
            pts.add(pt2);
            pts.add(pt3);
            pts.add(pt4);
            return pts;
        }else if(index.equals("南师操场")){
            List<LatLng> pts=new ArrayList<>();
            LatLng pt1 = new LatLng(32.1096190000,118.9205420000);
            LatLng pt2 = new LatLng(32.1093100000,118.9206860000);
            LatLng pt3 = new LatLng(32.1093400000,118.9207890000);
            LatLng pt4 = new LatLng(32.1096310000,118.9206270000);

            pts.add(pt1);
            pts.add(pt2);
            pts.add(pt3);
            pts.add(pt4);
            return pts;
        }
        else if(index.equals("南师校门")){
            List<LatLng> pts=new ArrayList<>();
            LatLng pt1 = new LatLng(32.1064610000,118.9188310000);
            LatLng pt2 = new LatLng(32.1062240000,118.9189160000);
            LatLng pt3 = new LatLng(32.1063240000,118.9192840000);
            LatLng pt4 = new LatLng(32.1065950000,118.9191810000);

            pts.add(pt1);
            pts.add(pt2);
            pts.add(pt3);
            pts.add(pt4);
            return pts;
        }
        return null;
    }

    public static String time(String date){
        String now=MyUtils.getStringDate(System.currentTimeMillis());
        //月份相同
        if(date.substring(5,7).equals(now.substring(0,2))){
            int day1=Integer.parseInt(now.substring(3,5));
            int day2=Integer.parseInt(date.substring(8,10));
            if(Math.abs(day1-day2)>2){
                return date.substring(5);
            }else{
                if(day1==day2){
                    //今天
                    Log.d("xzg","今天"+date.substring(11,16));
                    return "今天"+date.substring(11,16);
                }else if((day1-day2)==1){
                    Log.d("xzg","昨天"+date.substring(11,16));
                    return "昨天"+date.substring(11,16);
                }else if((day1-day2)==2){
                    Log.d("xzg","前天"+date.substring(11,16));
                    return "前天"+date.substring(11,16);
                }
            }
        }else{
            Log.d("xzg",date.substring(5));
            return date.substring(5);
        }
        return  null;
    }

    private void tocomfirm(final Context context, final Integer i){
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("你已向小伙伴发送过匹配需求，是否重新开始随机匹配");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
                Intent intent=new Intent();
                if(i==1){
                    intent.setClass(context,comfirmstate.class);
                }else if(i==2){
                    intent.setClass(context,changehead.class);
                }

            }
        });
    }


    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";

    //保存bitmap图像
    public static String saveBitmap(Context context, Bitmap mBitmap,String url) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + url + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

    public static  String savepath(Context context){
        String savePath;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        return  savePath;
    }



    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(Long date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日HH:mm");
        return formatter.format(date);
    }




}
