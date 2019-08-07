package com.fosun.data.cleanup.comment.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: zyf
 * @description: 基础参数类
 * @date: 2019/3/29
 */
@Getter
@Setter
@ApiModel(value = "BaseDto",description = "基础参数")
public class BaseDto {
    /**
     * 城市id
     */
    @ApiModelProperty(name = "srcCityId", value = "城市id")
    private String srcCityId;

    /**
     * 主体商场id
     */
    @ApiModelProperty(name = "srcMallId", value = "主体商场id")
    private Long srcMallId;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "srcUserId", value = "用户id")
    private Integer srcUserId;

}
