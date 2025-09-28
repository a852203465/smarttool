# 重试模块

## 1.引入依赖
```xml
<dependency>
    <groupId>cn.darkjrong</groupId>
    <artifactId>smarttool-retry</artifactId>
</dependency>
```

## 2.配置参数(application.properties)  yml配置
```yaml
stl:
  retry:
    # 最大重试次数
    max-attempts: 3
    plans:
      # 不间隔重试3次
      - time: 0
        time-unit: minutes
        retry-times: 3
      # 间隔5秒后重试3次
      - time: 5
        time-unit: minutes
        retry-times: 3
      # 每个1分钟重试一次
      - time: 1
        time-unit: minutes
        retry-times: 1
```
## 3. API 注入
```java
@Autowired
private RetryTemplate retryTemplate;            
```













