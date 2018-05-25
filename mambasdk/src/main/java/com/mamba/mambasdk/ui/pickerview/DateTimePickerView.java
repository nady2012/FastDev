package com.mamba.mambasdk.ui.pickerview;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mamba.mambasdk.R;
import com.mamba.mambasdk.ui.pickerview.lib.WheelView;
import com.mamba.mambasdk.ui.pickerview.listener.CustomListener;
import com.mamba.mambasdk.ui.pickerview.view.BasePickerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
 * 时间选择器
 * Created by Sai on 15/11/22.
 * Updated by XiaoSong on 2017-2-22.
 */
public class DateTimePickerView extends BasePickerView implements View.OnClickListener {
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private WheelDateTime wheelTime; //自定义控件
    private int layoutRes;
    private CustomListener customListener;
    private OnTimeSelectListener timeSelectListener;//回调接口
    private int gravity = Gravity.CENTER;//内容显示位置 默认居中
    private boolean[] type;// 显示类型
    private String strSubmit;//确定按钮字符串
    private String strCancel;//取消按钮字符串
    private String strTitle;//标题字符串
    private int colorSubmit;//确定按钮颜色
    private int colorCancel;//取消按钮颜色
    private int colorTitle;//标题颜色
    private int colorBackgroundWheel;//滚轮背景颜色
    private int colorBackgroundTitle;//标题背景颜色
    private int sizeSubmitCancel;//确定取消按钮大小
    private int sizeTitle;//标题字体大小
    private int sizeContent;//内容字体大小
    private CustomDate date;//当前选中时间
    private CustomDate startDate;//开始时间
    private CustomDate endDate;//终止时间
    private boolean cyclic;//是否循环
    private boolean cancelable;//是否能取消
    private boolean isCenterLabel;//是否只显示中间的label
    private int textColorOut; //分割线以外的文字颜色
    private int textColorCenter; //分割线之间的文字颜色
    private int dividerColor; //分割线的颜色
    private int backgroundId; //显示时的外部背景色颜色,默认是灰色
    // 条目间距倍数 默认1.6
    private float lineSpacingMultiplier = 1.6F;
    private boolean isDialog;//是否是对话框模式
    private String labelHours;
    private String labelMins;
    private String labelSeconds;
    private WheelView.DividerType dividerType;//分隔线类型

    //构造方法
    private DateTimePickerView(Builder builder) {
        super(builder.context);
        this.timeSelectListener = builder.timeSelectListener;
        this.gravity = builder.gravity;
        this.type = builder.type;
        this.strSubmit = builder.strSubmit;
        this.strCancel = builder.strCancel;
        this.strTitle = builder.strTitle;
        this.colorSubmit = builder.colorSubmit;
        this.colorCancel = builder.colorCancel;
        this.colorTitle = builder.colorTitle;
        this.colorBackgroundWheel = builder.colorBackgroundWheel;
        this.colorBackgroundTitle = builder.colorBackgroundTitle;
        this.sizeSubmitCancel = builder.sizeSubmitCancel;
        this.sizeTitle = builder.sizeTitle;
        this.sizeContent = builder.sizeContent;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.date = builder.date;
        this.cyclic = builder.cyclic;
        this.isCenterLabel = builder.isCenterLabel;
        this.cancelable = builder.cancelable;
        this.labelHours = builder.labelHours;
        this.labelMins = builder.labelMins;
        this.labelSeconds = builder.labelSeconds;
        this.textColorCenter = builder.textColorCenter;
        this.textColorOut = builder.textColorOut;
        this.dividerColor = builder.dividerColor;
        this.customListener = builder.customListener;
        this.layoutRes = builder.layoutRes;
        this.lineSpacingMultiplier = builder.lineSpacingMultiplier;
        this.isDialog = builder.isDialog;
        this.dividerType = builder.dividerType;
        this.backgroundId = builder.backgroundId;
        this.decorView = builder.decorView;
        initView(builder.context);
    }

    private void initView(Context context) {
        setDialogOutSideCancelable(cancelable);
        initViews(backgroundId);
        init();
        initEvents();
        if (customListener == null) {
            LayoutInflater.from(context).inflate(R.layout.pickerview_date_time, contentContainer);

            //顶部标题
            TextView tvTitle = (TextView) findViewById(R.id.tvTitle);

            //确定和取消按钮
            Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
            Button btnCancel = (Button) findViewById(R.id.btnCancel);

            btnSubmit.setTag(TAG_SUBMIT);
            btnCancel.setTag(TAG_CANCEL);

            btnSubmit.setOnClickListener(this);
            btnCancel.setOnClickListener(this);

            //设置文字
            btnSubmit.setText(TextUtils.isEmpty(strSubmit) ? context.getResources()
                .getString(R.string.pickerview_submit) : strSubmit);
            btnCancel.setText(TextUtils.isEmpty(strCancel) ? context.getResources()
                .getString(R.string.pickerview_cancel) : strCancel);
            tvTitle.setText(TextUtils.isEmpty(strTitle) ? "" : strTitle);//默认为空

            //设置文字颜色
            btnSubmit.setTextColor(colorSubmit == 0 ? pickerviewTimebtnNor : colorSubmit);
            btnCancel.setTextColor(colorCancel == 0 ? pickerviewTimebtnNor : colorCancel);
            tvTitle.setTextColor(colorTitle == 0 ? pickerviewTopbarTitle : colorTitle);

            //设置文字大小
            btnSubmit.setTextSize(sizeSubmitCancel);
            btnCancel.setTextSize(sizeSubmitCancel);
            tvTitle.setTextSize(sizeTitle);
            RelativeLayout rvTopBar = (RelativeLayout) findViewById(
                R.id.rv_topbar);
            rvTopBar.setBackgroundColor(
                colorBackgroundTitle == 0 ? pickerviewBgTopbar : colorBackgroundTitle);
        } else {
            customListener.customLayout(
                LayoutInflater.from(context).inflate(layoutRes, contentContainer));
        }
        // 时间转轮 自定义控件
        LinearLayout timePickerView = (LinearLayout) findViewById(
            R.id.timepicker);

        timePickerView.setBackgroundColor(
            colorBackgroundWheel == 0 ? bgColorDefault : colorBackgroundWheel);

        wheelTime = new WheelDateTime(timePickerView, type, gravity, sizeContent);

        if (startDate != null && endDate != null) {
            if (startDate.getDate().before(endDate.getDate())) {
                setRangDate();
            }
        } else if (startDate != null && endDate == null) {
            setRangDate();
        } else if (startDate == null && endDate != null) {
            setRangDate();
        }

        setTime();
        wheelTime.setLabels(labelHours, labelMins, labelSeconds);

        setOutSideCancelable(cancelable);
        wheelTime.setCyclic(cyclic);
        wheelTime.setDividerColor(dividerColor);
        wheelTime.setDividerType(dividerType);
        wheelTime.setLineSpacingMultiplier(lineSpacingMultiplier);
        wheelTime.setTextColorOut(textColorOut);
        wheelTime.setTextColorCenter(textColorCenter);
        wheelTime.isCenterLabel(isCenterLabel);
    }

    /**
     * 设置默认时间
     * @param date the date
    */
    public void setDate(CustomDate date) {
        this.date = date;
        setTime();
    }

    /**
     * 设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private void setRangDate() {
        wheelTime.setRangDate(startDate, endDate);
        //如果设置了时间范围
        if (startDate != null && endDate != null) {
            //判断一下默认时间是否设置了，或者是否在起始终止时间范围内
            if (date == null || date.getDate().before(startDate.getDate())
                || date.getDate().after(endDate.getDate())) {
                date = startDate;
            }
        } else if (startDate != null) {
            //没有设置默认选中时间,那就拿开始时间当默认时间
            date = startDate;
        } else if (endDate != null) {
            date = endDate;
        }
    }

    /**
    * Sets rang date.
    *
    * @param startDate the start date
    * @param endDate the end date
    */
    public void setRangDate(CustomDate startDate, CustomDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        setRangDate();
        setTime();
    }

    /**
     * 设置选中时间,默认选中当前时间
     */
    private void setTime() {
//        int dayOfYear;
        int hours;
        int minute;
        int seconds;

        CustomDate customDate = new CustomDate(new Date(System.currentTimeMillis()));
        Calendar calendar1 = Calendar.getInstance();
        if (date == null) {

            calendar1.setTime(customDate.getDate());
        } else {
            calendar1.setTime(date.getDate());

        }
        hours = calendar1.get(Calendar.HOUR_OF_DAY);
        minute = calendar1.get(Calendar.MINUTE);
        seconds = calendar1.get(Calendar.SECOND);

        wheelTime.setPicker(customDate, hours, minute, seconds);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_SUBMIT)) {
            returnData();
        }
        dismiss();
    }

    /**
    * Gets current time.
    *
    * @return the current time
    */
    public Date getCurrentTime() {
        try {
            return dateFormat.parse(wheelTime.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * Return data.
    */
    public void returnData() {
        if (timeSelectListener != null) {
            try {
                Date date = dateFormat.parse(wheelTime.getTime());
                timeSelectListener.onTimeSelect(date, clickView);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isDialog() {
        return isDialog;
    }

    /**
    * The interface On time select listener.
    */
    public interface OnTimeSelectListener {
        /**
        * On time select.
        *
        * @param date the date
        * @param v the v
        */
        void onTimeSelect(Date date, View v);
    }


    /**
    * The type Builder.
    */
    //建造器
    public static class Builder {
        /**
        * The Decor view.
        */
        public ViewGroup decorView;//显示pickerview的根View,默认是activity的根view
        private int layoutRes = R.layout.pickerview_date_time;
        private CustomListener customListener;
        private Context context;
        private OnTimeSelectListener timeSelectListener;
        private boolean[] type = new boolean[]{true, true, true, true};//显示类型 默认全部显示
        private int gravity = Gravity.CENTER;//内容显示位置 默认居中
        private String strSubmit;//确定按钮文字
        private String strCancel;//取消按钮文字
        private String strTitle;//标题文字
        private int colorSubmit;//确定按钮颜色
        private int colorCancel;//取消按钮颜色
        private int colorTitle;//标题颜色
        private int colorBackgroundWheel;//滚轮背景颜色
        private int colorBackgroundTitle;//标题背景颜色
        private int sizeSubmitCancel = 17;//确定取消按钮大小
        private int sizeTitle = 18;//标题字体大小
        private int sizeContent = 18;//内容字体大小
        private CustomDate date;//当前选中时间
        private CustomDate startDate;//开始时间
        private CustomDate endDate;//终止时间
        private boolean cyclic = false;//是否循环
        private boolean cancelable = true;//是否能取消
        private boolean isCenterLabel = true;//是否只显示中间的label
        private int textColorOut; //分割线以外的文字颜色
        private int textColorCenter; //分割线之间的文字颜色
        private int dividerColor; //分割线的颜色
        private int backgroundId; //显示时的外部背景色颜色,默认是灰色
        private WheelView.DividerType dividerType;//分隔线类型
        // 条目间距倍数 默认1.6
        private float lineSpacingMultiplier = 1.6F;

        private boolean isDialog;//是否是对话框模式

        private String labelHours;//单位
        private String labelMins;//单位
        private String labelSeconds;//单位

        /**
        * Instantiates a new Builder.
        *
        * @param context the context
        * @param listener the listener
        */
        //Required
        public Builder(Context context, OnTimeSelectListener listener) {
            this.context = context;
            this.timeSelectListener = listener;
        }

        /**
        * Sets type.
        *
        * @param type the type
        * @return the type
        */
        //Option
        public Builder setType(boolean[] type) {
            this.type = type;
            return this;
        }

        /**
        * Gravity builder.
        *
        * @param gravity the gravity
        * @return  builder
        */
        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
        * Sets submit text.
        *
        * @param strSubmit the str submit
        * @return the submit text
        */
        public Builder setSubmitText(String strSubmit) {
            this.strSubmit = strSubmit;
            return this;
        }

        /**
        * Is dialog builder.
        *
        * @param isDialog the is dialog
        * @return the builder
        */
        public Builder isDialog(boolean isDialog) {
            this.isDialog = isDialog;
            return this;
        }

        /**
        * Sets cancel text.
        *
        * @param strCancel the str cancel
        * @return the cancel text
        */
        public Builder setCancelText(String strCancel) {
            this.strCancel = strCancel;
            return this;
        }

        /**
        * Sets title text.
        *
        * @param strTitle the str title
        * @return the title text
        */
        public Builder setTitleText(String strTitle) {
            this.strTitle = strTitle;
            return this;
        }

        /**
        * Sets submit color.
        *
        * @param colorSubmit the color submit
        * @return the submit color
        */
        public Builder setSubmitColor(int colorSubmit) {
            this.colorSubmit = colorSubmit;
            return this;
        }

        /**
        * Sets cancel color.
        *
        * @param colorCancel the color cancel
        * @return the cancel color
        */
        public Builder setCancelColor(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        /**
         * 必须是viewgroup
         * 设置要将pickerview显示到的容器id
         *
         * @param decorView the decor view
        * @return  decor view
        */
        public Builder setDecorView(ViewGroup decorView) {
            this.decorView = decorView;
            return this;
        }

        /**
        * Sets bg color.
        *
        * @param colorBackgroundWheel the color background wheel
        * @return the bg color
        */
        public Builder setBgColor(int colorBackgroundWheel) {
            this.colorBackgroundWheel = colorBackgroundWheel;
            return this;
        }

        /**
        * Sets title bg color.
        *
        * @param colorBackgroundTitle the color background title
        * @return the title bg color
        */
        public Builder setTitleBgColor(int colorBackgroundTitle) {
            this.colorBackgroundTitle = colorBackgroundTitle;
            return this;
        }

        /**
        * Sets title color.
        *
        * @param colorTitle the color title
        * @return the title color
        */
        public Builder setTitleColor(int colorTitle) {
            this.colorTitle = colorTitle;
            return this;
        }

        /**
        * Sets sub cal size.
        *
        * @param sizeSubmitCancel the size submit cancel
        * @return the sub cal size
        */
        public Builder setSubCalSize(int sizeSubmitCancel) {
            this.sizeSubmitCancel = sizeSubmitCancel;
            return this;
        }

        /**
        * Sets title size.
        *
        * @param sizeTitle the size title
        * @return the title size
        */
        public Builder setTitleSize(int sizeTitle) {
            this.sizeTitle = sizeTitle;
            return this;
        }

        /**
        * Sets content size.
        *
        * @param sizeContent the size content
        * @return the content size
        */
        public Builder setContentSize(int sizeContent) {
            this.sizeContent = sizeContent;
            return this;
        }

        /**
         * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         *
         * @param date the date
        * @return  date
        */
        public Builder setDate(CustomDate date) {
            this.date = date;
            return this;
        }

        /**
        * Sets layout res.
        *
        * @param res the res
        * @param customListener the custom listener
        * @return the layout res
        */
        public Builder setLayoutRes(int res, CustomListener customListener) {
            this.layoutRes = res;
            this.customListener = customListener;
            return this;
        }

//        public Builder setRange(int startYear, int endYear) {
//            this.startYear = startYear;
//            this.endYear = endYear;
//            return this;
//        }

        /**
         * 设置起始时间
         * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         *
         * @param startDate the start date
        * @param endDate the end date
        * @return  rang date
        */
        public Builder setRangDate(CustomDate startDate, CustomDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
            return this;
        }

        /**
         * 设置间距倍数,但是只能在1.2-2.0f之间
         *
         * @param lineSpacingMultiplier the line spacing multiplier
        * @return the line spacing multiplier
        */
        public Builder setLineSpacingMultiplier(float lineSpacingMultiplier) {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
            return this;
        }

        /**
         * 设置分割线的颜色
         *
         * @param dividerColor the divider color
        * @return the divider color
        */
        public Builder setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        /**
         * 设置分割线的类型
         *
         * @param dividerType the divider type
        * @return the divider type
        */
        public Builder setDividerType(WheelView.DividerType dividerType) {
            this.dividerType = dividerType;
            return this;
        }

        /**
         * //显示时的外部背景色颜色,默认是灰色
         *
         * @param backgroundId the background id
        * @return the background id
        */
        public Builder setBackgroundId(int backgroundId) {
            this.backgroundId = backgroundId;
            return this;
        }

        /**
         * 设置分割线之间的文字的颜色
         *
         * @param textColorCenter the text color center
        * @return the text color center
        */
        public Builder setTextColorCenter(int textColorCenter) {
            this.textColorCenter = textColorCenter;
            return this;
        }

        /**
         * 设置分割线以外文字的颜色
         *
         * @param textColorOut the text color out
        * @return the text color out
        */
        public Builder setTextColorOut(int textColorOut) {
            this.textColorOut = textColorOut;
            return this;
        }

        /**
        * Is cyclic builder.
        *
        * @param cyclic the cyclic
        * @return the builder
        */
        public Builder isCyclic(boolean cyclic) {
            this.cyclic = cyclic;
            return this;
        }

        /**
        * Sets out side cancelable.
        *
        * @param cancelable the cancelable
        * @return the out side cancelable
        */
        public Builder setOutSideCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
        * Sets label.
        *
        * @param labelHours the label hours
        * @param labelMins the label mins
        * @param labelSeconds the label seconds
        * @return the label
        */
        public Builder setLabel(String labelHours, String labelMins, String labelSeconds) {
            this.labelHours = labelHours;
            this.labelMins = labelMins;
            this.labelSeconds = labelSeconds;
            return this;
        }

        /**
        * Is center label builder.
        *
        * @param isCenterLabel the is center label
        * @return the builder
        */
        public Builder isCenterLabel(boolean isCenterLabel) {
            this.isCenterLabel = isCenterLabel;
            return this;
        }

        /**
        * Build date time picker view.
        *
        * @return  date time picker view
        */
        public DateTimePickerView build() {
            return new DateTimePickerView(this);
        }
    }
}
