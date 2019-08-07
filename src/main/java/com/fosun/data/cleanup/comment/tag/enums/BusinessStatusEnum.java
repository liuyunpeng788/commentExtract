package com.fosun.data.cleanup.comment.tag.enums;

/**
 * @author: zyf
 * @description:
 * @date: 2019/4/1
 */
public enum BusinessStatusEnum {

    /**
     * 未开业
     */
    NOT_OPEN(-1,"未开业"),

    /**
     * 营业中
     */
    OPEN(0,"营业中"),

    /**
     * 已关闭
     */
    CLOSED(1,"已关闭");

    private Integer value;

    private String des;

    BusinessStatusEnum(Integer value,String des){
        this.value = value;
        this.des = des;
    }

    public Integer getValue() {
        return value;
    }

    public String getDes() {
        return des;
    }
}
