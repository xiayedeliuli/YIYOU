package cn.bmob.imdemo.YiYou.UI;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;

/**
 * Created by DELL on 2017/6/1.
 */

public class mywallet extends BaseActivity {
    RelativeLayout shouru1;
    RelativeLayout zhichu2;
    TextView gone1;
    TextView gone2;
    ImageView walletback;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mywallet);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        shouru1= (RelativeLayout) findViewById(R.id.shouru1);
        zhichu2= (RelativeLayout) findViewById(R.id.zhichu2);
        gone1= (TextView) findViewById(R.id.textgone1);
        gone2= (TextView) findViewById(R.id.textgone2);
        walletback= (ImageView) findViewById(R.id.walletback);
        walletback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        shouru1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gone1.setVisibility(View.VISIBLE);
                gone2.setVisibility(View.GONE);
            }
        });

        zhichu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gone2.setVisibility(View.VISIBLE);
                gone1.setVisibility(View.GONE);
            }
        });




    }

    public void tixian(View v){
        Toast.makeText(mywallet.this,"钱都没有，提毛提",Toast.LENGTH_SHORT).show();
    }

}
