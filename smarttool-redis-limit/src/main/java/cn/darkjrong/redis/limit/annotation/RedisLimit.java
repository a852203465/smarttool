package cn.darkjrong.redis.limit.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 限流
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLimit {

    /**
     * 锁的key前缀
     */
    String prefix() default "";

    /**
     * 锁名称
     */
    String name() default "";

    /**
     * 过期时间
     *
     * @return int
     */
    int expire() default 5;

    /**
     * 超时时间单位
     *
     * @return 秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 最多的访问限制次数
     *
     */
    long count() default 5;

    /**
     * 默认异常信息
     *
     * @return String
     */
    String message() default "你访问的接口已达到最大限流值,请降低访问频率";



}
