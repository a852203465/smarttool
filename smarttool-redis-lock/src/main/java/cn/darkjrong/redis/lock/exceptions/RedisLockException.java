package cn.darkjrong.redis.lock.exceptions;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * redis锁异常
 *
 * @author Rong.Jia
 * @date 2023/10/11
 */
public class RedisLockException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -2506514246646665562L;

    public RedisLockException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public RedisLockException(String message) {
        super(message);
    }

    public RedisLockException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public RedisLockException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RedisLockException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public RedisLockException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }

}
