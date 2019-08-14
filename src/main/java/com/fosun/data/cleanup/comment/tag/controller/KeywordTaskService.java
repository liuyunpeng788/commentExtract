package com.fosun.data.cleanup.comment.tag.controller;


import com.fosun.data.cleanup.comment.tag.service.impl.BaiduApiServiceImpl;
import com.fosun.data.cleanup.comment.tag.utils.ContextUtil;
import com.fosun.data.cleanup.comment.tag.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author: liumch
 * @create: 2019/7/26 11:21
 **/
@Slf4j
public class KeywordTaskService implements Runnable {
    private RedisTemplate redisTemplate;
    private String PRE_DATE_KEY =  "JOYCITY:APP:DATACLEAN:END_DATE:"  ;
    /**
     * 记录处理历史数据时，没有获取到数据的次数
     * 如果连续的次数超过阈值，则不再处理历史数据流程
     */
    private  String PRE_EMPTY_TIMES = "JOYCITY:APP:DATACLEAN:EMPTY_TIMES:"  ;


    private final BaiduApiServiceImpl baiduApiService;

    /**
     * 评论类型  0:商场 1：店铺 null: 同时跑商场和店铺
     */
    private Integer type;

    /**
     * 每天几点停止处理
     */
    private final static int STOP_HOUR = 22;
    /**
     * 每次处理多少天的数据
     */
    private final static int MINUS_DATE = 5;

    /**
     * 是否不跑当天的数据
     * 默认为false.
     */
    private boolean ignoreTodayFlag = false;
    /**
     * 起始日期 yyyy-MM-dd格式
     */
    String strStartDate;
    /**
     * 结束日期 yyyy-MM-dd格式
     */
    private String strEndDate;



    /**
     * 连续多少次没有数据。因为每次跑(@link MINUS_DATE) 天的数据，所以，如果连续3*MINUS_DATE 天没有数据，
     * 则可以说明已经没历史数据需要跑了。如果redis 中的阀值操作了Integer.max,则不再增加。
     * 否则，没有数据，则 EMPTY_TIMES 对应的值加1
     */
    private final static int THRESHOLD = 3;

    public KeywordTaskService(RedisTemplate redisTemplate,Integer type,boolean ignoreTodayFlag,String strStartDate,String strEndDate){
        this.redisTemplate = redisTemplate;
        this.type = null == type? 0: type;
        this.PRE_DATE_KEY += this.type;
        this.PRE_EMPTY_TIMES += this.type;
        this.ignoreTodayFlag = ignoreTodayFlag;
        this.strStartDate = strStartDate;
        this.strEndDate = strEndDate;
        baiduApiService = ContextUtil.applicationContext.getBean(BaiduApiServiceImpl.class);
    }
    @Override
    public void run() {

        if(StringUtils.isNotBlank(strEndDate) && StringUtils.isNotBlank(strStartDate) && null != type){
            //走补数据的流程
            baiduApiService.runMakeupData(type,strStartDate,strEndDate);
            return;
        }

        Date end = null;

        Date startDate =  DateUtil.getTimeOfPreviousDays(1) ;
        if(!ignoreTodayFlag){
            log.info("start to deal with increase data....");
            long begin = System.currentTimeMillis();
            //处理当天的数据
            try {
                baiduApiService.dealCommentTag(null,type,startDate,null);
            } catch (Exception e) {
                log.error("处理当天的评论数据出现异常.错误信息:{}",e.getMessage() );
            }
            log.info("finish to deal with increase data, time:{}s" ,(System.currentTimeMillis()-begin)*1.0/1000 );
        }

        /**
         * 出现次数
         */
        if(!redisTemplate.hasKey(PRE_EMPTY_TIMES)){
            redisTemplate.opsForValue().set(PRE_EMPTY_TIMES ,"0",10,TimeUnit.DAYS);
        }
        Integer times = Integer.valueOf((String)redisTemplate.opsForValue().get(PRE_EMPTY_TIMES));
         //每次处理前十天的数据
        LocalDateTime localDateTime = LocalDateTime.now();
        if(redisTemplate.hasKey(PRE_DATE_KEY)){
            end = DateUtil.asDate(LocalDate.parse((String)redisTemplate.opsForValue().get(PRE_DATE_KEY),DateUtil.dateFormatter)) ;
        }else{
            end = startDate;
        }

        log.info("end date:{}",end);
        //计数,观察当天跑了几轮
        AtomicInteger shop_num = new AtomicInteger(1);
        AtomicInteger mall_num = new AtomicInteger(1);

        while(times < THRESHOLD && localDateTime.getHour()< STOP_HOUR){
            log.info("start....current times:{}",times);
            long begin = System.currentTimeMillis();
            Date start = DateUtil.minusDays(end,MINUS_DATE);
            try {
                int records = baiduApiService.dealCommentTag(null,type,start,end);
                if(records == 0){
                   times =Integer.valueOf((String) redisTemplate.opsForValue().get(PRE_EMPTY_TIMES ));
                   if( times < Integer.MAX_VALUE){
                       redisTemplate.opsForValue().set(PRE_DATE_KEY  ,String.valueOf(times + 1),10,TimeUnit.DAYS);
                   }
               }
            } catch (Exception e) {
               log.error("处理日期为：{}-{}类型为：{}的数据异常，异常信息：{}" ,start,end,type,e.getMessage());
            }
            end = start;
            localDateTime = LocalDateTime.now();
            redisTemplate.opsForValue().set(PRE_DATE_KEY  ,DateUtil.asLocalDateTimeString(end,DateUtil.dateFormatter),3L, TimeUnit.DAYS);
            log.info("num:{},type:{},finish clean up {} days data ,time:{}s" ,type.equals(1)?shop_num.getAndIncrement():mall_num.getAndIncrement(),type,MINUS_DATE,(System.currentTimeMillis()-begin)*1.0/1000 );
        }


    }
}
