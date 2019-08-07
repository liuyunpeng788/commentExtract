package com.fosun.data.cleanup.comment.tag.vo;

 import com.fosun.data.cleanup.comment.tag.exception.ErrorCode;
 import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 公共返回视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ResponseVo<T> implements Serializable {

    /**
     * 默认成功
     */
    @ApiModelProperty("请求是否成功(true:成功 false:失败)")
    private boolean status = true;

    /**
     * 请求返回码:0表示成功
     */
    @ApiModelProperty("请求返回码:0表示成功")
    private Integer errorCode = ErrorCode.SUCCESS;

    /**
     * 请求返回说明
     */
    @ApiModelProperty("请求返回说明")
    private String msg;

    /**
     * 请求返回数据
     */
    @ApiModelProperty("请求返回数据")
    private T data;

    private ResponseVo(Integer error,String msgs,T data){
        this.errorCode = error;
        this.msg = msgs;
        this.data = data;
    }

    /**
     * 构造成功实例
     * @param data
     * @return
     */
    public  ResponseVo<T> success(T data){
        return new ResponseVo<>(ErrorCode.SUCCESS,"请求成功",data);
    }

    /**
     * 构造失败实例
     * @param error
     * @param msgs
     * @return
     */
    public  ResponseVo<T> fail(Integer error,String msgs){
        return new ResponseVo<>(error,msgs,null);
    }
}
