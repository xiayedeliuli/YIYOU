package cn.bmob.imdemo.YiYou.MyTimeDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;

import java.util.Calendar;

public class DateTimePickerDialog extends AlertDialog implements
        DialogInterface.OnClickListener {
    private DateTimePicker mDateTimePicker;
    private Calendar mDate = Calendar.getInstance();
    private OnDateTimeSetListener mOnDateTimeSetListener;
    @SuppressWarnings("deprecation")
    public DateTimePickerDialog(Context context, long date) {
        super(context);
        mDateTimePicker = new DateTimePicker(context);
        setView(mDateTimePicker);
		/*
		 *实现接口，实现里面的方法
		 */
        mDateTimePicker
                .setOnDateTimeChangedListener(new DateTimePicker.OnDateTimeChangedListener() {
                    @Override
                    public void onDateTimeChanged(DateTimePicker view,
                                                  int year, int month, int day, int hour, int minute) {
                        mDate.set(Calendar.YEAR, year);
                        mDate.set(Calendar.MONTH, month);
                        mDate.set(Calendar.DAY_OF_MONTH, day);
                        mDate.set(Calendar.HOUR_OF_DAY, hour);
                        mDate.set(Calendar.MINUTE, minute);
                        mDate.set(Calendar.SECOND, 0);
                        /**
                         * 更新日期
                         */
                        updateTitle(mDate.getTimeInMillis());
                    }
                });



        setButton(DialogInterface.BUTTON_NEGATIVE,"取消", (OnClickListener) null);
        setButton(DialogInterface.BUTTON_POSITIVE,"确认", this);
        mDate.setTimeInMillis(date);
        updateTitle(mDate.getTimeInMillis());
    }
    /*
     *接口回調
     *控件 秒数
     */
    public interface OnDateTimeSetListener {
        void OnDateTimeSet(AlertDialog dialog, long date);
    }
    /**
     * 更新对话框日期
     * @param date
     */
    private void updateTitle(final long date) {
//                BmobQuery<MyUser> query=new BmobQuery<>();
//                query.getObject(MyApplication.ID, new QueryListener<MyUser>() {
//                    @Override
//                    public void done(MyUser myUser, BmobException e) {
//                        if(e==null){
//                            MyApplication.newdate=myUser.getDate();
//
//                        }
//                    }
//                });
//                final int flagg = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME;
//                setTitle(  "匹配伙伴时间："+ DateUtils.formatDateTime(getContext(), MyApplication.newdate, flagg));
            int flag = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_TIME;
            setTitle(DateUtils.formatDateTime(this.getContext(), date, flag));
    }


    /*
     * 对外公开方法让Activity实现
     */
    public void setOnDateTimeSetListener(OnDateTimeSetListener callBack) {
        mOnDateTimeSetListener = callBack;
    }

    public void onClick(DialogInterface arg0, int arg1) {
        if (mOnDateTimeSetListener != null) {
            mOnDateTimeSetListener.OnDateTimeSet(this, mDate.getTimeInMillis());
        }
    }
}