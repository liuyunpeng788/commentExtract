package com.fosun.data.cleanup.comment.tag.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static com.fosun.data.cleanup.comment.tag.constant.Const.API_ALERT_KEY;

/**
 * @author: liumch
 * @create: 2019/8/14 16:48
 **/
@Component
@Slf4j
public class HttpUtil {

    @Value("{$alert_threshold}")
    private Integer alertThreshold;

    /**
     * 钉钉告警的url
     */
    @Value("${alert_url}")
    private String alertUrl;

    /**
     * 两次api请求调用的间隔时间
     */
    @Value("${baidu_api.sleep}")
    private Integer sleep;
    /**
     * 是否开启告警
     */

    @Value("{open_alert}")
    private Boolean openAlert;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 发送请求
     * @param url 请求的url
     * @param httpEntity http 请求报文
     * @return 请求的结果
     */
    public JSONObject sendReq(String url,final HttpEntity<MultiValueMap<String, Object>> httpEntity){
        ResponseEntity<JSONObject> response = null ;
        boolean success = false;
        JSONObject obj = new JSONObject();
        for(int i = 0;i<3; i++){
            try {
                response = restTemplate.postForEntity( url ,httpEntity, JSONObject.class);
                Thread.sleep(sleep);
                if(response.getStatusCode().equals(HttpStatus.OK)){
                    log.info("调用百度api请求成功。res: " + response.toString());
                    success = true;
                    obj = response.getBody();
                    break;
                }
            } catch (Exception e) {
                log.error("调用api请求发生异常,返回的结果：{}，异常详情：{}",response.toString(), e.getMessage());
            }
        }
        dealAlert(success);
        return obj;
    }

    private void dealAlert(boolean success){
        if(null != openAlert && openAlert){
            if(success){
                redisTemplate.delete(API_ALERT_KEY);
            }else if(!redisTemplate.hasKey(API_ALERT_KEY)){
                redisTemplate.boundValueOps(API_ALERT_KEY).set("1",1, TimeUnit.HOURS);
            }else{
                Integer errTimes = Integer.valueOf(redisTemplate.opsForValue().get(API_ALERT_KEY));
                if (errTimes >= alertThreshold){
                    //钉钉告警
                    dingdingAlert();
                    redisTemplate.delete(API_ALERT_KEY);
                }else{
                    redisTemplate.boundValueOps(API_ALERT_KEY).increment(1);
                }
            }
        }
    }

    private void dingdingAlert(){
        String msg = String.format("已经连续 %d 次无法获取百度API接口返回信息,请及时处理 ...", alertThreshold);
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
                log.info("发送钉钉告警成功");
            }
        }
    }
}
