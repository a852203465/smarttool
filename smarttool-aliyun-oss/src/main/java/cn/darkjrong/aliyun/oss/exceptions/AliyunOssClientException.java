package cn.darkjrong.aliyun.oss.exceptions;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 阿里云OSS客户端异常
 *
 * @author Rong.Jia
 * @date 2024/08/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AliyunOssClientException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -1501020198729282518L;

    public AliyunOssClientException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public AliyunOssClientException(String message) {
        super(message);
    }

    public AliyunOssClientException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public AliyunOssClientException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AliyunOssClientException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public AliyunOssClientException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }

}
