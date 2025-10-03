# 国际化配置模块

## 使用方式
### 引入依赖
```xml
<dependency>
    <groupId>cn.darkjrong</groupId>
    <artifactId>smarttool-i18n</artifactId>
</dependency>
```

### 配置参数(application.properties)yml配置
```yaml
i18n:
  # 多语言消息文件夹
  basename: classpath:i18n/messages
  # 字符集,默认：UTF-8
  defaultEncoding: UTF-8
  # 并发刷新,默认:true
  concurrentRefresh: true
  # 使用代码作为默认消息,默认:true
  useCodeAsDefaultMessage: true
  # 缓存时间,单位:秒,默认:5
  cacheSeconds: 5
  # 默认语言
  defaultLocale: zh_CN
  # 支持的语言,默认:Locale.getAvailableLocales()
  supportedLocales: zh_CN,en_US
  # 拦截器
  interceptor:
    # 多语言的请求头名,字段名
    name: lang
    # 过滤范围
    patterns: /**
```

### 获取多语言消息

#### 静态类方式
```java
// 静态类方式
I18nUtils.getMessage("login.fail.username");
```

#### spring Bean方式
```java
@Autowired
private I18nTemplate i18nTemplate;

i18nTemplate.getMessage("login.fail.username");

```







