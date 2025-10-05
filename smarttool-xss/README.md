# XSS防攻击模块

## 1. 使用方式

```xml
<dependency>
    <groupId>cn.darkjrong</groupId>
    <artifactId>smarttool-xss</artifactId>
</dependency>
```

## 3、配置
配置文件application.yml

```yaml
stl:
  xss:
    # 是否开启
    enabled: true
    # 需要应用XSS过滤的URL模式。
    url-patterns: /**
    # 需要排除的URL模式，这些URL不会进行XSS过滤。
    excludes:

```


