package com.fosun.data.cleanup.comment.tag.controller;

import com.fosun.data.cleanup.comment.tag.utils.ThreadPoolUtil;
import com.fosun.data.cleanup.comment.tag.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 调用百度api提取关键词的任务
 * @author: liumch
 * @create: 2019/7/26 10:33
 **/
@RestController
@Slf4j
@RequestMapping(value = "/feedback")
public class KeywordTaskController {

    @Autowired
    private ThreadPoolUtil threadPoolUtil;
    @Autowired
    private RedisTemplate  redisTemplate;

    @GetMapping(value ="/keyword/{type}")
    @ResponseBody
    public ResponseVo<String> extractKeywords(@PathVariable("type") int type,  Boolean ignoreTodayFlag,String strStartDate,String strEndDate){
        String msg = "收到关键字抽取请求";

        threadPoolUtil.getPoolService().submit( new KeywordTaskService(redisTemplate,type,null == ignoreTodayFlag?false:ignoreTodayFlag,strStartDate,strEndDate));
        return new ResponseVo<String>().success(msg);
    }
}
