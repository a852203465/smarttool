package cn.darkjrong.redis.lock.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis锁注解
 *
 * @author Rong.Jia
 * @date 2023/10/11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RedisLock {

    /**
     * 锁的key前缀
     */
    String prefix() default "";

    /**
     * 锁名称
     */
    String name() default "";

    /**
     * 锁的等待时间, 单位：秒
     */
    int waitTime() default -1;

    /**
     * 释放的时间, 单位：秒
     */
    int leaseTime() default -1;

    /**
     * 时间单位, 默认：秒
     *
     * @return {@link TimeUnit}
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;












}
