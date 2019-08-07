package com.fosun.data.cleanup.comment.tag.exception;


 import com.fosun.data.cleanup.comment.tag.vo.ResponseVo;
 import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 统一异常处理
 * @author <a href="mailto:liumch@fosun.com">刘梦超</a>
 * @date 2019/3/26 14:10
 * @description: TODO
 * @modified: TODO
 * @version:
**/
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    /**
     * 业务异常
     *
     * @param bizException
     * @return
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public ResponseEntity<ResponseVo> handleBizException(BizException bizException) {
        log.error("业务异常:", bizException);
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(false);
        responseVo.setErrorCode(bizException.getErrorCode());
        responseVo.setMsg(bizException.getMessage());
        return new ResponseEntity(responseVo, HttpStatus.OK);
    }

    /**
     * 参数校验异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseVo> handleBindException(MethodArgumentNotValidException e) {
        log.error("方法参数校验异常:", e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(false);
        responseVo.setErrorCode(ErrorCode.ARGS_ERROR);
        responseVo.setMsg(fieldError.getField() + fieldError.getDefaultMessage());
        return new ResponseEntity(responseVo, HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数校验异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseVo> handleBindException(BindException e) {
        log.error("绑定参数校验异常:", e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(false);
        responseVo.setErrorCode(ErrorCode.ARGS_ERROR);
        responseVo.setMsg(fieldError.getDefaultMessage());
        return new ResponseEntity(responseVo, HttpStatus.BAD_REQUEST);
    }


    /**
     * 错误的请求
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseVo> handleValidationException(HttpMessageNotReadableException e) {
        log.error("入参转换失败", e);
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(false);
        responseVo.setErrorCode(ErrorCode.ARGS_ERROR);
        responseVo.setMsg("错误的请求，请检查入参是否正确");
        return new ResponseEntity(responseVo, HttpStatus.BAD_REQUEST);
    }

    /**
     * 其他异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ResponseVo> handleOtherException(Exception e) {
        log.error("系统未知异常:", e);
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(false);
        responseVo.setErrorCode(ErrorCode.UNKNOWN_ERROR);
        responseVo.setMsg("系统未知异常:" + e.getMessage());
        return new ResponseEntity(responseVo, HttpStatus.OK);
    }
}
