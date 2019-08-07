package com.fosun.data.cleanup.comment.tag.dto.po.db;

import lombok.Data;

import javax.persistence.*;

/**
 * 店铺业态信息
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/4/3 18:27
 * @description: TODO
 * @modified: TODO
 * @version: 1.0
 **/
@Entity
@Data
@Table(name = "t_shop_class")
public class ShopClassPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 父级i
     */
    private Integer parentId;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 名字
     */
    private String name;

    /**
     * 百度type id
     */
    private Integer baiduTypeId;

    /**
     * 百度的类型名称
     */
    private String baiduTypeName;


}
