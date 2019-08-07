package com.fosun.data.cleanup.comment.tag.dto.po.mongodb;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Set;

/**
 * 评论的观点抽取标签信息
 * @author: liumch
 * @create: 2019/7/18 17:15
 **/

@Data
@Document(collection = "Comment_Tag_Info")
@Getter
@Setter
public class MongoCommentTagInfoPo implements Serializable {
    /**
     * 观点抽取标签
     */
    private Set<String> tag;
    /**
     * 百度业态类型 ，一共13种
     */
    private Integer bizType;
    /**
     * 标签来源 0：商场 1：店铺
     */
    private Integer source;
}
