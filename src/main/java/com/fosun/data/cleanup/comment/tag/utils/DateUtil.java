package com.fosun.data.cleanup.comment.tag.utils;


 import com.fosun.data.cleanup.comment.tag.exception.BizException;
 import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

 import static com.fosun.data.cleanup.comment.tag.exception.ErrorCode.ARGS_ERROR;
 import static org.apache.commons.lang3.time.DateFormatUtils.format;

/** 处理日期相关信息
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/3/28 16:43
 * @description: 处理日期信息
 * @modified: TODO
 * @version: 1.0
 **/
public class DateUtil {
    public static final DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter monthformat = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * 获取下月的月份 格式：yyyy-MM-dd
     * @return 下个月的月份
     */
    public static Date getFirstDayOfNextMonth(){
         return asDate(LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth()))  ;
    }




    /**
     * 获取指定月的下一个月份的第一天 格式：yyyy-MM-dd
     * @param  time : yyyy-MM-dd 或者 yyyy-MM格式
     * @return  Date 类型。 指定月的下个月的第一天
     */
    public static Date getFirstDayOfNextMonth(String time) throws BizException{
        return getFirstDay(time,TemporalAdjusters.firstDayOfNextMonth());
    }

    /**
     * 获取指定月的下2个月份的第一天 格式：yyyy-MM-dd
     * @param  time : yyyy-MM-dd 或者 yyyy-MM格式
     * @return  Date 类型。 指定月的下个月的第一天
     */
    public static Date getLastDayOfNextMonth(String time) throws BizException {
        LocalDate localDateTime = StringUtils.isNotBlank(time) ? LocalDate.parse(time + "-01",dateFormatter) : LocalDate.now().withDayOfMonth(1);
        localDateTime = localDateTime.plusMonths(2);
        return asDate(localDateTime);
    }


    /**
     * 减去一个月
     * @param date 日期类型
     * @return 前一个月的日期
     */
    public static Date minusOneMonth(Date date){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.minusMonths(1);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }




    /**
     * 根据起止月份字符串参数，获取月份列表
     * @param start 起始月份
     * @param end 结束月份
     * @return 月份的列表
     * @throws BizException
     */
    public static List<String> getDateRangeList(String start,String end) throws BizException{
        List<String> monthList = new ArrayList<>();

        if(StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)){
            if( start.length() == 7){
                start += "-01";
            }
            if( end.length() == 7){
                end += "-01";
            }
            LocalDate startDate =  LocalDate.parse(start,dateFormatter);
            LocalDate endDate =  LocalDate.parse(end ,dateFormatter);
            while(!startDate.isAfter(endDate)){
                monthList.add(startDate.format(monthformat));
                startDate = startDate.plusMonths(1);
            }
        }
        return monthList;

    }

    /**
     * 获取指定月的第一天 格式：yyyy-MM-dd
     * @param  time : yyyy-MM-dd 或者 yyyy-MM格式
     * @return  Date 类型。 指定月的第一天
     */
    public static Date getFirstDayOfCurrentMonth(String time) throws BizException{
        return getFirstDay(time,null);
    }



    /**
     * 获取指定月的第一天 格式：yyyy-MM-dd
     * @return 指定月的第一天
     */
    public static Date getFirstDay(String time, TemporalAdjuster adjuster) throws BizException{
        if(StringUtils.isBlank(time)){
            throw new BizException(ARGS_ERROR,"时间参数不能为空");
        }
        try{
            if( time.length() == 7){
                time += "-01";
            }

            LocalDate localDate = time.length() > 10 ? LocalDate.parse(time,datetimeFormatter): LocalDate.parse(time,dateFormatter);
            return null == adjuster? asDate(localDate):asDate(localDate.with(TemporalAdjusters.firstDayOfNextMonth()));
        }catch (Exception ex){
            throw new BizException(ARGS_ERROR,String.format("时间参数[%s]字符串错误，无法转成日期格式", time));
        }
    }

    /**
     * 获取上一个月的月份 格式：yyyy-MM-dd
     * @return 字符串格式的上个月月份
     */
    public static Date getFirstDayOfLastMonth(){
        return  asDate(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1));
    }

    /**
     * 获取当前月的月份的第一天 格式：yyyy-MM-dd
     * @return  Date 当前月月份
     */
    public static Date getFirstDayOfCurrentMonth(){
        return asDate(LocalDate.now().withDayOfMonth(1)) ;
    }


    /**
     * LocalDate 转换为 date
     * @param localDate 日期格式
     * @return Date 类型
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * date to LocalDateTime 字符串
     * @param date 日期
     * @return localDateTime 字符串
     */
    public static String asLocalDateTimeString(Date date){
        return asLocalDateTimeString(date,dateFormatter);
    }

    /**
     * date to LocalDateTime 字符串
     * @param date 日期
     * @return localDateTime 字符串
     */
    public static String asLocalDateTimeString(Date date,DateTimeFormatter formatter){
        if(null != date && null != formatter){
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(formatter);
        }
        return null;
    }

    public static String minusOneMonthAsDateString(Date date,DateTimeFormatter formatter){
        if(null != date && null != formatter){
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
            localDateTime = localDateTime.minusMonths(1);
            return localDateTime.format(formatter);
         }
        return null;
    }
    /**
     * LocalDateTime 转换为 date
     * @param localDateTime 时间格式
     * @return Date 类型
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得某天最大时间
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得某天最小时间
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static Date getLastDayfCurrentMonth(Date dateTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.getActualMaximum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    public static Date getFirstDayfCurrentMonth(Date dateTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    public static List<String> getMonthBetweenDates(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(new SimpleDateFormat("yyyy-MM").parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.setTime(new SimpleDateFormat("yyyy-MM").parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(format(curr.getTime(),"yyyy-MM"));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 获取某段时间内的所有日期
     * @param dStart
     * @param dEnd
     * @return
     */
    public static List<Date> findDates(Date dStart, Date dEnd) {
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dStart);

        List dateList = new ArrayList();
        dateList.add(dStart);
        while (dEnd.after(cStart.getTime())) {
            cStart.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(cStart.getTime());
        }
        return dateList;
    }

    /**
     * 获取前几天的凌晨时间，00：00：00
     * @param days 几天前
     * @return 返回几天前的日期
     */
    public static Date getTimeOfPreviousDays(int days) {
        return  asDate(LocalDateTime.now().minusDays(days).withHour(0).withMinute(0).withSecond(0).withNano(0));

    }

    /**
     * 获取date 类型日期的前几天
     * @param currentDate  当前日期
     * @param days 日期天数
     * @return 前几天的时间
     */
    public static Date minusDays(Date currentDate,int days){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentDate.getTime()), ZoneId.systemDefault());
         localDateTime = localDateTime.minusDays(days).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static void main(String[] args) throws Exception {
        Date dt1 = new Date();
        dt1 = minusDays(dt1,15*8+2);
        System.out.println(dt1);

        String date = LocalDateTime.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        System.out.println(date);
        String startDate = "2018-07";
        String endDate = "2019-07";
        Date start = getFirstDayOfNextMonth(startDate);
        Date end = getLastDayOfNextMonth(endDate);

        LocalDate ldt = LocalDate.now().plusMonths(2).withDayOfMonth(1);
        System.out.println(ldt.toString());

        Date dt = new Date();
        System.out.println(dt);
        System.out.println("---" + minusOneMonth(dt));

        System.out.println(getLastDayOfNextMonth(""));

        List<String> months = getDateRangeList("2019-01","2019-03");
        months.forEach(System.out::println);

        System.out.println(getTimeOfPreviousDays(1));

        System.out.println(getFirstDayOfNextMonth());
        System.out.println(asLocalDateTimeString(new Date()));

        String lastMonthFirstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1).format(dateFormatter);
        System.out.println(lastMonthFirstDay);

        System.out.println(getFirstDayOfCurrentMonth().toString());
        System.out.println(getFirstDayOfNextMonth("2019-01").toString());
    }


}
