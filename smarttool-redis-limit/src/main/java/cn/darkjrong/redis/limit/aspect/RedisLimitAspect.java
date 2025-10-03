package cn.darkjrong.redis.limit.aspect;

import cn.darkjrong.redis.limit.RedisLimitTemplate;
import cn.darkjrong.redis.limit.annotation.RedisLimit;
import cn.darkjrong.redis.limit.exceptions.RedisLimitException;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * redis限流AOP
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Slf4j
@Aspect
@Component
public class RedisLimitAspect {

    @Autowired
    private RedisLimitTemplate redisLimitTemplate;

    /**
     * 切点
     */
    @Pointcut("@annotation(cn.darkjrong.redis.limit.annotation.RedisLimit)")
    public void point() {
    }

    @Around("point()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RedisLimit redisLimit = method.getAnnotation(RedisLimit.class);
        final long count = redisLimit.count();
        final long seconds = Expiration.from(redisLimit.expire(), redisLimit.timeUnit()).getExpirationTimeInSeconds();
        String key = getKey(redisLimit, pjp);
        try {
            final boolean acquire = redisLimitTemplate.tryAcquire(key, count, seconds);
            if (acquire) {
                return pjp.proceed();
            } else {
                throw new RedisLimitException(redisLimit.message());
            }
        } catch (Throwable e) {
            log.error(String.format("key【%s】An anomaly occurred in the rate limiting, 【%s】", key, e.getMessage()), e);
            throw new RedisLimitException(e.getLocalizedMessage());
        }
    }

    /**
     * 获取Key
     *
     * @param joinPoint 加入点
     * @param redisLimit redis限流
     * @return {@link String}
     */
    private String getKey(RedisLimit redisLimit, ProceedingJoinPoint joinPoint) {
        String prefix = redisLimit.prefix();
        String name = redisLimit.name();
        if (StrUtil.isNotBlank(prefix) && StrUtil.isNotBlank(name)) {
            return (StrUtil.endWith(prefix, StrUtil.COLON) ? prefix : (prefix + StrUtil.COLON)) + name;
        } else if (StrUtil.isNotBlank(prefix) && StrUtil.isBlank(name)) {
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            return (StrUtil.endWith(prefix, StrUtil.COLON) ? prefix : (prefix + StrUtil.COLON)) + className + StrUtil.DOT + methodName;
        } else if (StrUtil.isNotBlank(name) && StrUtil.isBlank(prefix)) {
            return name;
        } else {
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            return className + StrUtil.COLON + methodName;
        }
    }



}