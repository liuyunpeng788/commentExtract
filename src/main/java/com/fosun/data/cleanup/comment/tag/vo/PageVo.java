package com.fosun.data.cleanup.comment.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PageVo", description = "分页信息")
public class PageVo<T> implements Serializable {
    @ApiModelProperty(name = "pageNum",value = "页码")
    private int pageNum;

    @ApiModelProperty(name = "pageSize" , value = "数量")
    private int pageSize;

    @ApiModelProperty(name = "total" , value = "总数")
    private long total;

    @ApiModelProperty(name = "totalPage",value = "总页数")
    private int totalPage;

    @ApiModelProperty(name = "list", value = "实体列表")
    private List<T> list;


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }


}