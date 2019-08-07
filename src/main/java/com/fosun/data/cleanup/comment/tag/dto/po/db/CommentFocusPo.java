package com.fosun.data.cleanup.comment.tag.dto.po.db;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/10 14:21
 * @description: TODO
 * @modified: TODO
 * @version: 1.0
 **/
@Data
@Entity
@Table(name = "t_consumer_comment_focus")
public class CommentFocusPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 主体id. 可以是商场，也可以是店铺
     */
    private Long  subjectId;
    /**
     * 主体类型。 0：商场，1：店铺
     */
    private Short subjectType;
    /**
     * 评论类型    0：坏评  1：好评
     */
    private Short commentType;
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 关键词数量
     */
    private Integer num;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
