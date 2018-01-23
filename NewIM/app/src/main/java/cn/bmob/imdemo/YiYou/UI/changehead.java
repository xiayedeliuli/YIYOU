package cn.bmob.imdemo.YiYou.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.imdemo.YiYou.bean.complain;
import cn.bmob.imdemo.YiYou.mylogin.welcome;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * Created by DELL on 2017/6/1.
 */

public class changehead extends BaseActivity {
    ImageView setback;
    RelativeLayout change;
    ImageView myhead;
    User user;
    boolean ischangehead;
    TextView headtext;
    TextView push;
    TextView textg;
    File file;
    ProgressDialog pd;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changehead);
        //状态栏透明
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        user = BmobUser.getCurrentUser(User.class);
        headtext= (TextView) findViewById(R.id.headtext);
        myhead= (ImageView) findViewById(R.id.myhead);
        push= (TextView) findViewById(R.id.push);
        textg= (TextView) findViewById(R.id.textg);
        ischangehead=getIntent().getBooleanExtra("changehead",true);
        if(ischangehead){
            new ImageLoader().showImageByThread(myhead,user.getAvatar());
        }else{
            push.setVisibility(View.VISIBLE);
            push.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(changehead.this,mymainactivity.class);
                    startActivity(intent);
                    //
                }
            });
            if(null!=user.getRealavatar()&user.getRealavatar().length()>0){
                new ImageLoader().showImageByThread(myhead,user.getRealavatar());
                textg.setText("验证学号");
            }else{
                headtext.setText("上传个人照片");
                textg.setText("打开相册");
                Toast.makeText(changehead.this,"请上传个人真实照片，如后台验证非真实照片，将会封号,无法恢复",Toast.LENGTH_SHORT).show();
            }
        }
        change= (RelativeLayout) findViewById(R.id.change);
        setback= (ImageView) findViewById(R.id.setback);
        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textg.getText().equals("打开相册")){
                    FunctionOptions options = new FunctionOptions.Builder()
                            .setMaxSelectNum(1)
                            .setImageSpanCount(3)
                            .setEnablePreview(true)
                            .setCompress(true)
                            .create();
                    PictureConfig.getInstance().init(options).openPhoto(changehead.this, resultCallback);
                }else if(textg.getText().equals("确定上传")){
                    upload(file);
                }else if(textg.getText().equals("验证学号")){
                    comfirmdialog("");
                }
            }
        });

    }

    //图片回调方法
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> list) {
            for (LocalMedia my : list) {
                Bitmap bitmap= BitmapFactory.decodeFile(my.getCompressPath());
                myhead.setImageBitmap(bitmap);
                file=new File(my.getCompressPath());
                textg.setText("确定上传");
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {
        }
    };


    private void comfirmdialog(final String realavater){
        final View myview=View.inflate(changehead.this,R.layout.studentcomfirmstate,null);
        final AlertDialog dialog=new AlertDialog.Builder(this).setView(myview).create();
        dialog.show();
        //2.得到window对象并设置相关属性
        final Window window=dialog.getWindow();
        if(window!=null){
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
            window.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toast("恭喜您上传资料成功，验证成功后，积分会实时下发到您的账号中");
                    SharedPreferences.Editor editor= welcome.sp.edit();
                    editor.putBoolean("iscomfirfate",true);
                    editor.apply();
                    dialog.cancel();
                    pd.cancel();
                    //发送一条验证消息给客户端，并保存他的验证数据
                    EditText key= (EditText) myview.findViewById(R.id.xeuhao);
                    EditText password= (EditText) myview.findViewById(R.id.mima);
                    send(key.getText().toString(),password.getText().toString(),realavater);

                }
            });

        }

    }

    private void upload(File file){
        pd=new ProgressDialog(changehead.this);
        pd.setTitle("任务执行中");
        pd.setMessage("正在进行，请稍候");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);//在setProgressStyle属性里可以设置圆形框和进度条
        pd.setCancelable(true);
        pd.show();
        final BmobFile bmobFile=new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.d("ssss","上传成功1");
                    update1(bmobFile);
                }else{
                    Log.d("ssss",""+e.getErrorCode()+e.getMessage());
                    toast("上传失败，请点击重试");
                }
            }
        });
    }


    private void update1(final BmobFile bmobFile){
        final User use=new User();
        if(ischangehead){
            use.setAvatar(bmobFile.getFileUrl());
        }else{
            use.setRealavatar(bmobFile.getFileUrl());
        }
        use.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null) {
                    Toast.makeText(changehead.this, "照片上传成功", Toast.LENGTH_SHORT).show();
                    update2(bmobFile);
                }
            }
        });
    }

    private void update2(final BmobFile bmobFile){
        MyUser myUser=new MyUser();
        if(ischangehead){
            myUser.setUseravatar(bmobFile.getFileUrl());
        }else{
            myUser.setUserRealAvater(bmobFile.getFileUrl());
        }
        myUser.setAreJianting(true);
        myUser.update(sp.getString("userMateID", ""), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e!=null) {
                    Log.d("ssss", "" + e.getErrorCode() + e.getMessage());
                }else{
                    if(ischangehead){
                        pd.cancel();
                        textg.setText("打开相册");
                    }else{
                        textg.setText("上传完毕");
                        comfirmdialog(bmobFile.getFileUrl());
                    }
                }
            }
        });
    }

    private void send(final String key, final String password, final String realavater){
        User user=new User();
        user.setStudentid(key);
        user.setStudentkey(password);
        user.update(sp.getString("objetid11", ""), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //发送一条数据
                    send2(key,password,realavater);
                }
            }
        });
    }

    private void send2(final String id, final String key, final String avater){
        complain complain=new complain();
        complain.setType("验证");
        complain.setCompleted(false);
        complain.setUSERID(user.getObjectId());
        complain.setUSERname(user.getUsername());
        if(null!=user.getMyReferences()&user.getMyReferences().length()>0){
            complain.setReferences(user.getMyReferences());
        }else{
            complain.setReferences("");
        }
        complain.setStudentid(id);
        complain.setManageid(user.getManageid());
        complain.setStudentkey(key);
        if(avater.length()>0){
            complain.setRealavater(avater);
        }else{
            complain.setRealavater(user.getRealavatar());
        }
        complain.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
    }





}
