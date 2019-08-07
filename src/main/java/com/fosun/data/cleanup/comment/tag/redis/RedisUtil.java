package com.fosun.data.cleanup.comment.tag.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.fosun.data.cleanup.comment.tag.constant.Const.REDIS_PREFIX;

/**
 * @author: liumch
 * @create: 2019/7/18 14:40
 **/

@Component(value = "RedisUtil")
public class RedisUtil {

    private static final String REQ_LOCK = REDIS_PREFIX + "ApiReqLock";
    private static final Long REDIS_TIMEOUT = 300L;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${baidu_api.sleep}")
    private Long sleepTime;

    /**
     * 锁是否存在
     * @return true 如果锁存在
     */
    public boolean existReqLock(){
        if(redisTemplate.hasKey(REQ_LOCK) && StringUtils.isNotBlank(redisTemplate.opsForValue().get(REQ_LOCK))){
            return true;
        }
        return false;
    }

    /**
     * 等待超时释放锁.如果锁存在，则等待2s
     */
    public void waitReqUnlock(){
        while(true){
            if(!existReqLock()){
                break;
            }
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置锁和超时时间
     */
    public void setReqLock(){
        redisTemplate.opsForValue().set(REQ_LOCK,"1",sleepTime == null ? REDIS_TIMEOUT : sleepTime , TimeUnit.MILLISECONDS);
//        redisTemplate.expire(REQ_LOCK,REDIS_TIMEOUT, TimeUnit.SECONDS);
    }

}
