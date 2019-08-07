package com.fosun.data.cleanup.comment.tag.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import static com.fosun.data.cleanup.comment.tag.constant.Const.REDIS_PREFIX;

/**
 * @author: liumch
 * @create: 2019/7/3 14:09
 **/
@Setter
@Getter
public class RedisDto {

    private BaseDto baseDto;
    private Integer dist;
    private String date;
    private Long classId;
    private Long subClassId;
    private Integer size;
    private Long srcMallId;

    public String buildRedisKey() {
        StringBuilder sb = new StringBuilder();
        if(null != baseDto.getSrcMallId()){
            sb.append(baseDto.getSrcMallId()).append("|");;
        }
        if(dist != null){
            sb.append(dist).append("|");
        }
        if(StringUtils.isNotBlank(date)){
            sb.append(date).append("|");
        }
        if(null != classId){
            sb.append(classId).append("|");
        }
        if(null != subClassId){
            sb.append(subClassId).append("|");
        }
        if(null != size){
            sb.append(size).append("|");
        }
        if(sb.length() > 1){
            //去掉最后一个"|"
            sb.setLength(sb.length() -1);
        }
        return REDIS_PREFIX + sb.toString();
    }

    public static class Build{
        private BaseDto baseDto;
        private Integer dist;
        private String date;
        private Long classId;
        private Long subClassId;
        private Integer size;
        private Long srcMallId;

        public  Build buildBaseDto(final BaseDto baseDto){
            this.baseDto = baseDto;
            return this;
        }
        public Build buildDist(Integer dist){
            this.dist = dist;
            return this;
        }
        public Build buildDate(String date){
            this.date =date;
            return this;
        }
        public Build buildClassId(Long classId){
            this.classId = classId;
            return this;
        }
        public Build buildSubClassId(Long subClassId){
            this.subClassId = subClassId;
            return this;
        }
        public Build buildSrcMallId(Long srcMallId){
            this.srcMallId = srcMallId;
            return this;
        }
        public Build buildSize(Integer size){
            this.size = size;
            return this;
        }

        public RedisDto build(){
            RedisDto redisDto = new RedisDto();
            redisDto.setBaseDto(baseDto);
            redisDto.setClassId(classId);
            redisDto.setSize(size);
            redisDto.setDate(date);
            redisDto.setDist(dist);
            redisDto.setSubClassId(subClassId);
            redisDto.setSrcMallId(srcMallId);
            return redisDto;
        }
    }
}
