package cn.bmob.imdemo.YiYou.Mate.MateDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.YiYou.Mate.MateDialog.MateDialog;
import cn.bmob.imdemo.YiYou.Mate.MateFragment;
import cn.bmob.imdemo.YiYou.Mate.MateMethod;
import cn.bmob.imdemo.YiYou.MyTimeDialog.DateTimePickerDialog;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;

/**
 * Created by Administrator on 2018/1/22.
 */

public class mClicklistener implements Clicklistener {
    private MateFragment mateFragment;
    private Long MateTime=0L;
    private String MateSex="";
    //0标识即时匹配模式，1表示普通匹配模式
    private int  MateMode=0;
    private String MateItem="";


    public mClicklistener(MateFragment context) {
        this.mateFragment = context;
    }

    @Override
    public void click(final View view, final Dialog dialog) {
        switch (view.getId()){
            case R.id.closedialog_img:
                //对话框消失，清空已保存的数据
                MateTime= 0L;
                MateSex="";
                MateMode=0;
                MateItem="";
               dialog.dismiss();
                break;
            case R.id.TimePicker_txt:
                DateTimePickerDialog TimeDialog = new DateTimePickerDialog(mateFragment.getContext(), System.currentTimeMillis());
        /**
         * 实现接口
         */
         TimeDialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
             @Override
             public void OnDateTimeSet(AlertDialog dialog, long date) {
                 TextView TimePicker_txt= (TextView) view;
                 TimePicker_txt.setText(MyUtils.getStringDate(date));
                 TimePicker_txt.setTextColor(Color.BLACK);
                 MateTime=date;
             }
         });
        TimeDialog.show();
                break;

            case R.id.sex_txt:
                view.setVisibility(View.GONE);
                dialog.findViewById(R.id.sex_Llayout).setVisibility(View.VISIBLE);
                break;

            case R.id.all_txt:
                TextView all_txt= (TextView) dialog.findViewById(R.id.all_txt);
                all_txt.setTextColor(Color.BLACK);
                TextView girl_txt= (TextView) dialog.findViewById(R.id.girl_txt);
                girl_txt.setTextColor(mateFragment.getResources().getColor(R.color.colorccc));
                TextView boy_txt= (TextView) dialog.findViewById(R.id.boy_txt);
                boy_txt.setTextColor(mateFragment.getResources().getColor(R.color.colorccc));
                MateSex="不限";
                break;

            case R.id.girl_txt:
                TextView all_txt1= (TextView) dialog.findViewById(R.id.all_txt);
                all_txt1.setTextColor(mateFragment.getResources().getColor(R.color.colorccc));
                TextView girl_txt1= (TextView) dialog.findViewById(R.id.girl_txt);
                girl_txt1.setTextColor(Color.BLACK);
                TextView boy_txt1= (TextView) dialog.findViewById(R.id.boy_txt);
                boy_txt1.setTextColor(mateFragment.getResources().getColor(R.color.colorccc));
                MateSex="女";
                break;

            case R.id.boy_txt:
                TextView all_txt2= (TextView) dialog.findViewById(R.id.all_txt);
                all_txt2.setTextColor(mateFragment.getResources().getColor(R.color.colorccc));
                TextView girl_txt2= (TextView) dialog.findViewById(R.id.girl_txt);
                girl_txt2.setTextColor(mateFragment.getResources().getColor(R.color.colorccc));
                TextView boy_txt2= (TextView) dialog.findViewById(R.id.boy_txt);
                boy_txt2.setTextColor(Color.BLACK);
                MateSex="男";
                break;

            case R.id.Item_Llayout:
                final GridDialog gridDialog=new GridDialog(mateFragment.getContext());
                gridDialog.show();
                gridDialog.setOnGridViewClick(new GridDialog.OnGridViewClick() {
                    @Override
                    public void Click(String item, Integer icon) {
                        MateItem=item;
                        gridDialog.dismiss();
                        TextView Itemicon_txt= (TextView) dialog.findViewById(R.id.Itemicon_txt);
                        Itemicon_txt.setText(MateItem);
                        Itemicon_txt.setVisibility(View.VISIBLE);
                        ImageView Itemicon_img= (ImageView) dialog.findViewById(R.id.Itemicon_img);
                        Itemicon_img.setImageResource(icon);
                        Itemicon_img.setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.Item_txt).setVisibility(View.GONE);
                    }
                });
                break;

            case R.id.MateMode_txt:
                view.setVisibility(View.GONE);
                TextView Mode1_txt= (TextView) dialog.findViewById(R.id.Mode1_txt);
                Mode1_txt.setVisibility(View.VISIBLE);
                TextView Mode2_txt= (TextView) dialog.findViewById(R.id.Mode2_txt);
                Mode2_txt.setVisibility(View.VISIBLE);
                break;
            case R.id.Mode1_txt:
                MateMode=0;
                TextView Mode1= (TextView) dialog.findViewById(R.id.Mode1_txt);
                Mode1.setTextColor(Color.BLACK);
                TextView Mode2= (TextView) dialog.findViewById(R.id.Mode2_txt);
                Mode2.setTextColor(mateFragment.getResources().getColor(R.color.colorccc));
                break;

            case R.id.Mode2_txt:
                MateMode=1;
                TextView Mode1_1= (TextView) dialog.findViewById(R.id.Mode1_txt);
                Mode1_1.setTextColor(mateFragment.getResources().getColor(R.color.colorccc));
                TextView Mode2_2= (TextView) dialog.findViewById(R.id.Mode2_txt);
                Mode2_2.setTextColor(Color.BLACK);
                break;

            case R.id.MateDialog_btn:
                Log.v("mClicklistener","匹配项目"+MateItem+"--匹配性别"+MateSex+"--匹配模式"+MateMode+"--匹配时间"+
                        MyUtils.getStringDate(MateTime));
                if(MateItem.length()==0||MateTime==0L||MateSex.length()==0||dialog.findViewById(R.id.MateMode_txt).getVisibility()
                        ==View.VISIBLE){
                    Toast.makeText(mateFragment.getContext(),"您还有信息没有填写，请仔细检查",Toast.LENGTH_LONG).show();
                }else{
                   //先把所有数据保存到数据类MateContains中
                    dialog.dismiss();
                    mateFragment.getMateContains().setMatesex(MateSex);
                    mateFragment.getMateContains().setMateItem(MateItem);
                    mateFragment.getMateContains().setMateTime(MateTime);
                    mateFragment.getMateContains().setMateMode(MateMode);
                    //开启匹配动画
                    mateFragment.getMateAnimation().closeTranslateAnimation();
                    mateFragment.getMateAnimation().startMateAnimantion();
                    //2秒后开始匹配
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //开启匹配
                            MateMethod.StartMate(mateFragment);
                        }
                    },2000);


                }

                break;

        }
    }
}
