package cn.darkjrong.redis.lock.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.darkjrong.core.lang.constants.NumberConstant;
import cn.darkjrong.redis.lock.exceptions.RedisLockException;
import cn.darkjrong.redis.lock.RedisLockTemplate;
import cn.darkjrong.redis.lock.annotation.RedisLock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Redis锁AOP
 *
 * @author Rong.Jia
 * @date 2023/10/11
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class RedisLockAspect {

    private final RedisLockTemplate redisLockTemplate;

    /**
     * 切点
     */
    @Pointcut("@annotation(cn.darkjrong.redis.lock.annotation.RedisLock)")
    public void point() {
    }

    @Around("point()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        String lockKey = getLockName(redisLock, joinPoint);

        int waitTime = redisLock.waitTime();
        int leaseTime = redisLock.leaseTime();
        TimeUnit timeUnit = redisLock.timeUnit();

        Lock lock = null;
        boolean isLock;
        if (waitTime > 0 && leaseTime > 0) {
            isLock = redisLockTemplate.tryLock(lockKey, timeUnit, waitTime, leaseTime);
        } else if (leaseTime > 0) {
            isLock = redisLockTemplate.tryLock(lockKey, timeUnit, NumberConstant.ZERO, leaseTime);
        } else if (waitTime > 0) {
            isLock = redisLockTemplate.tryLock(lockKey, timeUnit, waitTime);
        } else {
            lock = redisLockTemplate.lock(lockKey);
            isLock = Boolean.TRUE;
        }
        try {
            if (isLock) {
                return joinPoint.proceed();
            } else {
                log.error("【{}】获取分布式锁[异常]", lockKey);
                throw new RedisLockException(String.format("【%s】获取分布式锁[异常]", lockKey));
            }
        } finally {
            if (ObjectUtil.isNotNull(lock) && isLock) {
                lock.unlock();
            }
        }
    }

    /**
     * 获取锁名字
     *
     * @param joinPoint 加入点
     * @param redisLock redis锁
     * @return {@link String}
     */
    private String getLockName(RedisLock redisLock, ProceedingJoinPoint joinPoint) {
        String prefix = redisLock.prefix();
        String name = redisLock.name();
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
