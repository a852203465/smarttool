# swagger模块

## 1. 使用方式
### 2. 引入依赖

```xml
        <dependency>
            <groupId>cn.darkjrong</groupId>
            <artifactId>smarttool-swagger</artifactId>
            <version>${latestversion}</version>
        </dependency>
```

## 2 配置参数(application.properties)  yml配置
### 2.1 普通服务(webmvc)
```yaml

#swagger公共信息
stl:
  swagger:
    production: true 是否是生产环境
    title: 招投标项目-工作流引擎服务
    base-packages: com.example.xdccoredemo.controller
    description: 招投标项目-工作流引擎服务接口文档系统
    version: 2.1.0
    license: Apache License, Version 2.0
    license-url: https://www.apache.org/licenses/LICENSE-2.0.html
    terms-of-service-url: https://www.apache.org/licenses/LICENSE-2.0.html
    base-path: /**
    contact:
      name: 贾荣
      email: Rong.Jia@xdcplus.com
      url: https://www.xdcplus.com

```


