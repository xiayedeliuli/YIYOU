package cn.bmob.imdemo.YiYou.mylogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.YiYou.Mate.Mate;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.UI.mymainactivity;
import cn.bmob.imdemo.YiYou.bean.ManagementTable;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * Created by DELL on 2017/4/15.
 */

public class login_3 extends BaseActivity {

    private EditText ed1;
    private TextView ed3;
    private String number;
    private ImageView mytouxiang;
    private String url;
    Button tijiao;
    TextView studentkey;
    EditText studentpass;
    TextView sex1;
    TextView sex2;
    String sex;
    Spinner spinner;
    List<String> data_list;
    String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_3);
        sex="";
        sex1= (TextView) findViewById(R.id.sex1);
        sex2= (TextView) findViewById(R.id.sex2);
        studentkey= (TextView) findViewById(R.id.studentkey);
        studentpass= (EditText) findViewById(R.id.studentpass);
        tijiao= (Button) findViewById(R.id.tijiao);
        mytouxiang= (ImageView) findViewById(R.id.mytouxiang);
        ed1= (EditText) findViewById(R.id.login3_nickname);
        ed3= (TextView) findViewById(R.id.login3_sex);
        spinner= (Spinner) findViewById(R.id.spinner);
        //数据
        data_list = new ArrayList<String>();
        data_list.add("南京财经大学（仙林校区）");
        data_list.add("南京邮电大学（仙林校区）");
        data_list.add("南京师范大学（仙林校区）");
        //适配器
        ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index=data_list.get(position).substring(0,6);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        studentkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentkey.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
            }
        });
        ed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  sex1.setVisibility(View.VISIBLE);
                  sex2.setVisibility(View.VISIBLE);
                  ed3.setVisibility(View.GONE);
            }
        });

        number=getIntent().getStringExtra("phone");
        mytouxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionOptions optionss = new FunctionOptions.Builder().setMaxSelectNum(1).setCompress(true).create();
                PictureConfig.getInstance().init(optionss).openPhoto(login_3.this, resultCallback);
            }
        });
        sex1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex="男";
                sex1.setTextColor(getResources().getColor(R.color.black));
                sex2.setTextColor(getResources().getColor(R.color.ccc));
            }
        });
        sex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex="女";
                sex1.setTextColor(getResources().getColor(R.color.ccc));
                sex2.setTextColor(getResources().getColor(R.color.black));
            }
        });
       ed1.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               String editable = ed1.getText().toString();
               String str = stringFilter(editable);
               if(editable.length()>=6){
                   toast("只允许输入6位以内的字母，数字和汉字");
               }else{
                   if(!editable.equals(str)){
                       ed1.setText(str);
                       //设置新的光标所在位置
                       ed1.setSelection(str.length());
                       toast("只允许输入6位以内的字母，数字和汉字");
                   }
               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
    }

    public void tijiao(View v){
        final String nickname=ed1.getText().toString();
        boolean ct=mytouxiang.getDrawable().getCurrent().getConstantState()
                ==getResources().getDrawable(R.drawable.mytou6).getConstantState();
        if(nickname.length()!=0&sex.length()!=0&index.length()!=0&!ct){
            tijiao.setEnabled(false);
            Log.d("new","if方法启动了");
            final ManagementTable manage=new ManagementTable();
            manage.setJishu(0);
            manage.setBeoff(false);
            manage.setDongjie(false);
            manage.setStudentcomfirm(false);
            manage.setIsfenghao(false);
            manage.save(new SaveListener<String>() {
                @Override
                public void done(final String ss, BmobException e) {
                    User user=new User();
                    user.setUsername(nickname);
                    //默认的密码，主要是我后面用不到
                    user.setPassword("123456");
                    user.setManageid(ss);
                    user.setMobilePhoneNumber(number);
                    user.setQianming("");
                    if(studentpass.getText().length()>0){
                        user.setMyReferences(studentpass.getText().toString());
                    }else{
                        user.setMyReferences("");
                    }
                    user.setConfirmState(false);
                    user.setSex(sex);
                    user.setStudentid("");
                    user.setStudentkey("");
                    user.setIndex(index);
                    String avatar=url;
                    user.setAvatar(avatar);
                    user.setRealavatar("");
                    user.setMywallet(1);
                    user.setAweek(7);
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User s, BmobException e) {
                            if(e==null){
                                Mate mate=new Mate();
                                mate.setBelongToUerID(s.getObjectId());
                                mate.setSex(sex);
                                mate.setMateSex("");
                                mate.setMateItem("");
                                mate.setMateSex("");
                                mate.setSynchronizedID("");
                                String[] certainmateuser=new String[]{s.getObjectId(),s.getUsername(),s.getAvatar()};
                                mate.setCertainMateUser(certainmateuser);
                                mate.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e==null){
                                            SharedPreferences.Editor editor= welcome.sp.edit();
                                            editor.putString("Mate",s);
                                            editor.apply();
                                        }
                                    }
                                });

                                //下面注册用户的匹配表
                                final String ID=s.getObjectId();
                                MyUser myUser=new MyUser();
                                myUser.setAreJianting(true);
                                myUser.setUserid(s.getObjectId());
                                myUser.setUsername(s.getUsername());
                                myUser.setUseravatar(s.getAvatar());
                                myUser.setUsersex(s.getSex());
                                myUser.setUserRealAvater("");
                                myUser.setWasMated("尚未匹配");
                                myUser.setUsersex(s.getSex());
                                myUser.setItem("");
                                myUser.setDate((long) 0);
                                myUser.setCountMatePeriod( 0);
                                myUser.setChoicesex("");
                                myUser.setSecondDemond("项目");
                                myUser.setOnback(false);
                                myUser.setEnsure(false);
                                myUser.setOtherObjectId("");
                                myUser.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e==null){
                                            Toast.makeText(login_3.this,"恭喜您注册成功", Toast.LENGTH_SHORT).show();
                                            //到达这一步创建成功后，把手机号码和用户名存在本地
                                            SharedPreferences.Editor editor= welcome.sp.edit();
                                            //无论手机号码或者昵称等资料被改变，ID这一行是不变的
                                            editor.putString("objetid11",ID);
                                            editor.putString("userMateID",s);
                                            editor.putString("SecondChoice","项目");
                                            editor.putString("manage",ss);
                                            editor.putBoolean("isfirstin",true);
                                            editor.apply();
                                            //在这里转入界面
                                            Intent intent=new Intent();
                                            intent.setClass(login_3.this,mymainactivity.class);
                                            intent.putExtra("isfromlogin3",true);
//                                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }else{
                                            tijiao.setEnabled(true);
                                            Toast.makeText(login_3.this,"错误代码"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else{
                                if(e.getErrorCode()==301){
                                    Toast.makeText(login_3.this,"请输入有效的邮箱地址", Toast.LENGTH_SHORT).show();
                                }else{
                                    tijiao.setEnabled(true);
                                    Toast.makeText(login_3.this,"错误代码"+e.getErrorCode()+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
                }
            });
        }else{
            Toast.makeText(this,"请填写完信息", Toast.LENGTH_SHORT).show();
        }

    }




    //图片回调方法
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback(){
        @Override
        public void onSelectSuccess(List<LocalMedia> list) {
            for(LocalMedia my:list){
                String path=my.getCompressPath();
                Bitmap bitmap= BitmapFactory.decodeFile(path);
                mytouxiang.setImageBitmap(bitmap);
                if(bitmap.isRecycled()){
                    bitmap.recycle();
                }
                File file=new File(path);
                final BmobFile bmobFile=new BmobFile(file);
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            url=bmobFile.getFileUrl();
                        }
                    }
                });
            }

        }

        @Override
        public void onSelectSuccess(LocalMedia localMedia) {
        }
    };


    public static String stringFilter(String str)throws PatternSyntaxException {
        // 只允许字母、数字和汉字
        String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

}