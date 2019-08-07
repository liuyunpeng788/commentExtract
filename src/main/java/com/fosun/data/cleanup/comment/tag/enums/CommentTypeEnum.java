package com.fosun.data.cleanup.comment.tag.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论类型
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/9 14:36
 * @description: TODO
 * @modified: TODO
 * @version: 1.0
 **/

public enum CommentTypeEnum {
    SERVICE(1,"服务"),
    ENV(2,"环境"),
    FACILITY(3,"设施");

    private int index;
    private String value;
    CommentTypeEnum(int index,String value){
        this.index = index;
        this.value = value;
    }

    public int getIndex(){ return index;}
    public String getValue(){ return value;}

    public static String getValueByIndex(int index){
        for(CommentTypeEnum commentTypeEnum: CommentTypeEnum.values()){
            if(commentTypeEnum.getIndex() == index){
                return commentTypeEnum.getValue();
            }
        }
        return null;
    }

    public static List<String> getNames(){
        return Arrays.stream(values()).map(CommentTypeEnum::getValue).collect(Collectors.toList());
    }

}
