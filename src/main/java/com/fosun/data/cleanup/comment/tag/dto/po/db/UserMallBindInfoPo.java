package com.fosun.data.cleanup.comment.tag.dto.po.db;

import javax.persistence.*;
import java.util.Date;

/**
  * @date 2019/03/28
 * 用户商场绑定类
 */
@Table(name = "t_user_mall_bind_info")
@Entity
public class UserMallBindInfoPo  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * xms的用户id
     */
    private Integer userId;

    /**
     * 商场id
     */
    private Long mallId;

    /**
     * 创建人
     */
    private Integer creatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Integer updatorId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * xms的用户id
     * @return user_id xms的用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * xms的用户id
     * @param userId xms的用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 商场id
     * @return mall_id 商场id
     */
    public Long getMallId() {
        return mallId;
    }

    /**
     * 商场id
     * @param mallId 商场id
     */
    public void setMallId(Long mallId) {
        this.mallId = mallId;
    }

    /**
     * 创建人
     * @return creator_id 创建人
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * 创建人
     * @param creatorId 创建人
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
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

    /**
     * 修改人
     * @return updator_id 修改人
     */
    public Integer getUpdatorId() {
        return updatorId;
    }

    /**
     * 修改人
     * @param updatorId 修改人
     */
    public void setUpdatorId(Integer updatorId) {
        this.updatorId = updatorId;
    }

    /**
     * 修改时间
     * @return update_time 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 修改时间
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}