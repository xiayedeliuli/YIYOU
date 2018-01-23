package cn.bmob.imdemo.YiYou.MyTimeDialog;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import java.util.Calendar;

import cn.bmob.imdemo.R;

/**
 * Created by DELL on 2017/7/6.
 */

public class DateTimePicker extends FrameLayout {
    private final NumberPicker mDateSpinner;
    private final NumberPicker mHourSpinner;
    private final NumberPicker mMinuteSpinner;
    private Calendar mDate;
    private int mHour, mMinute;
    private String[] mDateDisplayValues = new String[7];
    private String[] mDateDisplayValues2 = new String[7];
    private OnDateTimeChangedListener mOnDateTimeChangedListener;

    public DateTimePicker(Context context) {
        super(context);
		/*
		 *獲取系統時間
		 */
        mDate = Calendar.getInstance();
        mHour = mDate.get(Calendar.HOUR_OF_DAY);
        mMinute = mDate.get(Calendar.MINUTE);
        /**
         * 加载布局
         */
        inflate(context, R.layout.datadialog, this);
        /**
         * 初始化控件
         */
        mDateSpinner = (NumberPicker) this.findViewById(R.id.np_date);
        mDateSpinner.setMinValue(0);
        mDateSpinner.setMaxValue(6);
        updateDateControl();
        mDateSpinner.setOnValueChangedListener(mOnDateChangedListener);

        mHourSpinner = (NumberPicker) this.findViewById(R.id.np_hour);
        mHourSpinner.setMaxValue(23);
        mHourSpinner.setMinValue(0);
        mHourSpinner.setValue(mHour);
        mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);

        mMinuteSpinner = (NumberPicker) this.findViewById(R.id.np_minute);
        mMinuteSpinner.setMaxValue(59);
        mMinuteSpinner.setMinValue(0);
        mMinuteSpinner.setValue(mMinute);
        mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener);
    }
    /**
     *
     * 控件监听器
     */
    private NumberPicker.OnValueChangeListener mOnDateChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            /**
             * 给接口传值
             */
            onDateTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnHourChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            mHour = mHourSpinner.getValue();
            onDateTimeChanged();
        }
    } ;


    private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            mMinute = mMinuteSpinner.getValue();
            onDateTimeChanged();
        }
    };

    private void updateDateControl() {
        /**
         * 星期几算法
         */
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mDate.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, -1 / 2 - 1);
        mDateSpinner.setDisplayedValues(null);
        for (int i = 0; i < 7; ++i) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            mDateDisplayValues[i] = (String) DateFormat.format("MM.dd EEEE",
                    cal);
            mDateDisplayValues2[i] = (String) DateFormat.format("MM.dd",
                    cal);
        }
        mDateSpinner.setDisplayedValues(mDateDisplayValues);
        mDateSpinner.setValue(0);
        mDateSpinner.invalidate();
    }


    /*
     *接口回调 参数是当前的View 年月日小时分钟
     */
    public interface OnDateTimeChangedListener {
        void onDateTimeChanged(DateTimePicker view, int year, int month,
                               int day, int hour, int minute);
    }
    /*
     *对外的公开方法
     */
    public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) {
        mOnDateTimeChangedListener = callback;
    }

    private void onDateTimeChanged() {
        if (mOnDateTimeChangedListener != null) {
            String day = mDateDisplayValues2[mDateSpinner.getValue()].substring(3);
            int dayint=Integer.parseInt(day);
            mOnDateTimeChangedListener.onDateTimeChanged(this,mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH),
                    dayint, mHour, mMinute);
        }
    }


}

