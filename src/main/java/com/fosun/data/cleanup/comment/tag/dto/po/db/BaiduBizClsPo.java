package com.fosun.data.cleanup.comment.tag.dto.po.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 百度一级业态与大众点评映射实体类
 * @author: liumch
 * @create: 2019/7/17 16:42
 **/

@Entity
@Table(name = "t_baidu_dazhong_biz_relation")
@Getter
@Setter
public class BaiduBizClsPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 大众一级业态id
     */
    private Integer dzId;
    /**
     * 大众业态名称
     */
    private String dzName;
    /**
     * 百度业态id
     */
    private Integer baiduId;
    /**
     * 百度业态名称
     */
    private String baiduName;
}
