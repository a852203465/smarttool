# 自动配置模块

## 1. 使用方式
### 1.1 引入依赖
```xml
<dependency>
    <groupId>cn.darkjrong</groupId>
    <artifactId>smarttool-autoconfigure</artifactId>
    <version>${latestversion}</version>
</dependency>
```

### 1.2 配置参数(application.properties)  yml配置
#### 1.2.1 线程池配置
```yaml
stl:
  task:
    pool:
      enabled: true   # 必须为true, 否则不会生效
      core-pool-size: 10  
      max-pool-size: 10    
      keep-alive-seconds: 10
      queue-capacity: 100
      threadName: asyncExecutor-
```

#### 1.2.2 文件上传限制配置(仅WebMvc生效)
```yaml
stl:
  multipart:
    enabled: true  # 必须为true, 否则不会生效
    location: /data   
    is-project-root: true   # 路径是否在当前项目的根目录生成
    max-request-size: 10
    max-file-size: 10
    file-size-threshold: 10
```

#### 1.2.3 定时器连接池
```yaml
stl:
  scheduled:
    enabled: true  # 是否开启
    pool-size: 10
```

#### 1.2.4 跨域
```yaml
stl:
  cors:
    enabled: true  # 是否开启
```

#### 1.2.5 任务执行器
```yaml
stl:
  task:
    executor:
      enabled: true   # 必须为true, 否则不会生效
      core-pool-size: 10  
      max-pool-size: 10    
      keep-alive-seconds: 10
      queue-capacity: 100
```

#### 1.2.6 参数去除前后空格
```yaml
stl:
  trim:
    enabled: true
```























