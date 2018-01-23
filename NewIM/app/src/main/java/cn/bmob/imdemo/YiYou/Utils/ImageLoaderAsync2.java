package cn.bmob.imdemo.YiYou.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cn.bmob.imdemo.R;

/**
 * Created by DELL on 2017/5/8.
 * 这个类用来下载图片
 * 使用异步任务的方法
 */

public class ImageLoaderAsync2 {
    //这个类用来缓存图片
    private LruCache<String,Bitmap> mpictureCache;
    private ListView listView;
    private Set<NewsAsyncTask> mTask;
    private Context context;
    private  HashMap<String,Bitmap> map=new HashMap<>();



    //在初始化方法中初始化图片缓存类
    public ImageLoaderAsync2(ListView listView,Context context){
        this.listView=listView;
        mTask=new HashSet<>();
        //这个是系统最大可用内存
          int maxmemory= (int) Runtime.getRuntime().maxMemory();
        //设定我们需要的缓存的大小
          int cachesize=maxmemory/4;
        mpictureCache=new LruCache<String, Bitmap>(cachesize){
            @Override
            //每次加入缓存时调用
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存时调用
                return value.getByteCount();
            }
        };
        this.context=context;

    }

    //将图片添加到缓存中
    public void addBitmapToCache(String url, Bitmap bitmap){
        Log.d("gg","向缓存中存图片的方法被启动了");
        //先判断缓存中是否已有
             if(getBitmapFromCache(url)==null){
                 //url是key
                 //bitmap是value
                 mpictureCache.put(url,bitmap);
             }
    }




    //从缓存中提取图片
    public Bitmap getBitmapFromCache(String url){
        Log.d("gg","从缓存中取图片的方法被启动了");
        return mpictureCache.get(url);
    }




    //适配器中的图片加载主要通过这个方法
    public void showImageByAsync(ImageView imageView, final String url){
        //先从缓存中取数据，如果没有再从网络加载
        if(url!=null&&!url.equals("")){
            if(null!=map.get(url)){
                imageView.setImageBitmap(map.get(url));
            }else{
                int size=url.length()-4;
                String one= (String) url.substring(0,size);
                String path=MyUtils.savepath(context)+one+".jpg";
                Bitmap bitmap=BitmapFactory.decodeFile(path);
                if(bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                    map.put(url,bitmap);
                    Log.d("xzf","从文件中拿图片");
                }else{
                    imageView.setImageResource(R.color.login);
                }
            }
        }
    }

    //创建一个方法，用于通过url获取bitmap图片
    public Bitmap getBitmapFromUrl(String urlString){
        Bitmap bitmap;
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
            bitmap= BitmapFactory.decodeStream(is,null,options);
            int size=urlString.length()-4;
            String one= (String) urlString.substring(0,size);
            String path=MyUtils.savepath(context)+one+".jpg";
            File file=new File(path);
            if(!file.exists()){
                String paht=MyUtils.saveBitmap(context,bitmap, one);
                map.put(urlString,bitmap);
                File files=new File(paht);
                Log.d("xzf","保存图片："+files.length()/1024);
            }
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

    //在这个方法中重新加载所有可见项的图片
    public void loadimage(int start,int end,String[] urls,String[] tagj){
        for(int i=start;i<end;i++){
            String url=urls[i];
            String tag=tagj[i];
            loadimagemethod(url,tag);
        }
    }

    //加载图片的执行任务
    private void loadimagemethod(String url,String tag){
        ImageView imageView= (ImageView) listView.findViewWithTag(tag);
        //先从缓存中取数据，如果没有再从网络加载
        if(url!=null&&!url.equals("")){
            if(null!=map.get(url)){
                imageView.setImageBitmap(map.get(url));
            }else{
                int size=url.length()-4;
                String one= (String) url.substring(0,size);
                String path=MyUtils.savepath(context)+one+".jpg";
                Bitmap bitmap=BitmapFactory.decodeFile(path);
                if(bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                    map.put(url,bitmap);
                    Log.d("xzf","从文件中拿图片");
                }else{
                    NewsAsyncTask task=new NewsAsyncTask(tag);
                    task.execute(url);
                    mTask.add(task);
                }
            }
        }
    }


    //这个任务取现所有正在进行中的任务
      public void cancellALLTask(){
        if(mTask!=null){
            for(NewsAsyncTask task:mTask){
                task.cancel(false);
            }
        }
    }





    private class NewsAsyncTask extends AsyncTask<String ,Void,Bitmap> {
        private String tag;

        public NewsAsyncTask(String tag){
           this.tag=tag;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap=getBitmapFromUrl(params[0]);
            //在这里将下载后的图片保存到缓存中
            //只有执行这个任务，说明缓存中没有图片
            if(bitmap!=null){
                addBitmapToCache(params[0],bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView= (ImageView) listView.findViewWithTag(tag);
            if(imageView!=null&&bitmap!=null){
                imageView.setImageBitmap(bitmap);
                if(bitmap.isRecycled()){
                    bitmap.recycle();
                    Log.d("bmobss","清理方法执行了");
                }
                System.gc();
            }
        }
    }





}
