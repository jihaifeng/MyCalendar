package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.necer.ncalendar.R;
import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;
import org.joda.time.DateTime;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public class WeekView extends CalendarView {

  private OnClickWeekViewListener mOnClickWeekViewListener;

  public WeekView(Context context, DateTime dateTime, OnClickWeekViewListener onClickWeekViewListener) {
    super(context);

    this.mInitialDateTime = dateTime;
    Utils.NCalendar weekCalendar = Utils.getWeekCalendar(dateTime);

    dateTimes = weekCalendar.dateTimeList;
    mOnClickWeekViewListener = onClickWeekViewListener;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    mWidth = getWidth();
    //mHeight = getHeight();
    //为了与月日历保持一致，往上压缩一下,5倍的关系
    mHeight = (int) (getHeight() - getResources().getDimension(R.dimen.dp_2));
    mRectList.clear();

    for (int i = 0; i < 8; i++) {

      Rect rect = new Rect(i * mWidth / 8, 0, i * mWidth / 8 + mWidth / 8, mHeight);
      mRectList.add(rect);
      Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
      int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;

      if (i == 7) {
        // 绘制文字
        if (null != getSelectDateTime()) {
          mPaint.setColor(mTextColorNormal);
          canvas.drawText(month[getSelectDateTime().monthOfYear().get() - 1], rect.centerX(), baseline, mPaint);
        }
        return;
      }

      DateTime dateTime = new DateTime(dateTimes.get(i));

      //当月和非当月月的颜色不同
      if (Utils.isEqualsMonth(dateTime, mInitialDateTime)) {
        // 当月
        mPaint.setColor(mTextColorToday);
        canvas.drawText(String.valueOf(dateTime.getDayOfMonth()), rect.centerX(), baseline, mPaint);
        //绘制圆点
        // 当天
        if (Utils.isToday(dateTime)) {
          // 绘制文字
          mPaint.setColor(mTextColorToday);
          canvas.drawText(String.valueOf(dateTime.getDayOfMonth()), rect.centerX(), baseline, mPaint);
          //绘制圆点
          mPaint.setColor(mPointColorNormal);
          drawPoint(canvas, rect, dateTime, baseline);
        }
        // 选中的日期
        if (mSelectDateTime != null && dateTime.equals(mSelectDateTime)) {
          // 绘制文字
          mPaint.setColor(mTextColorSelect);
          canvas.drawText(String.valueOf(dateTime.getDayOfMonth()), rect.centerX(), baseline, mPaint);
          //绘制圆点
          mPaint.setColor(mPointColorSelect);
          drawPoint(canvas, rect, dateTime, baseline);
        } else if (!Utils.isToday(dateTime)) {
          // 绘制文字
          mPaint.setColor(mTextColorNormal);
          canvas.drawText(String.valueOf(dateTime.getDayOfMonth()), rect.centerX(), baseline, mPaint);
          //绘制圆点
          mPaint.setColor(mPointColorNormal);
          drawPoint(canvas, rect, dateTime, baseline);
        }
      } else {
        // 非当月

        // 选中的日期
        if (mSelectDateTime != null && dateTime.equals(mSelectDateTime)) {
          // 绘制文字
          mPaint.setColor(mTextColorSelect);
          canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mPaint);
          //绘制圆点
          mPaint.setColor(mPointColorSelect);
          drawPoint(canvas, rect, dateTime, baseline);
        } else {
          // 绘制文字
          mPaint.setColor(mTextColorHint);
          canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX(), baseline, mPaint);
          //绘制圆点
          mPaint.setColor(mPointColorNormal);
          drawPoint(canvas, rect, dateTime, baseline);
        }
      }
    }
  }

  public void drawPoint(Canvas canvas, Rect rect, DateTime dateTime, int baseline) {
    if (pointList != null && pointList.contains(dateTime.toLocalDate().toString())) {
      canvas.drawCircle(rect.centerX(), baseline + Attrs.monthCalendarHeight / 30, mPointSize, mPaint);
    }
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    return mGestureDetector.onTouchEvent(event);
  }

  private GestureDetector mGestureDetector =
      new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override public boolean onDown(MotionEvent e) {
          return true;
        }

        @Override public boolean onSingleTapUp(MotionEvent e) {
          for (int i = 0; i < mRectList.size(); i++) {
            Rect rect = mRectList.get(i);
            if (rect.contains((int) e.getX(), (int) e.getY())) {
              if (i < 7) {
                DateTime selectDateTime = new DateTime(dateTimes.get(i));
                mOnClickWeekViewListener.onClickCurrentWeek(selectDateTime);
              }
              break;
            }
          }
          return true;
        }
      });

  public boolean contains(DateTime dateTime) {
    return dateTimes.contains(dateTime);
  }
}
