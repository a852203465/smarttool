package cn.darkjrong.core.exceptions;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 工具类全局运行时异常
 *
 * @author Rong.Jia
 * @date 2023/06/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StlRuntimeException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -1501020198729282518L;

    public StlRuntimeException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public StlRuntimeException(String message) {
        super(message);
    }

    public StlRuntimeException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public StlRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public StlRuntimeException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public StlRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }


}
