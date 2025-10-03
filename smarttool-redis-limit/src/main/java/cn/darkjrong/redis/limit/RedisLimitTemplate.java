package cn.darkjrong.redis.limit;


import cn.darkjrong.redis.RedisUtils;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.List;

/**
 * 基于 Redis 实现的分布式限流
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Slf4j
public class RedisLimitTemplate {

    private final RedisUtils redisUtils;

    public RedisLimitTemplate(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 尝试获取
     *
     * @param key         key
     * @param limitCount  限流数
     * @param limitExpire 限流时间
     * @return true | false
     */
    public boolean tryAcquire(String key, long limitCount, long limitExpire) {
        final List<String> keys = Collections.singletonList(key);
        String luaScript = buildLuaScript();
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long count = redisUtils.getRedisTemplate().execute(redisScript, keys, limitCount, limitExpire);
        return ObjectUtil.isNotNull(count) && count <= limitCount;
    }

    /**
     * 限流 脚本
     *
     * @return lua脚本
     */
    private String buildLuaScript() {
        // 调用不超过最大值，则直接返回
        // 执行计算器自加
        // 从第一次调用开始限流，设置对应键值的过期
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }


}