package com.fosun.data.cleanup.comment.tag.dto.po.mongodb;

import lombok.Data;

/**
 * 店铺具体评论项
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/18 11:00
 * @description: TODO
 * @modified: TODO
 * @version: 1.0
 **/
@Data
public class ScoreItemPo {
    /**
     * 评分类型id
     */
    private Long scoreId;

    /**
     * 评分类型名称
     */
    private String scoreName;

    /**
     * 评分值
     */
    private Double value;
}
