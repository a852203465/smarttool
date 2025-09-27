# minio模块
MINIO Java SDK封装

## 版本说明
 - minio：8.3.0

## 使用方式
### 1.引入依赖
```xml
<dependency>
    <groupId>cn.darkjrong</groupId>
    <artifactId>smarttool-minio</artifactId>
</dependency>
```

### 2.配置参数(application.properties)  yml配置
```yaml
stl:
    minio:
      # 必须为true
      enabled: true
      endpoint: http:192.168.42.131:9000
      access-key: minio
      secret-key: minio123
      bucket-name: data
```
### 3. API 注入
```java
@Autowired
private MinioTemplate minioTemplate;            
```











