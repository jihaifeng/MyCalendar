package com.necer.ncalendar.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import com.necer.ncalendar.R;
import com.necer.ncalendar.adapter.CalendarAdapter;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.view.CalendarView;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public abstract class CalendarPager extends ViewPager {

  protected CalendarAdapter calendarAdapter;
  protected DateTime startDateTime;
  protected DateTime endDateTime;
  protected int mPageSize;
  protected int mCurrPage;
  protected DateTime mInitialDateTime;//日历初始化datetime，即今天
  protected DateTime mSelectDateTime;//当前页面选中的datetime
  protected List<String> pointList;//圆点

  protected boolean isPagerChanged = true;//是否是手动翻页
  protected DateTime lastSelectDateTime;//上次选中的datetime
  protected boolean isDefaultSelect = true;//是否默认选中

  private OnPageChangeListener onPageChangeListener;

  public CalendarPager(Context context) {
    this(context, null);
  }

  public CalendarPager(Context context, AttributeSet attrs) {
    super(context, attrs);

    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomCalendar);
    // 非当月日期颜色
    Attrs.mTextColorHint = ta.getColor(R.styleable.CustomCalendar_mTextColorHint, Color.parseColor("#FFB9BFC8"));
    // 当月日期未选中颜色
    Attrs.mTextColorNormal = ta.getColor(R.styleable.CustomCalendar_mTextColorNormal, Color.parseColor("#333333"));
    // 选中的日期文字颜色
    Attrs.mTextColorSelect = ta.getColor(R.styleable.CustomCalendar_mTextColorSelect, Color.parseColor("#FF14CFB2"));
    // 当天日期未选中颜色
    Attrs.mTextColorToday = ta.getColor(R.styleable.CustomCalendar_mTextColorToday, Color.parseColor("#E77069"));
    // 日期文字大小
    Attrs.mTextSize = ta.getDimension(R.styleable.CustomCalendar_mTextSize, getResources().getDimension(R.dimen.sp_15));
    // 未选中日期的圆点颜色
    Attrs.mPointColorNormal = ta.getColor(R.styleable.CustomCalendar_mPointColorNormal, Color.parseColor("#FFB9BFC8"));
    // 选中日期的圆点颜色
    Attrs.mPointColorSelect = ta.getColor(R.styleable.CustomCalendar_mPointColorSelect, Color.parseColor("#FF14CFB2"));
    // 圆点大小
    Attrs.mPointSize =
        ta.getDimension(R.styleable.CustomCalendar_mPointSize, getResources().getDimension(R.dimen.sp_3));
    // 月历视图高度
    Attrs.monthCalendarHeight =
        ta.getInt(R.styleable.CustomCalendar_calendarHeight, (int) getResources().getDimension(R.dimen.dp_260));
    // 日历背景颜色
    Attrs.backgroundColor = ta.getColor(R.styleable.CustomCalendar_backgroundColor, Color.WHITE);

    ta.recycle();

    mInitialDateTime = new DateTime().withTimeAtStartOfDay();

    startDateTime = new DateTime("1901-01-01");
    endDateTime = new DateTime("2099-12-31");

    setDateInterval(null, null);

    getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override public void onGlobalLayout() {
        initCurrentCalendarView(mCurrPage);
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
      }
    });

    setBackgroundColor(Attrs.backgroundColor);
  }

  /**
   * 设置起止日期
   *
   * @param startString 开始日期 yyyy-MM-dd
   * @param endString 结束日期 yyyy-MM-dd
   */
  public void setDateInterval(String startString, String endString) {
    if (startString != null && !"".equals(startString)) {
      startDateTime = new DateTime(startString);
    }
    if (endString != null && !"".equals(endString)) {
      endDateTime = new DateTime(endString);
    }

    if (mInitialDateTime.getMillis() < startDateTime.getMillis()
        || mInitialDateTime.getMillis() > endDateTime.getMillis()) {
      throw new RuntimeException(getResources().getString(R.string.range_date));
    }

    calendarAdapter = getCalendarAdapter();
    setAdapter(calendarAdapter);
    setCurrentItem(mCurrPage);

    if (onPageChangeListener != null) {
      removeOnPageChangeListener(onPageChangeListener);
    }

    onPageChangeListener = new OnPageChangeListener() {
      @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        initCurrentCalendarView(position);
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    };

    addOnPageChangeListener(onPageChangeListener);
  }

  protected abstract CalendarAdapter getCalendarAdapter();

  protected abstract void initCurrentCalendarView(int position);

  protected abstract void setDateTime(DateTime dateTime);

  public void toToday() {
    setDateTime(new DateTime().withTimeAtStartOfDay());
  }

  /**
   * 下一页，月日历即是下一月，周日历即是下一周
   */
  public void toNextPager() {
    setCurrentItem(getCurrentItem() + 1, true);
  }

  /**
   * 上一页
   */
  public void toLastPager() {
    setCurrentItem(getCurrentItem() - 1, true);
  }

  //设置日期
  public void setDate(String formatDate) {
    setDateTime(new DateTime(formatDate));
  }

  public void setPointList(List<String> pointList) {

    List<String> formatList = new ArrayList<>();
    for (int i = 0; i < pointList.size(); i++) {
      String format = new DateTime(pointList.get(i)).toString("yyyy-MM-dd");
      formatList.add(format);
    }

    this.pointList = formatList;
    CalendarView calendarView = calendarAdapter.getCalendarViews().get(getCurrentItem());
    if (calendarView == null) {
      return;
    }
    calendarView.setPointList(formatList);
  }

  public void setDefaultSelect(boolean defaultSelect) {
    isDefaultSelect = defaultSelect;
  }
}
