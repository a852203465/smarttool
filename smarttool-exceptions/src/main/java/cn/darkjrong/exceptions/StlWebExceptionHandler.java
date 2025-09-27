package cn.darkjrong.exceptions;

import cn.darkjrong.core.exceptions.StlException;
import cn.darkjrong.core.exceptions.StlRuntimeException;
import cn.darkjrong.core.exceptions.StlStatefulException;
import cn.darkjrong.core.exceptions.StlWebException;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.darkjrong.core.enums.ErrorEnum;
import cn.darkjrong.core.domain.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 异常控制处理器
 *
 * @author Rong.Jia
 * @date 2019/4/3
 */
@Slf4j
@SuppressWarnings("ALL")
@RestControllerAdvice
public class StlWebExceptionHandler {

    /**
     * 捕获违反约束异常，并返回异常数据
     *
     * @param e e
     * @return {@link ResponseVO}
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseVO constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> errorList = new LinkedList<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            log.error("ConstraintViolationException : {} - {} - {}",
                    constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getInvalidValue(),
                    constraintViolation.getMessage());
            errorList.add(constraintViolation.getMessage());
        }

        log.error("constraintViolationExceptionHandler :{}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.PARAMETER_ERROR.getCode(), errorList.get(0));
    }

    /**
     * 捕获自定义异常，并返回异常数据
     *
     * @param e e
     * @return {@link ResponseVO}
     */
    @ExceptionHandler(value = StlWebException.class)
    public ResponseVO stlWebExceptionHandler(StlWebException e) {
        log.error("stlWebExceptionHandler  {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(e.getCode(), e.getMessage());
    }

    /**
     * 捕获全局异常，并返回异常数据
     *
     * @param e e
     * @return {@link ResponseVO}
     */
    @ExceptionHandler(value = StlException.class)
    public ResponseVO stlExceptionHandler(StlException e) {
        log.error("stlExceptionHandler  {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.ERROR.getCode(), e.getMessage());
    }

    /**
     * 捕获全局异常，并返回异常数据
     *
     * @param e e
     * @return {@link ResponseVO}
     */
    @ExceptionHandler(value = StlRuntimeException.class)
    public ResponseVO stlRuntimeExceptionHandler(StlRuntimeException e) {
        log.error("stlRuntimeExceptionHandler  {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.ERROR.getCode(), e.getMessage());
    }

    /**
     * 捕获有状态异常，并返回异常数据
     *
     * @param e e
     * @return {@link ResponseVO}
     */
    @ExceptionHandler(value = StlStatefulException.class)
    public ResponseVO stlStatefulExceptionHandler(StlStatefulException e) {
        log.error("stlStatefulExceptionHandler  {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(e.getStatus(), e.getMessage());
    }

    /**
     * 捕获缺失参数异常，并返回异常数据
     *
     * @param e e 异常
     * @return {@link ResponseVO}
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseVO missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("missingServletRequestParameterException {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.LACK_OF_PARAMETER);
    }


    /**
     * 捕获不合法的参数异常，并返回异常数据
     *
     * @param e e
     * @return {@link ResponseVO}
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseVO illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error("illegalArgumentExceptionHandler  {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.PARAMETER_ERROR.getCode(), e.getMessage());
    }

    /**
     * 捕捉404异常
     *
     * @param e 404 异常
     * @return {@link ResponseVO}
     * @date 2019/04/17 09:46:22
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseVO noHandlerFoundHandle(NoHandlerFoundException e) {
        log.error("noHandlerFoundHandle {}", ExceptionUtil.stacktraceToString(e));
        return new ResponseVO(ErrorEnum.NOT_FOUND);
    }

    /**
     * 处理
     * 字段验证异常处理
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVO methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        List<String> errorList = new LinkedList<>();
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        for (ObjectError error : errors) {
            log.error("MethodArgumentNotValidException : {} - {}", error.getObjectName(), error.getDefaultMessage());
            errorList.add(error.getDefaultMessage());
        }
        log.error("methodArgumentNotValidExceptionHandle :{}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.PARAMETER_ERROR.getCode(), errorList.get(0));
    }

    /**
     * 字段验证异常处理
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResponseVO bindExceptionHandle(BindException e) {
        List<String> errorList = new LinkedList<>();
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        for (ObjectError error : errors) {
            log.error("BindException : {} - {}", error.getObjectName(), error.getDefaultMessage());
            errorList.add(error.getDefaultMessage());
        }
        log.error("bindExceptionHandle :{}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.PARAMETER_ERROR.getCode(), errorList.get(0));
    }

    /**
     * 参数类型不正确异常
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseVO methodArgumentTypeMismatchExceptionException(MethodArgumentTypeMismatchException e) {
        log.error("methodArgumentTypeMismatchExceptionException : {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.THE_PARAMETER_TYPE_IS_INCORRECT);
    }

    /**
     * 不支持当前请求方法
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseVO handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException : {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.REQUEST_MODE_ERROR);
    }

    /**
     * 不支持当前媒体类型
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseVO handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error("handleHttpMediaTypeNotSupportedException : {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.MEDIA_TYPE_NOT_SUPPORTED);
    }

    /**
     * 默认异常处理
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseVO defaultErrorHandler(Exception e) {

        if (e.getCause() instanceof StlWebException) {
            stlWebExceptionHandler((StlWebException) e);
        }

        if (e.getCause() instanceof IllegalArgumentException) {
            illegalArgumentExceptionHandler((IllegalArgumentException) e.getCause());
        }

        if (e.getCause() instanceof MethodArgumentTypeMismatchException) {
            methodArgumentTypeMismatchExceptionException((MethodArgumentTypeMismatchException) e.getCause());
        }

        if (e.getCause() instanceof MissingServletRequestParameterException) {
            missingServletRequestParameterExceptionHandler((MissingServletRequestParameterException) e.getCause());
        }

        if (e.getCause() instanceof ConstraintViolationException) {
            constraintViolationExceptionHandler((ConstraintViolationException) e.getCause());
        }

        if (e.getCause() instanceof StlStatefulException) {
            stlStatefulExceptionHandler((StlStatefulException) e.getCause());
        }

        log.error("defaultErrorHandler : {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 默认运行异常处理
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ResponseVO runtimeErrorHandler(RuntimeException e) {

        if (e.getCause() instanceof StlWebException) {
            stlWebExceptionHandler((StlWebException) e);
        }

        if (e.getCause() instanceof HttpMessageNotReadableException) {
            httpMessageNotReadableHandler((HttpMessageNotReadableException) e.getCause());
        }

        if (e.getCause() instanceof MultipartException) {
            multipartExceptionHandler((MultipartException) e.getCause());
        }

        if (e.getCause() instanceof IllegalArgumentException) {
            illegalArgumentExceptionHandler((IllegalArgumentException) e.getCause());
        }

        if (e.getCause() instanceof MethodArgumentTypeMismatchException) {
            methodArgumentTypeMismatchExceptionException((MethodArgumentTypeMismatchException) e.getCause());
        }

        if (e.getCause() instanceof ConstraintViolationException) {
            constraintViolationExceptionHandler((ConstraintViolationException) e.getCause());
        }

        if (e.getCause() instanceof StlStatefulException) {
            stlStatefulExceptionHandler((StlStatefulException) e.getCause());
        }

        log.error("runtimeErrorHandler : {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 文件上传异常处理
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseVO maxUploadHandler(MaxUploadSizeExceededException e) {
        log.error("MaxUploadSizeExceededException : {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.FILE_LIMIT_EXCEEDED);
    }

    /**
     * 使用反射或者代理造成的异常需要根据异常类型单独处理
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(value = UndeclaredThrowableException.class)
    @ResponseBody
    public ResponseVO undeclaredThrowableException(HttpServletRequest req, UndeclaredThrowableException e) {

        //密文解密失败异常
        if (e.getCause() instanceof BadPaddingException) {

            log.error("BadPaddingException : {}", ExceptionUtil.stacktraceToString(e));
            return ResponseVO.error(ErrorEnum.SYSTEM_ERROR);
        }

        log.error("undeclaredThrowableException : {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.SYSTEM_ERROR);
    }

    /**
     * http 请求参数格式错误
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseVO httpMessageNotReadableHandler(HttpMessageNotReadableException e) {
        log.error("httpMessageNotReadableHandler : {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.REQUEST_PARAMETER_FORMAT_IS_INCORRECT);
    }

    /**
     * 超出最大限制捕获异常
     *
     * @param e 异常信息
     * @return ResponseVO 返回异常信息
     */
    @ExceptionHandler(value = MultipartException.class)
    @ResponseBody
    public ResponseVO multipartExceptionHandler(MultipartException e) {
        log.error("multipartExceptionHandler : {}", ExceptionUtil.stacktraceToString(e));
        return ResponseVO.error(ErrorEnum.FILE_LIMIT_EXCEEDED);

    }




}
