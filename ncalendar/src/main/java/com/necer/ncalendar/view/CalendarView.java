package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import com.necer.ncalendar.R;
import com.necer.ncalendar.utils.Attrs;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Created by necer on 2017/8/29.
 * QQ群:127278900
 */

public abstract class CalendarView extends View {

  protected DateTime mSelectDateTime;//被选中的datetime yyyy-MM-dd
  protected DateTime mInitialDateTime;//初始传入的datetime，yyyy-MM-dd
  protected int mWidth;
  protected int mHeight;
  protected List<DateTime> dateTimes;

  protected Paint mPaint; // 画笔

  protected int mTextColorHint = Color.parseColor("#FFB9BFC8");//不是当月的颜色
  protected int mTextColorNormal = Color.parseColor("#333333"); // 当月日期颜色
  protected int mTextColorSelect = Color.parseColor("#FF14CFB2"); // 选中的日期文字颜色
  protected int mTextColorToday = Color.parseColor("#E77069"); // 当天日期未选中颜色
  protected float mTextSize = getResources().getDimension(R.dimen.sp_15); // 文字大小

  protected int mPointColorNormal = Color.parseColor("#FFB9BFC8");// 未选中日期的圆点颜色
  protected int mPointColorSelect = Color.parseColor("#FF14CFB2"); // 选中日期的圆点颜色
  protected float mPointSize = getResources().getDimension(R.dimen.sp_3);//圆点大小

  protected List<Rect> mRectList;//点击用的矩形集合

  protected List<String> pointList;

  protected String[] days = new String[] { "日", "一", "二", "三", "四", "五", "六" };
  protected String[] month =
      new String[] { "一 月", "二 月", "三 月", "四 月", "五 月", "六 月", "七 月", "八 月", "九 月", "十 月", "十一 月", "十二 月" };

  public CalendarView(Context context) {
    super(context);

    mTextColorHint = Attrs.mTextColorHint;//不是当月的颜色
    mTextColorNormal = Attrs.mTextColorNormal; // 当月日期颜色
    mTextColorSelect = Attrs.mTextColorSelect; // 选中的日期文字颜色
    mTextColorToday = Attrs.mTextColorToday; // 当天日期未选中颜色
    mTextSize = Attrs.mTextSize; // 文字大小
    mPointColorNormal = Attrs.mPointColorNormal;// 未选中日期的圆点颜色
    mPointColorSelect = Attrs.mPointColorSelect; // 选中日期的圆点颜色
    mPointSize = Attrs.mPointSize;//圆点大小

    mRectList = new ArrayList<>();

    mPaint = new Paint();
    mPaint.setTextSize(mTextSize);
    mPaint.setAntiAlias(true);
    mPaint.setTextAlign(Paint.Align.CENTER);
  }

  public DateTime getInitialDateTime() {
    return mInitialDateTime;
  }

  public DateTime getSelectDateTime() {
    return mSelectDateTime;
  }

  public void setSelectDateTime(DateTime dateTime) {
    this.mSelectDateTime = dateTime;
    invalidate();
  }

  public void setDateTimeAndPoint(DateTime dateTime, List<String> pointList) {
    this.mSelectDateTime = dateTime;
    this.pointList = pointList;
    invalidate();
  }

  public void clear() {
    this.mSelectDateTime = null;
    invalidate();
  }

  public void setPointList(List<String> pointList) {
    this.pointList = pointList;
    invalidate();
  }
}
