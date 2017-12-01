package com.necer.ncalendar.utils;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Weeks;

/**
 * Created by necer on 2017/6/9.
 */

public class Utils {

  //是否同月
  public static boolean isEqualsMonth(DateTime dateTime1, DateTime dateTime2) {
    return dateTime1.getYear() == dateTime2.getYear() && dateTime1.getMonthOfYear() == dateTime2.getMonthOfYear();
  }

  /**
   * 第一个是不是第二个的上一个月,只在此处有效
   *
   * @param dateTime1
   * @param dateTime2
   *
   * @return
   */
  public static boolean isLastMonth(DateTime dateTime1, DateTime dateTime2) {
    DateTime dateTime = dateTime2.plusMonths(-1);
    return dateTime1.getMonthOfYear() == dateTime.getMonthOfYear();
  }

  /**
   * 第一个是不是第二个的下一个月，只在此处有效
   *
   * @param dateTime1
   * @param dateTime2
   *
   * @return
   */
  public static boolean isNextMonth(DateTime dateTime1, DateTime dateTime2) {
    DateTime dateTime = dateTime2.plusMonths(1);
    return dateTime1.getMonthOfYear() == dateTime.getMonthOfYear();
  }

  /**
   * 获得两个日期距离几个月
   *
   * @return
   */
  public static int getIntervalMonths(DateTime dateTime1, DateTime dateTime2) {
    dateTime1 = dateTime1.withDayOfMonth(1).withTimeAtStartOfDay();
    dateTime2 = dateTime2.withDayOfMonth(1).withTimeAtStartOfDay();

    return Months.monthsBetween(dateTime1, dateTime2).getMonths();
  }

  /**
   * 获得两个日期距离几周
   *
   * @param dateTime1
   * @param dateTime2
   *
   * @return
   */
  public static int getIntervalWeek(DateTime dateTime1, DateTime dateTime2) {

    dateTime1 = getSunFirstDayOfWeek(dateTime1);
    dateTime2 = getSunFirstDayOfWeek(dateTime2);

    return Weeks.weeksBetween(dateTime1, dateTime2).getWeeks();
  }

  /**
   * 是否是今天
   *
   * @param dateTime
   *
   * @return
   */
  public static boolean isToday(DateTime dateTime) {
    Log.i("dateTimes", "new DateTime()：" + new DateTime().withTimeAtStartOfDay());
    Log.i("dateTimes", "dateTime：" + dateTime);
    return new DateTime().withTimeAtStartOfDay().equals(dateTime);
  }

  /**
   * @param dateTime 今天
   *
   * @return
   */
  public static NCalendar getMonthCalendar(DateTime dateTime) {

    DateTime lastMonthDateTime = dateTime.plusMonths(-1);//上个月
    DateTime nextMonthDateTime = dateTime.plusMonths(1);//下个月

    int days = dateTime.dayOfMonth().getMaximumValue();//当月天数
    int lastMonthDays = lastMonthDateTime.dayOfMonth().getMaximumValue();//上个月的天数

    int firstDayOfWeek = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), 1, 0, 0, 0).getDayOfWeek();
    //当月第一天周几

    int endDayOfWeek = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), days, 0, 0, 0).getDayOfWeek();
    //当月最后一天周几

    NCalendar nCalendar = new NCalendar();
    List<DateTime> dateTimes = new ArrayList<>();

    //周日开始的
    //上个月
    if (firstDayOfWeek != 7) {
      for (int i = 0; i < firstDayOfWeek; i++) {
        DateTime dateTime1 = new DateTime(lastMonthDateTime.getYear(), lastMonthDateTime.getMonthOfYear(),
            lastMonthDays - (firstDayOfWeek - i - 1),0,0,0);
        dateTimes.add(dateTime1);
      }
    }
    //当月
    for (int i = 0; i < days; i++) {
      DateTime dateTime1 = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), i + 1,0,0,0);
      dateTimes.add(dateTime1);
    }
    //下个月
    if (endDayOfWeek == 7) {
      endDayOfWeek = 0;
    }
    for (int i = 0; i < 6 - endDayOfWeek; i++) {
      DateTime dateTime1 = new DateTime(nextMonthDateTime.getYear(), nextMonthDateTime.getMonthOfYear(), i + 1,0,
          0,0);
      dateTimes.add(dateTime1);
    }

    nCalendar.dateTimeList = dateTimes;
    return nCalendar;
  }

  /**
   * 周视图的数据
   *
   * @param dateTime 日期
   *
   * @return
   */
  public static NCalendar getWeekCalendar(DateTime dateTime) {
    List<DateTime> dateTimeList = new ArrayList<>();
    List<DateTime> localDateList = new ArrayList<>();
    dateTime = getSunFirstDayOfWeek(dateTime);
    NCalendar calendar = new NCalendar();
    for (int i = 0; i < 7; i++) {
      DateTime dateTime1 = dateTime.plusDays(i);

      dateTimeList.add(dateTime1);
      localDateList.add(dateTime1);
    }
    calendar.dateTimeList = dateTimeList;
    return calendar;
  }

  //转化一周从周日开始
  public static DateTime getSunFirstDayOfWeek(DateTime dateTime) {
    if (dateTime.dayOfWeek().get() == 7) {
      return dateTime;
    } else {
      return dateTime.minusWeeks(1).withDayOfWeek(7);
    }
  }

  //包含农历,公历,格式化的日期
  public static class NCalendar {
    public List<DateTime> dateTimeList;
  }
}
