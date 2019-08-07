package com.fosun.data.cleanup.comment.tag.enums;

/**
 * @author: zyf
 * @description: 好差评
 * @date: 2019/4/8
 */
public enum CommentScoreTypeEnum {

    /**
     * 差评
     */
    NEGATIVE((short) 0,"差评"),

    /**
     * 好评
     */
    FAVOURABLE((short) 1,"好评");


    private Short value;

    private String des;

    CommentScoreTypeEnum(Short value,String des){
        this.value = value;
        this.des = des;
    }

    public Short getValue() {
        return value;
    }

    public String getDes() {
        return des;
    }

}
