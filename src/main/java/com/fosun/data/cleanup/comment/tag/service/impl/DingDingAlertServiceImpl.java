package com.fosun.data.cleanup.comment.tag.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 钉钉告警服务
 * @author: liumch
 * @create: 2019/8/14 15:49
 **/
@Component
@Slf4j
public class DingDingAlertServiceImpl {
    @Value("{$alert_threshold}")
    private Integer alertThreshold;

    @Value("${alert_url}")
    private String alertUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     *  百度 api 访问的结果队列
     */
    private final Queue<Boolean> resQueue = new ArrayBlockingQueue<>(alertThreshold);


    /**
     * 统一的对外接口服务。根据当前的执行结果，判断是否需要告警
     * @param success api调用结果
     */
    public void checkAndSendAlert(boolean success){
        insertElem(success);
        if(needAlert()){
            dingdingAlert();
        }
    }

    private void dingdingAlert(){
        String msg = "已经连续十次无法获取返回信息,请及时处理 ...";
        dingdingAlert(msg);
    }

    /**
     * 发送钉钉告警
     * @param msg 告警的消息详情
     */
    private void dingdingAlert(String msg){
        if(StringUtils.isNotBlank(msg)){
            JSONObject contentObj = new JSONObject();
            contentObj.put("content",msg);
            JSONObject obj = new JSONObject();
            obj.put("msgtype","text");
            obj.put("markdown",contentObj);
            obj.put("isAtAll",true);
            JSONObject postResult = restTemplate.postForObject(alertUrl, obj, JSONObject.class);
            if(postResult.getInteger("errcode").equals(0)){
                log.info("发送告警成功");
            }
        }
    }

    /**
     * 队列添加调用结果信息
     * @param success api调用结果
     */
    private synchronized void insertElem(boolean success){
        if(resQueue.size() < alertThreshold){
            resQueue.add(success);
        }else {
            resQueue.poll();
            resQueue.offer(success);
        }
    }

    /**
     * 判断是否需要告警
     * @return
     */
    private synchronized boolean  needAlert(){
        if(resQueue.size() < 10){
            return false;
        }
        Iterator<Boolean> iterator = resQueue.iterator();
        while (iterator.hasNext()){
            if( iterator.next()){
                return false;
            }
        }
        return true;
    }
}
