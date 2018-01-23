package cn.bmob.imdemo.YiYou.Mate.MateDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.bmob.imdemo.R;


/**
 * Created by Administrator on 2018/1/19.
 */

public class MateDialog extends Dialog {
    private View rootview;
    private Clicklistener mClicklistener;

    public MateDialog(Context context) {
        super(context);
        init();
        setclick();
    }


    private void init(){
        LayoutInflater inflater=LayoutInflater.from(getContext());
        //这个是大对话框用到的view
        rootview=inflater.inflate(R.layout.matedialog,null);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Window window=this.getWindow();
        window.setContentView(rootview);
        window.setWindowAnimations(R.style.AnimBottom);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width= (int) (width*0.95);
        wlp.height= (int) (height*0.95);
        window.setAttributes(wlp);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void setclick(){
        rootview.findViewById(R.id.closedialog_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mClicklistener.click(view,MateDialog.this);
            }
        });
        rootview.findViewById(R.id.TimePicker_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mClicklistener.click(view,MateDialog.this);
            }
        });
        rootview.findViewById(R.id.all_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClicklistener.click(view,MateDialog.this);
            }
        });
        rootview.findViewById(R.id.girl_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClicklistener.click(view,MateDialog.this);
            }
        });
        rootview.findViewById(R.id.boy_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClicklistener.click(view,MateDialog.this);
            }
        });
        rootview.findViewById(R.id.sex_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClicklistener.click(view,MateDialog.this);
            }
        });
        rootview.findViewById(R.id.Item_Llayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClicklistener.click(view,MateDialog.this);
            }
        });
        rootview.findViewById(R.id.MateMode_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClicklistener.click(view,MateDialog.this);
            }
        });
        rootview.findViewById(R.id.Mode1_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClicklistener.click(view,MateDialog.this);
            }
        });
        rootview.findViewById(R.id.Mode2_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClicklistener.click(view,MateDialog.this);
            }
        });
        rootview.findViewById(R.id.MateDialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClicklistener.click(view,MateDialog.this);
            }
        });


    }



    public void setCliklistener(Clicklistener cliklistener){
        mClicklistener=cliklistener;
    }

}
