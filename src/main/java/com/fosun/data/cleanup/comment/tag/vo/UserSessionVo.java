package com.fosun.data.cleanup.comment.tag.vo;

import lombok.Data;

/**
 * @author yuanfeiyang
 * @Title: UserSessionVo
 * @ProjectName boss-server
 * @Description: TODO
 * @date 2018/8/3019:54
 */

@Data
public class UserSessionVo {

    /**
     * token
     */
    private String token;

    /**
     * 账户id
     */
    private String userName;
}
