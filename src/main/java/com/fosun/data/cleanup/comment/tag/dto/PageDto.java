package com.fosun.data.cleanup.comment.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: zyf
 * @description: 分页查询
 * @date: 2019/4/1
 */
@Data
@ApiModel(value = "PageDto",description = "分页查询参数")
public class PageDto  extends BaseDto {

    @ApiModelProperty(name = "mallId", value = "查询商场id")
    private Long mallId;

    @ApiModelProperty(name = "startTime", value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty(name = "endTime", value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 页码
     */
    @ApiModelProperty(name = "pageNum", value = "页码")
    private Integer pageNum;

    /**
     * 数量
     */
    @ApiModelProperty(name = "pageSize", value = "数量")
    private Integer pageSize;

}
