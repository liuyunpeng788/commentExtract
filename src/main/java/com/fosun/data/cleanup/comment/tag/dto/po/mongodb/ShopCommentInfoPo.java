package com.fosun.data.cleanup.comment.tag.dto.po.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 店铺点评实体对象
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/3/27 19:46
 * @description: 店铺点评
 * @modified: TODO
 * @version:
**/

@Data
@Document(collection = "Shop_Comment_Info")
public class ShopCommentInfoPo implements Serializable {

    /**
     * 评论id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     *    评论id
     */
    private String id;

    private String commentId;
    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户等级 ，如： lv7
     */
    private String userLevel;

    /**
     * 是否为vip, false： 不是vip
     */
    private Boolean vipFlag;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 人均消费
     */
    private Double avgConsumption;

    /**
     * 评论评分/星级
     */
    private Integer commentLevel;

    /**
     * 评论项列表
     */
    private List<ScoreItemPo> scoreItemPos;

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