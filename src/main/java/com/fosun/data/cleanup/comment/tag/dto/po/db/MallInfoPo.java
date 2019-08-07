package com.fosun.data.cleanup.comment.tag.dto.po.db;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/** 商场信息
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/3/28 16:16
 * @description: 商场信息详情
 * @modified: TODO
 * @version: 1.0
 **/

@Entity
@Data
@Table(name="t_mall_info")
public class MallInfoPo {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商场id
     */
    private Long mallId;

    /**
     * 商场名称
     */
    private String mallName;

    /**
     * 商场星级
     */
    private BigDecimal mallLevel;

    /**
     * 商场业态 (关联商铺业态表)
     */
    private Long mallFormat;

    /**
     * 商场联系电话
     */
    private String mallTel;

    /**
     * 营业日
     */
    private String businessOpenday;

    /**
     * 营业开始时间
     */
    private String businessStart;

    /**
     * 营业结束时间
     */
    private String businessEnd;

    /**
     * 24小时营业标识 1:是 0:否
     */
    private Byte openAllday;

    /**
     * 整周营业标识 1:是 0:否
     */
    private Byte openAllweek;

    /**
     * 商场地址
     */
    private String address;

    /**
     * 商场经度
     */
    private BigDecimal longitude;

    /**
     * 商场纬度
     */
    private BigDecimal latitude;

    /**
     * 位置code
     */
    private String geoCode;

    /**
     * 所属区域
     */
    private String parkId;

    /**
     * 所属省份
     */
    private String provinceId;

    /**
     * 所属城市
     */
    private String cityId;

    /**
     * 商场所在行政区 (关联地区表)
     */
    private String citySubId;

    /**
     * 商场所在商圈 (关联商圈表)
     */
    private String districtId;

    /**
     * 商场评论数
     */
    private Long comments;

    /**
     * 人均消费
     */
    private BigDecimal consumptionPer;

    /**
     * 营业状态
     */
    private Integer businessStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 爬取时间
     */
    private Date updateTime;

}
