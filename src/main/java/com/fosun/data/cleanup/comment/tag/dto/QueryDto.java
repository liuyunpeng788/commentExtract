package com.fosun.data.cleanup.comment.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: zyf
 * @description: 查询
 * @date: 2019/4/2
 */
@Data
@ApiModel(value = "QueryDto",description = "查询参数")
public class QueryDto extends PageDto{

    @ApiModelProperty(name = "mallName", value = "商场名称")
    private String mallName;

    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;

    @ApiModelProperty(name = "districtId", value = "行政区id")
    private String districtId;

    @ApiModelProperty(name = "brandName", value = "品牌名称")
    private String brandName;

    /**
     * 距离
     */
    @ApiModelProperty(name = "distribution", value = "距离")
    private Integer distribution;

    /**
     * 开始月份
     */
    @ApiModelProperty(name = "startMonth", value = "开始月份")
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date  startMonth;

    /**
     * 结束月份
     */
    @ApiModelProperty(name = "endMonth", value = "结束月份")
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date  endMonth;

    /**
     * 排序类型
     */
    @ApiModelProperty(name = "sortType", value = "排序类型 0:开始时间正序 1:结束时间倒序")
    private Integer sortType = 0;

    /**
     * 评分类型
     */
    @ApiModelProperty(name = "commentScoreType", value = "评分类型 0:差评 1:好评")
    private Integer commentScoreType;

    /**
     * 评论类型
     */
    @ApiModelProperty(name = "commentType", value = "评论类型 1:设施 2:环境 3:服务 ")
    private Integer commentType;

    @ApiModelProperty(name = "startDateTime", value = "开始时间 时 分 秒")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  startDateTime;

    /**
     * 结束月份
     */
    @ApiModelProperty(name = "endDateTime", value = "结束时间 时 分 秒")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  endDateTime;

}
