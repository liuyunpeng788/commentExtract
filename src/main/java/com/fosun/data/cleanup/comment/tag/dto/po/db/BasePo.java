package com.fosun.data.cleanup.comment.tag.dto.po.db;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author: zyf
 * @description: 基础
 * @date: 2019/3/28
 */
@Data
@MappedSuperclass
public class BasePo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
