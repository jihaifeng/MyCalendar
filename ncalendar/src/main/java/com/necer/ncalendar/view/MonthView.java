package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.necer.ncalendar.R;
import com.necer.ncalendar.listener.OnClickMonthViewListener;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;
import org.joda.time.DateTime;

/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public class MonthView extends CalendarView {

  private int mRowNum;
  private OnClickMonthViewListener mOnClickMonthViewListener;


  public MonthView(Context context, DateTime dateTime, OnClickMonthViewListener onClickMonthViewListener) {
    super(context);
    this.mInitialDateTime = dateTime;


    //0周日，1周一
    Utils.NCalendar nCalendar2 = Utils.getMonthCalendar(dateTime);
    mOnClickMonthViewListener = onClickMonthViewListener;

    dateTimes = nCalendar2.dateTimeList;

    mRowNum = dateTimes.size() / 7  ;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    mWidth = getWidth();
    //绘制高度
    mHeight = getDrawHeight();
    mRectList.clear();
    for (int i = 0; i < mRowNum; i++) {

      for (int j = 0; j < 7; j++) {
        Rect rect = new Rect(j * mWidth / 7, i * mHeight / mRowNum, j * mWidth / 7 + mWidth / 7,
            i * mHeight / mRowNum + mHeight / mRowNum);
        mRectList.add(rect);

        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();

        int baseline;//让6行的第一行和5行的第一行在同一直线上，处理选中第一行的滑动
        if (mRowNum == 5) {
          baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        } else {
          baseline =
              (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2 + (mHeight / 5 - mHeight / 6) / 2;
        }

        //if (i == 0) {
        //  // 绘制文字
        //  mPaint.setColor(mTextColorNormal);
        //  canvas.drawText(days[j], rect.centerX(), baseline, mPaint);
        //} else {

          DateTime dateTime = new DateTime(dateTimes.get(i * 7 + j));
          Log.i("dateTimes" , "dateTimes.get(i)：" + mInitialDateTime);
          //当月和非当月月的颜色不同
          if (Utils.isEqualsMonth(dateTime, mInitialDateTime)) {
            // 当月
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
              canvas.drawText(String.valueOf(dateTime.getDayOfMonth()), rect.centerX(), baseline, mPaint);
              //绘制圆点
              mPaint.setColor(mPointColorSelect);
              drawPoint(canvas, rect, dateTime, baseline);
            } else {
              // 绘制文字
              mPaint.setColor(mTextColorHint);
              canvas.drawText(String.valueOf(dateTime.getDayOfMonth()), rect.centerX(), baseline, mPaint);
              //绘制圆点
              mPaint.setColor(mPointColorNormal);
              drawPoint(canvas, rect, dateTime, baseline);
            }
          }
        //}
      }
    }
  }

  /**
   * 月日历的绘制高度，
   * 为了月日历6行时，绘制农历不至于太靠下，绘制区域网上压缩一下
   *
   * @return
   */
  public int getDrawHeight() {
    return (int) (Attrs.monthCalendarHeight - getResources().getDimension(R.dimen.dp_10));
  }

  //绘制圆点
  public void drawPoint(Canvas canvas, Rect rect, DateTime dateTime, int baseline) {
    if (pointList != null && pointList.contains(dateTime.toLocalDate().toString())) {
      canvas.drawCircle(rect.centerX(), baseline + Attrs.monthCalendarHeight / 30, mPointSize, mPaint);
    }
  }

  private GestureDetector mGestureDetector =
      new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override public boolean onDown(MotionEvent e) {
          return true;
        }

        @Override public boolean onSingleTapUp(MotionEvent e) {
          for (int i = 0; i < mRectList.size()+7; i++) {
            Rect rect = mRectList.get(i);
            Log.i("rect","rect：" + rect + " " + i);
            if (rect.contains((int) e.getX(), (int) e.getY())) {
              DateTime selectDateTime = new DateTime(dateTimes.get(i));
              if (Utils.isLastMonth(selectDateTime, mInitialDateTime)) {
                mOnClickMonthViewListener.onClickLastMonth(selectDateTime);
              } else if (Utils.isNextMonth(selectDateTime, mInitialDateTime)) {
                mOnClickMonthViewListener.onClickNextMonth(selectDateTime);
              } else {
                mOnClickMonthViewListener.onClickCurrentMonth(selectDateTime);
              }
              break;
            }
          }
          return true;
        }
      });

  @Override public boolean onTouchEvent(MotionEvent event) {
    return mGestureDetector.onTouchEvent(event);
  }

  public int getRowNum() {
    return mRowNum;
  }

  public int getSelectRowIndex() {
    if (mSelectDateTime == null) {
      return 0;
    }
    int indexOf = dateTimes.indexOf(mSelectDateTime);
    return indexOf / 7;
  }
}
