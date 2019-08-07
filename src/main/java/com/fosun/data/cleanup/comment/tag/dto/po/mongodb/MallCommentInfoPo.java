package com.fosun.data.cleanup.comment.tag.dto.po.mongodb;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/** 商场评论信息
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/3/27 20:05
 * @description: 商场评论
 * @modified: TODO
 * @version: 1.0
 **/
@Data
@Document(collection = "Mall_Comment_Info")
public class MallCommentInfoPo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     *    评论id
     */
    private String id;

    private String commentId;
    /**
     *   商场id
     */
    private Long mallId;

    /**
     * 商场名称
     */
    private String mallName;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户等级 如 Iv7
     */
    private String userLevel;

    /**
     * 评论评分/评星
     */
    private Integer commentLevel;

    /**
     * 设施评分
     */
    private Double facilityScore;

    /**
     * 环境评分
     */
    private Double envScore;

    /**
     * 服务评分
     */
    private Double serviceScore;

    /**
     * 评论数
     */
    private Integer commentNum;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 获赞数
     */
    private Integer favoriteNum;

    /**
     * 回应数
     */
    private Integer replyNum;

    /**
     * 收藏数
     */
    private Integer collectNum;

    /**
     * 投诉数
     */
    private Integer explainNum;

    /**
     * 商家回应内容
     */
    private String merchantReplyContent;

    /**
     * 商家回应时间
     */
    private Date merchantReplyTime;

    /**
     * 高亮的评论
     */
    private String highlightContent;

    /**
     * 评论的标签
     */
    private List<String> commentTag;

    /**
     * 爬取时间
     */
    private Date crawlingTime;

    /**
     * 创建时间
     */
    private Date createTime;



}
