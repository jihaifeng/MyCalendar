//package com.necer.ncalendar;
//
//import android.util.Log;
//import java.text.ParsePosition;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.Locale;
//import java.util.TimeZone;
//
///**
// * Func：
// * Desc:
// * Author：JHF
// * Date：2017-12-01-0001 23:07
// * Mail：jihaifeng@meechao.com
// */
//public class DateTime {
//  private String dateStr;
//  private int maxDays;
//  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//  private Calendar calendar = Calendar.getInstance();
//  private ParsePosition parsePosition = new ParsePosition(8);
//  private int dayOfWeek;
//  private int monthDiff;
//  private int weekDiff;
//
//  public DateTime() {
//    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//    dateStr = dateFormat.format(new Date());
//    Log.i("dateTimes", "new Date() dateTimes：" + dateStr);
//  }
//
//  public   String getLocalDatetimeString(String local) {
//
//    Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(local));
//
//    cal.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
//
//    String date = cal.get(Calendar.YEAR) +"-"+ (cal.get(Calendar.MONTH) + 1) +"-"+ cal.get(Calendar.DAY_OF_MONTH);
//
//    return dateFormat.format(cal.getTime());
//  }
//
//  public DateTime(String date) {
//    dateStr = date;
//    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//  }
//
//  /**
//   * 获取某年中的某月的第 day 天是星期几
//   *
//   * @param month 目标月份
//   *
//   * @return
//   */
//  public DateTime(int year, int month, int day) {
//    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//    Calendar cal = Calendar.getInstance();
//    cal.set(year, month - 1, day);
//    dateStr = dateFormat.format(cal.getTime());
//    dayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) - 1) == 0 ? 7 : cal.get(Calendar.DAY_OF_WEEK) - 1;
//  }
//
//  /**
//   * 日期
//   *
//   * @return 日期 int
//   */
//  public int getDayOfMonth() {
//    calendar.setTime(toLocalDate());
//    return calendar.get(Calendar.DATE);
//  }
//
//  /**
//   * 年份
//   *
//   * @return 年份 int
//   */
//  public int getYear() {
//    calendar.setTime(toLocalDate());
//    return calendar.get(Calendar.YEAR);
//  }
//
//  /**
//   * 月份
//   *
//   * @return 月份 int
//   */
//  public int getMonthOfYear() {
//    calendar.setTime(toLocalDate());
//    return calendar.get(Calendar.MONTH) + 1;
//  }
//
//  /**
//   * 日期格式化
//   *
//   * @return date yyyy-MM-dd
//   */
//  public Date toLocalDate() {
//    return dateFormat.parse(dateStr,new ParsePosition(0));
//  }
//
//  /**
//   * 月份增加
//   *
//   * @param i 增加几
//   *
//   * @return 月份 int
//   */
//  public DateTime plusMonths(int i) {
//    calendar.setTime(toLocalDate());
//    calendar.add(Calendar.MONTH, i);
//    dateStr = dateFormat.format(calendar.getTime());
//    return new DateTime(dateStr);
//  }
//
//  /**
//   * 日期增加
//   *
//   * @param i 增加几
//   *
//   * @return 日期 int
//   */
//  public DateTime plusDays(int i) {
//    calendar.setTime(toLocalDate());
//    calendar.add(Calendar.DAY_OF_MONTH, i);
//    dateStr = dateFormat.format(calendar.getTime());
//    return new DateTime(dateStr);
//  }
//
//  /**
//   * 周增加
//   *
//   * @param i 增加几
//   *
//   * @return 周 int
//   */
//  public DateTime plusWeeks(int i) {
//    calendar.setTime(toLocalDate());
//    calendar.add(Calendar.WEEK_OF_MONTH, i);
//    dateStr = dateFormat.format(calendar.getTime());
//    return new DateTime(dateStr);
//  }
//
//  /**
//   * 返回一周中的第几天
//   *
//   * @return 第几天
//   */
//  public int dayOfWeek() {
//    calendar.setTime(toLocalDate());
//    int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
//    return i == 0 ? 7 : i;
//  }
//
//  /**
//   * 日期转换成秒数
//   *
//   * @return 秒数
//   */
//  public long getMillis() {
//    return toLocalDate().getTime();
//  }
//
//  public DateTime withTimeAtStartOfDay() {
//    return this;
//  }
//
//  /**
//   * 格式化时间
//   *
//   * @param format 日期格式
//   *
//   * @return 日期
//   */
//  public String toString(String format) {
//    SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
//    return formatter.format(toLocalDate());
//  }
//
//  public String getDateStr() {
//    return dateStr;
//  }
//
//  public DateTime dayOfMonth() {
//    switch (getMonthOfYear()) {
//      case 1:
//      case 3:
//      case 5:
//      case 7:
//      case 8:
//      case 10:
//      case 12:
//        maxDays = 31;
//        break;
//      case 4:
//      case 6:
//      case 9:
//      case 11:
//        maxDays = 30;
//        break;
//      case 2:
//        if (isLeapYear()) {
//          maxDays = 29;
//        } else {
//          maxDays = 28;
//        }
//    }
//    return this;
//  }
//
//  /**
//   * 判断是否润年
//   *
//   * @return true 闰年
//   */
//  public boolean isLeapYear() {
//    /**
//     * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
//     * 3.能被4整除同时能被100整除则不是闰年
//     */
//    Calendar gc = Calendar.getInstance();
//    gc.setTime(toLocalDate());
//    int year = gc.get(Calendar.YEAR);
//    if ((year % 400) == 0) {
//      return true;
//    } else if ((year % 4) == 0) {
//      if ((year % 100) == 0) {
//        return false;
//      } else {
//        return true;
//      }
//    } else {
//      return false;
//    }
//  }
//
//  public int getMaximumValue() {
//    return maxDays;
//  }
//
//  public int getDayOfWeek() {
//    return dayOfWeek;
//  }
//
//  public DateTime withDayOfMonth(int i) {
//    return new DateTime(getYear(), getMonthOfYear(), i);
//  }
//
//  /**
//   * 获取两个日期相差几个月
//   *
//   * @param startDate
//   * @param endDate
//   *
//   * @return
//   */
//  public DateTime monthsBetween(DateTime startDate, DateTime endDate) {
//    Date start = startDate.toLocalDate();
//    Date end = endDate.toLocalDate();
//    if (start.after(end)) {
//      Date t = start;
//      start = end;
//      end = t;
//    }
//    Calendar startCalendar = Calendar.getInstance();
//    startCalendar.setTime(start);
//    Calendar endCalendar = Calendar.getInstance();
//    endCalendar.setTime(end);
//    Calendar temp = Calendar.getInstance();
//    temp.setTime(end);
//    temp.add(Calendar.DATE, 1);
//
//    int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
//    int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
//
//    if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
//      monthDiff = year * 12 + month + 1;
//    } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
//      monthDiff = year * 12 + month;
//    } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
//      monthDiff = year * 12 + month;
//    } else {
//      monthDiff = (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
//    }
//    return this;
//  }
//
//  public int getMonths() {
//    return monthDiff;
//  }
//
//  public DateTime weeksBetween(DateTime startDate, DateTime endDate) {
//    Date start = startDate.toLocalDate();
//    Date end = endDate.toLocalDate();
//    int day = (int) Math.abs((start.getTime() - end.getTime()) / (24 * 60 * 60 * 1000));
//    weekDiff = day / 7;
//    return this;
//  }
//
//  public int getWeeks() {
//    return weekDiff;
//  }
//
//  /**
//   * 前移i个星期
//   *
//   * @param i
//   */
//  public DateTime minusWeeks(int i) {
//    return plusWeeks(-i);
//  }
//
//  public DateTime withDayOfWeek(int i) {
//    return plusDays(i);
//  }
//}
