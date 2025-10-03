package cn.darkjrong.redis.limit.exceptions;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * redis限流异常
 *
 * @author Rong.Jia
 * @date 2023/10/11
 */
public class RedisLimitException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -2506514246646665562L;

    public RedisLimitException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public RedisLimitException(String message) {
        super(message);
    }

    public RedisLimitException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public RedisLimitException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RedisLimitException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public RedisLimitException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }

}
