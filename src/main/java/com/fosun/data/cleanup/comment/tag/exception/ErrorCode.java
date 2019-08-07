package com.fosun.data.cleanup.comment.tag.exception;

/**
 * 错误码
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/3/26 14:11
 * @description: TODO
 * @modified: TODO
 * @version:
**/


public class ErrorCode {

    /**
     * 成功
     */
    public static final int SUCCESS = 1000;

    /**
     * 用户密码验证失败
     */
    public static final int ERROR_AUTH = 4001;

    /**
     * 未知系统异常
     */
    public static final int UNKNOWN_ERROR = 5000;

    /**
     * 入参错误
     */
    public static final int ARGS_ERROR = 5001;

    /**
     * 业务异常
     */
    public static final int BIZ_ERROR = 5002;

    /**
     * 无效得用户
     */
    public static final int USER_NOT_FOUND_ERROR = 5003;

    /**
     * 操作过于频繁
     */
    public static final int REQUEST_LIMIT_ERROR = 5004;
}
