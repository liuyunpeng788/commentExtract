package com.fosun.data.cleanup.comment.tag.dto.po.db;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zyf
 * @description: 购物中心业态表
 */
@Table(name = "t_mall_class")
@Entity
public class MallClassPo {
    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long formatId;

    /**
     * 业态名称
     */
    private String formatName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 
     * @return format_id 
     */
    public Long getFormatId() {
        return formatId;
    }

    /**
     * 
     * @param formatId 
     */
    public void setFormatId(Long formatId) {
        this.formatId = formatId;
    }

    /**
     * 业态名称
     * @return format_name 业态名称
     */
    public String getFormatName() {
        return formatName;
    }

    /**
     * 业态名称
     * @param formatName 业态名称
     */
    public void setFormatName(String formatName) {
        this.formatName = formatName == null ? null : formatName.trim();
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}