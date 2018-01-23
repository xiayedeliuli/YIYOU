package cn.bmob.imdemo.YiYou.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.ListInfo;
import cn.bmob.imdemo.YiYou.myadapter.GridViewAdapter;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by DELL on 2017/4/21.
 */

public class newinfo extends AppCompatActivity {
    private TextView fabu;
    private EditText edit;
    TextView cancel;
    private String infos;
    //存放选择照片的地址
    private ArrayList<String> path = new ArrayList<>();
    private ImageView imageView;
    private List<String> filepaths=new ArrayList<String>();
    private GridView gridView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newinfo);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        cancel= (TextView) findViewById(R.id.cancel);
        fabu = (TextView) findViewById(R.id.fabu);
        edit = (EditText) findViewById(R.id.edit);
        imageView = (ImageView) findViewById(R.id.image);
        gridView= (GridView) findViewById(R.id.gridview);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击图片的监听事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duoxuan();
            }
        });

        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.getText().length()==0){
                    Toast.makeText(newinfo.this,"说点什么吧", Toast.LENGTH_SHORT).show();
                }else{
                    ListInfo info=new ListInfo();
                    final User user = BmobUser.getCurrentUser(User.class);
                    if(path.size()>0){
                        uploadpngfile(path);
                        info.setFirsturl(path.get(0));
                        String[] array=(String[]) path.toArray(new String[path.size()]);
                        info.setArr(array);
                    }else{
                        noPicture();
                    }
                    //单例刷新用到的数据
                    info.setIndex(user.getIndex());
                    info.setName(user.getUsername());
                    info.setInfosex(user.getSex());
                    info.setInfo(edit.getText().toString());
                    info.setZan(0);
                    info.setZa(0);
                    info.setPing(0);
                    info.setSystem(MyUtils.getDateTimeFromMillisecond(System.currentTimeMillis()));
                    info.setUseridmanage(user.getManageid());
                    info.setUserid(user.getObjectId());
                    info.setUserimage(user.getAvatar());
                    finish();
                    Intent intent = new Intent("fragment");
                    intent.putExtra("frag", "上传");
                    intent.putExtra("frag2",info);
                    sendBroadcast(intent);
                }
            }
        });

    }

    //这个方法是点一下打开图片选择器的方法
    public void duoxuan() {
        FunctionOptions options = new FunctionOptions.Builder()
                .setMaxSelectNum(8)
                .setImageSpanCount(3)
                .setEnablePreview(true)
                .setCompress(true)
                .setEnableQualityCompress(true)
                .setCompressQuality(10)
                .setEnablePixelCompress(true)
                .setCompressH(1280)
                .setCompressW(720)
                .create();
        PictureConfig.getInstance().init(options).openPhoto(newinfo.this, resultCallback);
    }

    //图片回调方法
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> list) {
            path.clear();
            for (LocalMedia my : list) {
                String onepath = my.getCompressPath();
                path.add(onepath);
            }
            //这是显示选择照片的gridview
            GridViewAdapter adapter=new GridViewAdapter(newinfo.this,path);
            gridView.setAdapter(adapter);
        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {
        }
    };


    public void uploadpngfile(final List<String> pathList) {
        final int ss=(int) (Math.random() * 100000);
                        // 下面的循环将图片压缩，并保存成文件
                        for (final String path : pathList) {
                            File file=new File(path);
                            if(file.length()/1024>100){
                                //根据路径获得图片后进行压缩
                            Bitmap bitmap= MyUtils.getimage(path,true);
                            Log.d("xz",(bitmap.getRowBytes()*bitmap.getHeight())+"");
                            int i=(int) (Math.random() * 100000);
                            File file2=MyUtils.dongtaisaveBitmapFile(bitmap,i,ss);
                            Log.d("xz",""+file2.length());
                                try {
                                    filepaths.add(file2.getCanonicalPath());
                                    Log.d("path", "" + file2.getCanonicalPath());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }else{;
                                try {
                                    filepaths.add(file.getCanonicalPath());
                                    Log.d("path", "" + file.getCanonicalPath());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }

                        }
                        final String[] array=(String[]) filepaths.toArray(new String[filepaths.size()]);
                        BmobFile.uploadBatch(array, new UploadBatchListener() {
                            @Override
                            public void onSuccess(List<BmobFile> list, final List<String> list1) {
                                if (list1.size() == array.length) {
                                    final String[] array=(String[]) list1.toArray(new String[list1.size()]);
                                    //如果数量相等，则代表文件全部上传完成
                                    final User user = BmobUser.getCurrentUser(User.class);
                                    infos = edit.getText().toString();
                                    String index = "南京财经大学";
                                     ListInfo info = new ListInfo();
                                    info.setIndex(index);
                                    info.setName(user.getUsername());
                                    info.setNumber(user.getMobilePhoneNumber());
                                    info.setInfosex(user.getSex());
                                    info.setInfo(infos);
                                    info.setZan(0);
                                    info.setZa(0);
                                    info.setPing(0);
                                    info.setUseridmanage(user.getManageid());
                                    info.setArr(array);
                                    info.setFirsturl(list1.get(0));
                                    info.setUserid(user.getObjectId());
                                    info.setUserimage(user.getAvatar());
                                    info.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if(e==null){
                                                Intent intent = new Intent("fragment");
                                                intent.putExtra("frag", "完成");
                                                sendBroadcast(intent);
                                            }
                                            else{
                                                Log.d("xzf",e.getErrorCode()+e.getMessage());
                                            }
                                        }
                                    });

                                }

                            }

                            @Override
                            public void onProgress(int i, int i1, int i2, int i3) {

                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(newinfo.this,"错误码"+i +",错误描述："+s,Toast.LENGTH_SHORT).show();
                            }
                        });
    }




    public  void noPicture(){
        final User user = BmobUser.getCurrentUser(User.class);
        infos = edit.getText().toString();
        String index = "南京财经大学";
        ListInfo info = new ListInfo();
        info.setIndex(index);
        info.setName(user.getUsername());
        info.setNumber(user.getMobilePhoneNumber());
        info.setInfosex(user.getSex());
        info.setInfo(infos);
        info.setZan(0);
        info.setZa(0);
        info.setPing(0);
        info.setUseridmanage(user.getManageid());
        info.setUserid(user.getObjectId());
        info.setUserimage(user.getAvatar());
        info.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Intent intent = new Intent("fragment");
                    intent.putExtra("frag", "完成");
                    sendBroadcast(intent);
                }
            }
        });
    }


}
