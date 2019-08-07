package com.fosun.data.cleanup.comment.tag.dto.po.db;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 重点关注的商场
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/1 11:05
 * @description: 重点关注的商场
 * @modified: TODO
 * @version: 1.0
 **/
@Data
@Entity
@Table(name = "t_mall_focus")
public class MallFocusPo {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 主体商场id
     */
    private Long srcMallId;

    /**
     * 重点关注的商场id
     */
    private Long mallId;

    /**
     * 商场名称
     */
    private String mallName;

    /**
     * 状态。-1 删除  1：有效
     */
    private Short status;

    /**
     * 创建人id
     */
    private Long creator;

    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}