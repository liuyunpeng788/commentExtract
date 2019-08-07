package com.fosun.data.cleanup.comment.tag.dto.po.mongodb;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 问询评论信息
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/3/27 20:47
 * @description: 问询评论
 * @modified: TODO
 * @version: 1.0
 **/
@Data
public class CommentInfoPo implements Serializable {

    private Long userId; //评论者id

    private String userName; //评论者用户名

    private String content; //评论内容

    private Date createTime; //评论时间

    private Integer commentReplyNum; //评论回复数

    private Integer commentFavoriteNum; //评论获赞数

}
