package cn.bmob.imdemo.YiYou.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.model.UserModel;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * Created by DELL on 2017/6/1.
 */

public class changeuserinfo extends BaseActivity {
    ImageView setback;
    ImageView folderup;
    EditText ednicheng;
    EditText edqianming;
    EditText edyundong;
    EditText edyinyue;
    EditText edshuji;
    EditText eddianying;
    EditText edshiwu;
    EditText edlvxing;
    GridView gridview;
    //这个path用来存放所有的图片，包括从相册新选的，以前的旧的，还有默认
    private ArrayList<String> path = new ArrayList<>();
    ChangeInfoGridViewAdapter adapter;
    //这个oldpath单独存放老的图片，当删除时应该和path同时删除
    List<String> oldpath;
    ProgressDialog pd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeuserinfo);
        //状态栏透明
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        eddianying= (EditText) findViewById(R.id.eddianying);
        edlvxing= (EditText) findViewById(R.id.edlvxing);
        ednicheng= (EditText) findViewById(R.id.ednicheng);
        edqianming= (EditText) findViewById(R.id.edqianming);
        edshiwu= (EditText) findViewById(R.id.edshiwu);
        edyundong= (EditText) findViewById(R.id.edyundong);
        edyinyue= (EditText) findViewById(R.id.edyinyue);
        edshuji= (EditText) findViewById(R.id.edshuji);
        setback= (ImageView) findViewById(R.id.setback);
        folderup= (ImageView) findViewById(R.id.folderup);
        gridview= (GridView) findViewById(R.id.grid);
        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        String[] arr=getIntent().getStringArrayExtra("string");
        if(null!=arr){
            edyundong.setText(arr[0]);
            edyinyue.setText(arr[1]);
            edshuji.setText(arr[2]);
            eddianying.setText(arr[3]);
            edshiwu.setText(arr[4]);
            edlvxing.setText(arr[5]);
        }
        ednicheng.setText(getIntent().getStringExtra("username"));
        edqianming.setText(getIntent().getStringExtra("qianming"));

       oldpath=getIntent().getStringArrayListExtra("path");
        //初始化上传照片的布局
        if(null!=oldpath){
            path.addAll(oldpath);
        }
        path.add("默认");
        adapter=new ChangeInfoGridViewAdapter(changeuserinfo.this,path);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=(path.size()-1)){
                  update(path.get(position));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("是否修改您的信息");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("xzf7","3"+path.size());
                path.remove("默认");
                Log.d("xzf7","4"+path.size());
                if(null!=oldpath){
                    if(oldpath.size()>0){
                        Log.d("xzf7","特别2"+oldpath.size());
                        path.removeAll(oldpath);
                        Log.d("xzf7","5"+path.size());
                    }
                }
                Log.d("xzf7","6"+path.size());
                baocun(path);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }

    private void baocun(List<String> pathList){
        pd=new ProgressDialog(changeuserinfo.this);
        pd.setTitle("正在保存资料");
        pd.setMessage("正在上传，请稍候");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);//在setProgressStyle属性里可以设置圆形框和进度条
        pd.setCancelable(true);
        pd.show();
        if(pathList.size()>0){
            Log.d("xzf7","有图片更新方法");
            final String[] uploadpath = pathList.toArray(new String[0]);
            BmobFile.uploadBatch(uploadpath, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if(list1.size()==uploadpath.length){//如果数量相等，则代表文件全部上传完成
                        //do something
                        final User use = BmobUser.getCurrentUser(User.class);
                        String[] arr=new String[]{edyundong.getText().toString(),edyinyue.getText().toString(),
                                edshuji.getText().toString(),eddianying.getText().toString(),edshiwu.getText().toString()
                                ,edlvxing.getText().toString()};
                        User user=new User();
                        user.setInterest(arr);
                        if(null!=oldpath){
                            list1.addAll(oldpath);
                        }
                        user.setPictures(list1);
                        user.setUsername(ednicheng.getText().toString());
                        user.setQianming(edqianming.getText().toString());
                        user.update(use.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    MyUser myUser=new MyUser();
                                    myUser.setAreJianting(true);
                                    myUser.setUsername(ednicheng.getText().toString());
                                    myUser.update(sp.getString("userMateID", ""), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                UserModel.getInstance().login(ednicheng.getText().toString(), "123456", new LogInListener() {
                                                    @Override
                                                    public void done(Object o, BmobException e) {
                                                        if (e == null) {
                                                            //这是个更新用户状态的方法
                                                            User user = (User) o;
                                                            BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                                                            Toast.makeText(changeuserinfo.this, "用户信息更新成功", Toast.LENGTH_SHORT).show();
                                                            pd.cancel();
                                                            finish();
                                                        }else{
                                                            Log.d("sss",e.getErrorCode()+e.getMessage());
                                                        }
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(changeuserinfo.this,"系统错误，请重新尝试",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    Log.d("sss","保存失败"+e.getErrorCode()+e.getMessage()+use.getObjectId());
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

                }
            });
        }else{
            Log.d("xzf7","无图片更新方法");
            final User use = BmobUser.getCurrentUser(User.class);
            String[] arr=new String[]{edyundong.getText().toString(),edyinyue.getText().toString(),
                    edshuji.getText().toString(),eddianying.getText().toString(),edshiwu.getText().toString()
                    ,edlvxing.getText().toString()};
            User user=new User();
            user.setInterest(arr);
            user.setPictures(oldpath);
            user.setUsername(ednicheng.getText().toString());
            user.setQianming(edqianming.getText().toString());
            user.update(use.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        MyUser myUser=new MyUser();
                        myUser.setAreJianting(true);
                        myUser.setUsername(ednicheng.getText().toString());
                        myUser.update(sp.getString("userMateID", ""), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    UserModel.getInstance().login(ednicheng.getText().toString(), "123456", new LogInListener() {
                                        @Override
                                        public void done(Object o, BmobException e) {
                                            if (e == null) {
                                                //这是个更新用户状态的方法
                                                User user = (User) o;
                                                BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                                                Toast.makeText(changeuserinfo.this, "用户信息更新成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else{
                                                Log.d("sss",e.getErrorCode()+e.getMessage());
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(changeuserinfo.this,"系统错误，请重新尝试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Log.d("sss","保存失败"+e.getErrorCode()+e.getMessage()+use.getObjectId());
                    }
                }
            });
        }
    }

    //图片回调方法
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> list) {
            gridview.setVisibility(View.VISIBLE);
            path.clear();
            if(null!=oldpath){
                path.addAll(oldpath);
            }
            for (LocalMedia my : list) {
                String onepath = my.getCompressPath();
                path.add(onepath);
            }
            path.add("默认");
            //这是显示选择照片的gridview
            adapter=new ChangeInfoGridViewAdapter(getApplicationContext(),path);
            gridview.setAdapter(adapter);
        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {
        }
    };

    class ChangeInfoGridViewAdapter extends BaseAdapter {
        private Context context;
        private List<String> images=new ArrayList<>();

        private ChangeInfoGridViewAdapter(Context context, List<String> images) {
            this.images=images;
            this.context=context;

        }

        @Override
        public int getCount() {
           return  images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            int size=images.size()-1;
            if(position!=size){
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    // 获得容器
                    convertView = LayoutInflater.from(this.context).inflate(R.layout.gridviewadapter, parent,false);
                    // 初始化组件
                    viewHolder.gridimage = (ImageView) convertView.findViewById(R.id.gridimage);
                    // 给converHolder附加一个对象
                    convertView.setTag(viewHolder);
                } else {
                    // 取得converHolder附加的对象
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                // 给组件设置资源
                Log.d("xzf",""+images.get(position));
                Bitmap bitmap= BitmapFactory.decodeFile(images.get(position));
                if(null!=bitmap){
                    viewHolder.gridimage.setImageBitmap(bitmap);
                }else{
                    new ImageLoader().showImageByThread(viewHolder.gridimage,images.get(position));
                }
                viewHolder.gridimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                RelativeLayout dianji2 = (RelativeLayout) convertView.findViewById(R.id.dianji2);
                dianji2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update(images.get(position));
                    }
                });
                return convertView;
            } else {
                View myview = LayoutInflater.from(this.context).inflate(R.layout.lastgridview, parent, false);
                RelativeLayout dianji = (RelativeLayout) myview.findViewById(R.id.newinfo);
                dianji.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int max = 7 - (path.size() - 1);
                        FunctionOptions options = new FunctionOptions.Builder()
                                .setMaxSelectNum(max)
                                .setImageSpanCount(3)
                                .setEnablePreview(true)
                                .setCompress(true)
                                .setEnableQualityCompress(true)
                                .setCompressQuality(10)
                                .setEnablePixelCompress(true)
                                .setCompressH(1280)
                                .setCompressW(720)
                                .create();
                        PictureConfig.getInstance().init(options).openPhoto(changeuserinfo.this, resultCallback);
                    }
                });
                return myview;
            }
        }

        class ViewHolder {
            private ImageView gridimage;
        }
    }

    private void update(String object){
        showdialog(object);
    }

    private void showdialog(final String objectid){
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.show();
        Window window=dialog.getWindow();
        if(window!=null){
            window.setContentView(R.layout.showdialog);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.AnimBottom);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.findViewById(R.id.quxiaos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            window.findViewById(R.id.shanchu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    path.remove(objectid);
                    adapter.notifyDataSetChanged();
                    if(null!=oldpath){
                        if(oldpath.size()>0){
                            oldpath.remove(objectid);
                        }
                    }

                }
            });
        }
    }
}
