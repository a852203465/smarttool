# FTP客户端模块 
FTP Client操作包 

# 1.使用方式
## 1.1 引入依赖
```xml
<dependency>
    <groupId>cn.darkjrong</groupId>
    <artifactId>smarttool-ftpclient</artifactId>
</dependency>
```

## 1.2 yml配置
```yaml
ftp:
  client:
    # 必须配置enabled: true，否则默认false不起作用
    enabled: true
    host: 192.168.10.107
    port: 21
    base-dir: /data
    username: admin
    passive-mode: false
    password: 123456
    buffer-size: 2048
    encoding: UTF-8
    dataTimeout: 1s
    keepAliveTimeout: 2s
    connect-timeout: 5s
    pool:
        max-active: 10
        max-idle: 5
        max-wait: 1000
        min-idle: 1
```

## 1.3 API操作
```java

@Autowired
private FtpClientTemplate ftpClientTemplate;
```
































