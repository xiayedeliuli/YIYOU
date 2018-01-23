package cn.bmob.imdemo.YiYou.mylogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by DELL on 2017/4/15.
 */

public class login_1 extends BaseActivity {

    private EditText number;
    private Button button;
//    TextView zhanghao;
    TextView zhanghaotxt;
    boolean iszhaohuizhanghao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_1);
        zhanghaotxt= (TextView) findViewById(R.id.number);
//        zhanghao= (TextView) findViewById(R.id.zhanghao);
        number= (EditText) findViewById(R.id.login_1_et_phone);
        button= (Button) findViewById(R.id.login_1_next);
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("xzf","正在输入号码，这里还缺少一个判断手机号码的正则表达式");
                if(number.getText().length()==11){
                    button.setTextColor(Color.WHITE);
                }else {
                    button.setTextColor(getResources().getColor(R.color.login));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        iszhaohuizhanghao=getIntent().getBooleanExtra("iszhaohuizhanghao",false);
        if(iszhaohuizhanghao){
            zhanghaotxt.setText("请输入原手机号登陆");
        }
//        zhanghao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!iszhaohuizhanghao){
//                    Intent intent=new Intent();
//                    intent.setClass(login_1.this,login_1.class);
//                    intent.putExtra("iszhaohuizhanghao",true);
//                    startActivity(intent);
//                }else{
//                    toast("您已在登陆页面，请直接输入手机号码");
//                }
//
//            }
//        });
    }

    public void login_1_next(View v){
        //输入手机号码长度为0的提示
        if(number.getText().toString().length()==11){
            final String phone=number.getText().toString();
            BmobSMS.requestSMSCode(phone, "亦友社交", new QueryListener<Integer>() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if(e==null){//
                        Toast.makeText(login_1.this,"短信已发送成功,请及时查看手机",Toast.LENGTH_SHORT).show();
                        Log.i("xzf","短信发送成功，短信id："+integer);//用于查询本次短信发送详情
                    }else{
                        Log.i("xzf","errorCode = "+e.getErrorCode()+",errorMsg = "+e.getLocalizedMessage());
                    }

                }

            });

            Intent intent=new Intent();
            intent.setClass(login_1.this,login_2.class);
            intent.putExtra("phone",phone);
            if(iszhaohuizhanghao){
                intent.putExtra("iszhaohuizhanghao2",true);
            }
            startActivity(intent);
        }else {
            Toast.makeText(this,"请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        }

    }
}