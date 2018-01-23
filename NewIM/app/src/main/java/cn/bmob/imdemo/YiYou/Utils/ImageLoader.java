package cn.bmob.imdemo.YiYou.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DELL on 2017/5/8.
 * 这个类用来下载图片
 * 使用多线程的方法
 */

public class ImageLoader {

    private ImageView imageView;
    private String url;
    private LruCache<String,Bitmap> mlrucahe;

    //在这里取得子线程发送过来的消息，进行主线程的更新
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                Bitmap bitmap= (Bitmap) msg.obj;
                imageView.setImageBitmap((Bitmap) msg.obj);
            if(null!=bitmap){
                if(bitmap.isRecycled()){
                    bitmap.recycle();
                }
            }
            System.gc();
        }
    };

    public ImageLoader(){ //这个是系统最大可用内存
        int maxmemory= (int) Runtime.getRuntime().maxMemory();
        //设定我们需要的缓存的大小
        int cachesize=maxmemory/4;
        mlrucahe=new LruCache<String, Bitmap>(cachesize){
            @Override
            //每次加入缓存时调用
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存时调用
                return value.getByteCount();
            }
        };

    }

    //将图片添加到缓存中
    public void addBitmapToCache(String url, Bitmap bitmap){
        Log.d("ww","向缓存中存图片的方法被启动了");
        //先判断缓存中是否已有
        if(getBitmapFromCache(url)==null){
            //url是key
            //bitmap是value
            mlrucahe.put(url,bitmap);
            Log.d("ww",""+url);
        }
    }

    //从缓存中提取图片
    public Bitmap getBitmapFromCache(String url){
        Log.d("ww","从缓存中取图片的方法被启动了");
        return mlrucahe.get(url);
    }



    //方法1：线程
    public void showImageByThread(final ImageView imageView, final String url){
        //把这个图片传递过来
        this.imageView=imageView;
        this.url=url;
      new Thread(){
          @Override
          public void run() {
              if(!url.startsWith("h")){
                  Message message= Message.obtain();
                  message.obj=BitmapFactory.decodeFile(url);
                  handler.sendMessage(message);;
              }else{
                  if(getBitmapFromCache(url)!=null){
                      Log.d("ww","缓存中有图片");
                      //将bitmap以Msg的方式发送出去
                      Message message= Message.obtain();
                      message.obj=getBitmapFromCache(url);
                      handler.sendMessage(message);
                  }else{
                      Log.d("ww","缓存中没有图片");
                      Message message= Message.obtain();
                      message.obj=getBitmapFromUrl(url);
                      handler.sendMessage(message);
                      addBitmapToCache(url,getBitmapFromUrl(url));
                  }
              }
          }

      }.start();
    }


    //创建一个方法，用于通过url获取bitmap图片
    public Bitmap getBitmapFromUrl(String urlString){
        InputStream is = null;
        try {
            URL url=new URL(urlString);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            is=new BufferedInputStream(connection.getInputStream());
            //下面的属性是对bitmap进行内存的管理优化
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inPurgeable=true;
            options.inInputShareable=true;
            options.inPreferredConfig= Bitmap.Config.RGB_565;
            Bitmap bitmap= BitmapFactory.decodeStream(is,null,options);
            connection.disconnect();
            return  bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }

}
