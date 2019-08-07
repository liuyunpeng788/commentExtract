package com.fosun.data.cleanup.comment.tag.exception;

/**
 * Description: 自定义业务异常
 *
 * @author: qiuxiao
 * @time: 2018/8/8 13:30
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 3469612794612543565L;

    /**
     * 错误异常码
     */
    private Integer errorCode;

    public BizException() {
    }

    public BizException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BizException(String message) {
        super(message);
        this.errorCode = ErrorCode.BIZ_ERROR;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
