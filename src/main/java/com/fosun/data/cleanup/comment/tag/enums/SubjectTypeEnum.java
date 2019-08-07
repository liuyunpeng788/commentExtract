package com.fosun.data.cleanup.comment.tag.enums;

/**
 * @author: zyf
 * @description:
 * @date: 2019/4/1
 */
public enum SubjectTypeEnum {

    /**
     * 商场
     */
    MALL((short) 0,"商场"),

    /**
     * 店铺
     */
    SHOP((short) 1,"店铺");


    private Short value;

    private String des;

    SubjectTypeEnum(Short value,String des){
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
