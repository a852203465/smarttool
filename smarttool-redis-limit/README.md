# 基于Redis限流模块

## 1. 使用方式
### 1.1 引入依赖
```xml
<dependencies>
    <dependency>
        <groupId>cn.darkjrong</groupId>
        <artifactId>smarttool-redis-limit</artifactId>
        <version>${latestversion}</version>
    </dependency>
</dependencies>
```

### 1.2 工具类方式
```java
    @Autowired
    private RedisLimitTemplate redisLimitTemplate;
```

### 1.3 注解方式(在方法上增加注解@RedisLimit)
 - 注解参数说明
   - prefix: 锁的key前缀
   - name: 锁名称
   - expire: 过期时间, 默认：5
   - count: 最多的访问限制次数, 默认：5
   - timeUnit: 时间单位, 默认：秒
   - message：异常提示消息

 - 如果`prefix`, `name`均为空, 会以`类名:方法名`为锁Key
 - 如果`prefix`不为空,`name`为空, 会以`prefix:类名.方法名`为锁Key

```java
import cn.darkjrong.redis.limit.annotation.RedisLimit;

@RedisLimit(prefix = "workflow", name = "startCompensate")
public void startCompensate() {
   a() {
   }
}
```










































