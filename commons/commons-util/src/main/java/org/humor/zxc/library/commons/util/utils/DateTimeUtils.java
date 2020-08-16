package org.humor.zxc.library.commons.util.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期、时间常用方法类.
 * <p>
 * 
 */
public final class DateTimeUtils {

	// 格式：年－月－日 小时：分钟：秒
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	// 格式：年－月－日 小时：分钟
	public static final String FORMAT_TWO = "yyyy-MM-dd HH:mm";

	// 格式：年月日 小时分钟秒
	public static final String FORMAT_THREE = "yyyyMMdd-HHmmss";

	// 格式：年－月－日
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	// 格式：月－日
	public static final String SHORT_DATE_FORMAT = "MM-dd";

	// 格式：小时：分钟：秒
	public static final String HH_MM_SS = "HH:mm:ss";

	// 格式：年-月
	public static final String YYYY_MM = "yyyy-MM";

	// 年的加减
	public static final int SUB_YEAR = Calendar.YEAR;

	// 月加减
	public static final int SUB_MONTH = Calendar.MONTH;

	// 天的加减
	public static final int SUB_DAY = Calendar.DATE;

	// 小时的加减
	public static final int SUB_HOUR = Calendar.HOUR;

	// 分钟的加减
	public static final int SUB_MINUTE = Calendar.MINUTE;

	// 秒的加减
	public static final int SUB_SECOND = Calendar.SECOND;

	static final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四",
			"星期五", "星期六" };

	@SuppressWarnings("unused")
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
		
	private DateTimeUtils() {

	}
	
	/**
	 * 获取当前时间，以秒返回
	 * @return
	 */
	public static int getCurrTime(){
		return (int)(System.currentTimeMillis()/1000);
	}

	public static Date getCurrDate() {
		return new Date(System.currentTimeMillis());
	}
	/**
	 * 当前是哪一天
	 * @return
	 */
	public static int getDayCount() {
		long daynum = System.currentTimeMillis() / 1000 / 60 / 60 / 24;

		return (int) daynum;
	}

	/**
	 * 当前是哪一天
	 * @param datetime
	 * @return
	 */
	public static int getDayCount(final String datetime) {
		int[] arr = parseDatetimeToArray(datetime);
		int year = arr[0];
		int month = arr[1];
		int day = arr[2];

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		long daynum = cal.getTimeInMillis() / 1000 / 60 / 60 / 24;
		return (int) daynum;
	}

	public static int getDayCount(final String date1, String date2) {

		int dayCount1 = DateTimeUtils.getDayCount(date1);
		int dayCount2 = DateTimeUtils.getDayCount(date2);
		return (dayCount1 - dayCount2);
	}

	public static String getDate() {
		return getDate(System.currentTimeMillis());
	}

	public static String getDate(final int daynum) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, daynum);
		return getDate(cal.getTimeInMillis());
	}

	public static String addDate(final int daynum) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, daynum);
		return getDate(cal.getTimeInMillis());
	}

	public static String addDate(final String date, final int daynum) {
		int[] arr = parseDatetimeToArray(date);
		int year = arr[0];
		int month = arr[1];
		int day = arr[2];

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day + daynum);
		return getDate(cal.getTimeInMillis());
	}

	/**
	 * 将时间或者日期转换成int数组
	 * 
	 * @param datetime
	 * @return
	 */
	public static int[] parseDatetimeToArray(final String datetime) {
		int year = Integer.parseInt(datetime.substring(0, 4));
		int month = Integer.parseInt(datetime.substring(5, 7));
		int day = Integer.parseInt(datetime.substring(8, 10));

		return new int[] { year, month, day };
	}

	private static final SimpleDateFormat GET_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static synchronized String getDate(final long millis) {
		Date date = new Date();
		if (millis > 0) {
			date.setTime(millis);
		}
		return GET_DATE_FORMAT.format(date);
	}
	
	/**
	 * 返回long时间戳
	 * @param dateTime 格式yyyy-MM-dd
	 * @return
	 */
	public static long getDateLong(String dateTime) {
		Date date;
		try {
			date = GET_DATE_FORMAT.parse(dateTime);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 返回long时间戳
	 * @param dateTime
	 * @param pattern 格式
	 * @return
	 */
	public static long getDateLong(String dateTime, String pattern) {
		Date date;
		try {
			date = new SimpleDateFormat(pattern).parse(dateTime);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getHour() {
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.HOUR_OF_DAY);
	}

	public static int getDay() {
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.DAY_OF_MONTH);
	}

	public static int getMonth() {
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.MONTH);
	}
	
	public static int getYear(){
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.YEAR);
	}

	public static int getMinute() {
		Calendar cld = Calendar.getInstance();
		return cld.get(Calendar.MINUTE);
	}

	public static String getTime() {
		return getTime(0);
	}

	/**
	 * long millis加上int minute 如int很大会出现被载断情况，得出的结果错误， 现将int转换成long后再进行计算.
	 * 如计算一年后的时间24*60*365*60*1000此数已超出int范围,截断后计算出错。
	 */
	public static String addTime(final int minute) {
		long millis = System.currentTimeMillis();
		millis = millis + (minute * 60L * 1000L);
		return getTime(millis);
	}

	public static String addTime(String time, int minute) {
		long millis = getTimestamp(time);
		millis = millis + (minute * 60L * 1000L);
		return getTime(millis);
	}

	private static final SimpleDateFormat GET_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 返回时间long 
	 * @param dateTime 格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static long getDateTimeLong(String dateTime){
		Date date;
		try {
			date = GET_TIME_FORMAT.parse(dateTime);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getTime(int second) {
		long millis = (long) second * 1000L;
		return getTime(millis);
	}

	public static String getTime(String time) {
		if (time == null) {
			return null;
		}
		else {
			return time.substring(0, 19);
		}
	}

	public static synchronized String getTime(final long millis) {
		Date date = new Date();
		if (millis > 0) {
			date.setTime(millis);
		}
		return GET_TIME_FORMAT.format(date);
	}

	/**
	 * 根据字符串获取时间戳
	 * 
	 * TODO old 根据字符串获取时间戳方法很消耗性能，待优化
	 * 
	 * @param datetime
	 * @return
	 */
	public static long getTimestamp(final String datetime) {
		Calendar cal = Calendar.getInstance();

		int year = Integer.parseInt(datetime.substring(0, 4));
		int month = Integer.parseInt(datetime.substring(5, 7));
		int day = Integer.parseInt(datetime.substring(8, 10));

		int hour = Integer.parseInt(datetime.substring(11, 13));
		int minute = Integer.parseInt(datetime.substring(14, 16));
		int second = Integer.parseInt(datetime.substring(17, 19));

		cal.set(year, month - 1, day, hour, minute, second);
		if (datetime.length() > 19) {
			int mill = Integer.parseInt(datetime.substring(20));
			cal.set(Calendar.MILLISECOND, mill);
		}
		else {
			cal.set(Calendar.MILLISECOND, 0);
		}

		return cal.getTimeInMillis();
	}

	public static long getTimestamp() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}

	public static int getUnixTimestamp() {
		long timestamp = getTimestamp();
		return (int) (timestamp / 1000);
	}

	public static int getUnixTimestamp(String datetime) {
		long timestamp = getTimestamp(datetime);
		return (int) (timestamp / 1000);
	}

	private static final String IS_DATE_REGEX = "^[0-9]{4}\\-[0-9]{1,2}\\-[0-9]{1,2}$";

	/**
	 * 判断字符串是否为正确的日期格式
	 * 
	 * @param date
	 * @return 是否合法日期格式
	 */
	public static boolean isDate(final String date) {
		if (date == null) {
			return false;
		}

		return date.matches(IS_DATE_REGEX);
	}

	private static final String IS_TIME_REGEX = "^[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}$";

	/**
	 * 判断字符串是否为正确的时间格式
	 * 
	 * @param time
	 * @return 是否合法时间格式
	 */
	public static boolean isTime(final String time) {
		if (time == null) {
			return false;
		}
		return time.matches(IS_TIME_REGEX);
	}

	private static final String IS_DATETIME_REGEX = "^[0-9]{4}\\-[0-9]{1,2}\\-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}(\\.[0-9]{1,3})?$";

	/**
	 * 判断字符串是否为正确的日期 + 时间格式
	 * 
	 * @param str
	 * @return 是否合法日期 + 时间格式
	 */
	public static boolean isDateTime(final String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		return str.matches(IS_DATETIME_REGEX);
	}

	public static int getSecond(final String datetime) {
		long time = getTimestamp(datetime);
		return (int) (time / 1000);
	}

	@SuppressWarnings("deprecation")
	public static String getGMT(final String time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getTimestamp(time));
		Date date = cal.getTime();
		return date.toGMTString();
	}

	private static final String[] CN_WEEK_NAMES = { "天", "一", "二", "三", "四", "五", "六" };

	public static String getWeekName() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return CN_WEEK_NAMES[day];
	}

	private static final String[] EN_WEEK_NAMES = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

	public static String getWeekName(final String datetime) {
		Calendar cld = Calendar.getInstance();
		cld.setTimeInMillis(getTimestamp(datetime));
		int num = cld.get(Calendar.DAY_OF_WEEK) - 1;
		return EN_WEEK_NAMES[num];
	}

	/**
	 * 获取月份的天数
	 * 
	 * @param monthNum
	 *            0:表示当前月份 负数：表示前n个月份 整数：表示后n个月份
	 * @return
	 */
	public static int getDayCountOfMonth(final int monthNum) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, monthNum);
		cal.set(Calendar.DATE, 1);
		int daynum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return daynum;
	}

	/**
	 * 获取月份的第一天
	 * 
	 * @param monthNum
	 *            0:表示当前月份 负数：表示前n个月份 整数：表示后n个月份
	 * @return
	 */
	public static String getFirstDayOfMonth(final int monthNum) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, monthNum);
		cal.set(Calendar.DATE, 1);
		return getDate(cal.getTimeInMillis());
	}

	public static String getFirstDayOfMonth(final String date, final int monthNum) {
		int[] arr = parseDatetimeToArray(date);
		int year = arr[0];
		int month = arr[1];
		int day = arr[2];

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		cal.add(Calendar.MONTH, monthNum);
		cal.set(Calendar.DATE, 1);
		return getDate(cal.getTimeInMillis());
	}

	/**
	 * 获取星期一
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonday(final String date) {
		int[] arr = parseDatetimeToArray(date);
		int year = arr[0];
		int month = arr[1];
		int day = arr[2];

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);

		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, 2);
		return getDate(cal.getTimeInMillis());
	}

	/**
	 * 判断传入的日期是否为今天
	 * 
	 * @param time
	 *            String
	 * @return boolean
	 */
	public static boolean isToday(String time) {
		if (time == null || time.length() < 10) {
			return false;
		}

		time = time.substring(0, 10);
		if (DateTimeUtils.getDate().equals(time)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否是今天
	 * @param time 时间的int值秒级别
	 * @return
	 */
	public static boolean isToday(int time){
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),0,0,0);
		
		if(cal.getTimeInMillis()/1000 < time){
			return true;
		}
		return false;
	}

	private static final SimpleDateFormat GET_INT_TIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 把符合日期格式的字符串转换为日期类型
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date stringtoDate(String dateStr, String format) {
		Date d = null;
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			formater.setLenient(false);
			d = formater.parse(dateStr);
		} catch (Exception e) {
			// log.error(e);
			d = null;
		}
		return d;
	}

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 */
	public static Date stringtoDate(String dateStr, String format,
			ParsePosition pos) {
		Date d = null;
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			formater.setLenient(false);
			d = formater.parse(dateStr, pos);
		} catch (Exception e) {
			// log.error(e);
			d = null;
		}
		return d;
	}

	/**
	 * 把日期转换为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		String result = "";
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			result = formater.format(date);
		} catch (Exception e) {
			// log.error(e);
		}
		return result;
	}

	/**
	 * 获取当前时间的指定格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrDate(String format) {
		return dateToString(new Date(), format);
	}
	
	/**
	 * 时间转秒数
	 * 
	 * @param date
	 * @return
	 */
	public static long getSecondsByDate(String date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(stringtoDate(date, DateTimeUtils.YYYY_MM_DD));
		return calendar.getTimeInMillis() / 1000;
	}
	
	
	/**
	 * 返回字符串格式的时间的秒数
	 * 
	 * @param dateTime 年-月-日 时:分:秒 格式的时间串
	 * @return
	 */
	public static long getSecondsByDateTime(String dateTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(stringtoDate(dateTime, DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
		return calendar.getTimeInMillis() / 1000;
	}
	
	public static int getTimeStamp() {
        long longTime = System.currentTimeMillis();
        return (int) (longTime / 1000);
    }
 
    public static Date formatTimeStamp(int timestamp) {
        long longTime = toLong(timestamp);
        return new Date(longTime);
    }
 
    private static long toLong(int timestamp) {
        return (long) timestamp * 1000;
    }
    
    /**
     * 根据时间戳，转换成指定的时间格式的字符串
     * 
     * @param timestamp 时间戳，单位：秒
     * @param pattern 比如 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatTimeStamp(int timestamp, String pattern){
        return DateFormatUtils.format(toLong(timestamp), pattern);
    }
    
	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static synchronized String getIntTime() {
		Date date = new Date();
		return GET_INT_TIME_FORMAT.format(date);
	}
	
	public static String getBefore(long time){
		String message = "";
		long now = System.currentTimeMillis();
		if(now>time){
			//计算是不是1天内的时间差
			long diffTime = now - time;
			if(diffTime < 86400000){//一天内
				//是1天内的时间差
				int diff = (int)diffTime/3600000;
				if(diff>0){
					return diff + "小时前";
				}else{
					diff = (int)diffTime/60000;
					if(diff>0){
						return diff + "分钟前";
					}else{
						if(diffTime>=5000){
							return diffTime/1000 + "秒前";
						}else{
							return "刚刚";
						}
					}
				}
			}else if(diffTime<86400000L * 30){//月内
				return diffTime/86400000L + "天前";
			}else if(diffTime<86400000L * 365){//年内
				return diffTime/(86400000L * 30) + "月前";
			}else {
				return diffTime/(86400000L * 365) + "年前";
			}
			
			/*
			if(diffTime<=86400000){
				//是1天内的时间差
				int diff = (int)diffTime/3600000;
				if(diff>0){
					message = diff + "小时前";
				}else{
					diff = (int)diffTime/60000;
					if(diff>0){
						message = diff + "分钟前";
					}else{
						if(diffTime>=5000){
							message = diffTime/1000 + "秒前";
						}else{
							message = "刚刚";
						}
					}
				}
			}else{
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				
				c1.setTimeInMillis(now);
				c2.setTimeInMillis(time);
				
				int YearDiff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
				int MonthDiff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
				int DayDiff = c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH) ;
				if(MonthDiff < 0){
					MonthDiff += 12;
					YearDiff--;
				}
				if(YearDiff>0){
					message = YearDiff + "年前";
				}else{
					if(MonthDiff>0){
						message = MonthDiff + "个月前";
					}else{
						DayDiff = c1.get(Calendar.DAY_OF_MONTH)  - c2.get(Calendar.DAY_OF_MONTH) ;
						if(DayDiff>0){
							message = DayDiff + "天前";
						}
					}
				}
			}
			*/
		}
		return message;
	}
		
	public static String subMonth(String date,int month)  {  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        Date dt;
		try {
			dt = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}  
        Calendar rightNow = Calendar.getInstance();  
        rightNow.setTime(dt);  
  
        rightNow.add(Calendar.MONTH, -month);  
        Date dt1 = rightNow.getTime();  
        String reStr = sdf.format(dt1);  
  
        return reStr;  
    } 
	
	public static int subMonth(int date,int month){
		Date dt = new Date(((long)date) * 1000);
		Calendar rightNow = Calendar.getInstance();  
	    rightNow.setTime(dt);  
	    rightNow.add(Calendar.MONTH, -month);  
	    Date dt1 = rightNow.getTime();
	    return (int)(dt1.getTime() / 1000);
	}
	
	public static int getMonthTimeStmp(int timeStmp){
		Date date = new Date(timeStmp * 1000L);
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("ddddd:" + df.format(c.getTime()));
		return DateTimeUtils.getTimeStmp(df.format(c.getTime()));
	}

	public static int getTimeStmp(String dateFromat){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(dateFromat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 
		 int timeStemp =  (int) (date.getTime() / 1000L);
	     return timeStemp;
	}

	/**
	 * 根据时期时间,转换成时间秒
	 * @param date
	 * @return
	 */
	public static int getSecondsByDate2(Date date){
		if(null == date){
			return DateTimeUtils.getCurrTime();
		}
		return (int)(date.getTime()/1000);
	}
	
	/**
	 * "yyyy-MM-dd HH:mm:ss"
	 * @param str
	 * @return
	 */
	public static Date parseStrToDate(String str) {
		if(StringUtil.isNull(str)){
			return null;
		}
		try {
			DateFormat fmtTemp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return fmtTemp.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date getDate(Long date) {
		if(date == null){
			return null;
		}
		return new Date(date);
	}
	
	public static int calDateDualAsMin(Date fromDate, Date toDate){
		if(fromDate == null || toDate == null){
			return 0;
		}
		
		long st = fromDate.getTime();
		long end = toDate.getTime();
		
		long d = (end - st)/(1000 * 60);
		
		return (int)d;
	}
	
	public static void main(final String[] args) {
//		System.out.println(getYear());
//		System.out.println(getMonth());
//		System.out.println(getDate());
		Integer currTime = DateTimeUtils.getCurrTime();
		currTime = Integer.parseInt(String.valueOf(DateTimeUtils.getSecondsByDateTime("2017-02-28 12:34:56")));
		long sss = DateTimeUtils.getMonthTimeStmp(currTime);
		System.out.println("当前时间戳：" + currTime);
		System.out.println("当前时间戳2：" + sss);
		System.out.println("ttt:" + DateTimeUtils.getTime(sss * 1000));
		System.out.println("当前时间戳3：" + DateTimeUtils.getTime(1452239640));
//		
//		String date = "2015-03-12";
//		long aa = DateTime.getSecondsByDate(date);
//		System.out.println("aa:" + aa);
//		
//		String date2 = "2016-01-01 00:00:00";
//		long bb = DateTime.getSecondsByDateTime(date2);
//		System.out.println("bb:" + bb);
//		
//		String cc = DateTime.addDate(date, 2*30);
//		System.out.println("cc:" + cc);
//		
//		String dd = DateTime.getDate();
//		System.out.println("dd:" + dd);
//		
//		String ee = DateTime.addDate(dd, -2*30);
//		System.out.println("ee:" + ee);
//		
//		String beginDate = "2015-04-10";
//		int ff = (int)DateTime.getSecondsByDate(beginDate);
//		System.out.println("ff:" + ff);
		System.out.println(subMonth(DateTimeUtils.getCurrTime(), 1));
		
		System.out.println(DateTimeUtils.getTime());
	}

}
