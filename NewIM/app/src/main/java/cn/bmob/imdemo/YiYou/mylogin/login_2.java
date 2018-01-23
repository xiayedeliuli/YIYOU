package cn.bmob.imdemo.YiYou.mylogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.model.UserModel;
import cn.bmob.imdemo.YiYou.UI.mymainactivity;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by DELL on 2017/4/15.
 */

public class login_2 extends BaseActivity{
    private EditText login2_phone;
    private String text;
    private EditText login2_confirm;
    private TextView huoqu;
    private String code;
    TextView timelogin2;
    String phone;
    CountDownTimer cd;
    TextView te;
    boolean iszhaohuizhanghao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_2);
        login2_phone= (EditText) findViewById(R.id.login_2_et_phone);
        timelogin2= (TextView) findViewById(R.id.timelogin2);
        te= (TextView) findViewById(R.id.te);
        iszhaohuizhanghao=getIntent().getBooleanExtra("iszhaohuizhanghao2",false);
        cd =new CountDownTimer(62001,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int t= (int) ((millisUntilFinished/1000)-1);
                timelogin2.setText(""+t);
                String tes="已将6位验证码发送到您的手机上，如果您未收到，"+t+"秒后可以点击重新获取短信验证码";
                te.setText(tes);
            }

            @Override
            public void onFinish() {
                if(timelogin2.getText().equals("0")){
                    huoqu.setEnabled(true);
                    timelogin2.setVisibility(View.GONE);
                    huoqu.setVisibility(View.VISIBLE);
                }
            }
        };
        cd.start();
        text=getIntent().getStringExtra("phone");
        login2_phone.setText(text);
        login2_confirm= (EditText) findViewById(R.id.login_2_et_confirm);
        huoqu= (TextView) findViewById(R.id.chongxinhuoqu);
        huoqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huoqu.setEnabled(false);
                                   phone=login2_phone.getText().toString();
                                   BmobSMS.requestSMSCode(phone, "亦友社交", new QueryListener<Integer>() {
                                       @Override
                                       public void done(Integer integer, BmobException e) {
                                           if(e==null){
                                               Toast.makeText(login_2.this,"短信已重新发送，请及时查看短信", Toast.LENGTH_SHORT).show();
                                               Log.i("xzf","短信发送成功，短信id："+integer);//用于查询本次短信发送详情
                                               cd.start();
                                               timelogin2.setVisibility(View.VISIBLE);
                                               huoqu.setVisibility(View.GONE);
                                           }else{
                                               huoqu.setEnabled(true);
                                               Toast.makeText(login_2.this,"系统问题，请重新点击发送短信", Toast.LENGTH_SHORT).show();
                                               Log.i("xzf","errorCode = "+e.getErrorCode()+",errorMsg = "+e.getLocalizedMessage());

                                           }

                                       }

                                   });


            }
        });

        login2_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone=login2_phone.getText().toString();
                code=login2_confirm.getText().toString();
                Log.d("xzf",code);
                BmobSMS.verifySmsCode(phone, code, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){//短信验证码已验证成功
                            if(iszhaohuizhanghao){
                                final SharedPreferences.Editor editor= welcome.sp.edit();
                                BmobQuery<User> query=new BmobQuery<User>();
                                query.addWhereEqualTo("mobilePhoneNumber",text);
                                query.findObjects(new FindListener<User>() {
                                    @Override
                                    public void done(List<User> list, BmobException e) {
                                        if(e==null){
                                            for(final User my:list){
                                                BmobQuery<MyUser> query1=new BmobQuery<MyUser>();
                                                query1.addWhereEqualTo("userid",my.getObjectId());
                                                query1.findObjects(new FindListener<MyUser>() {
                                                    @Override
                                                    public void done(List<MyUser> list, BmobException e) {
                                                       if(e==null) {
                                                           for(MyUser mys:list){
                                                               editor.putString("SecondChoice","项目");
                                                               editor.putString("objetid11",my.getObjectId());
                                                               editor.putString("manage",my.getManageid());
                                                               editor.putString("userMateID",mys.getObjectId());
                                                               editor.putBoolean("iscomfirfate",false);
                                                               editor.apply();

                                                               //返回主界面
                                                               Toast.makeText(login_2.this,"用户信息更新成功",Toast.LENGTH_SHORT).show();
                                                               Intent intent=new Intent();
                                                               intent.setClass(login_2.this,mymainactivity.class);
                                                               startActivity(intent);
                                                           }
                                                       }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(login_2.this,"短信验证成功，请填写您的相关信息",Toast.LENGTH_SHORT).show();
                                Log.i("xzf", "验证通过");
                                Intent intent=new Intent();
                                intent.setClass(login_2.this,login_3.class);
                                intent.putExtra("phone",text);
                                startActivity(intent);
                            }
                        }else{
                            if(code.length()==6){
                                toast("您输入的验证码不正确，清检查后输入");
                            }
                            Log.i("xzf", "验证失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    //这个方法是DEMO自带的登陆方法，需要传入账户名和密码
    public void onloginclick(String username) {

        UserModel.getInstance().login(username, "123456", new LogInListener() {

            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    Toast.makeText(login_2.this, "用户信息更新成功", Toast.LENGTH_SHORT).show();
                    //这是个更新用户状态的方法
                    User user = (User) o;
                    BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                }
            }
        });

    }
}