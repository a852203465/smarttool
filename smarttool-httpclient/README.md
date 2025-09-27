# Httpclient模块
基于RestTemplate封装

## 1.引入依赖
```xml
<dependency>
    <groupId>cn.darkjrong</groupId>
    <artifactId>smarttool-httpclient</artifactId>
</dependency>
```

## 2.配置参数(application.properties)  yml配置
```yaml
stl:
    http-client:
      # 是否开启
      enabled: true
      #连接池的最大连接数，0代表不限；如果取0，需要考虑连接泄露导致系统崩溃的后果
      maxTotalConnect: 1000
      #每个路由的最大连接数,如果只调用一个地址,可以将其设置为最大连接数
      maxConnectPerRoute: 200
      # 指客户端和服务器建立连接的超时时间,ms , 最大约21秒,因为内部tcp在进行三次握手建立连接时,默认tcp超时时间是20秒
      connectTimeout: 3000
      # 指客户端从服务器读取数据包的间隔超时时间,不是总读取时间,也就是socket timeout,ms
      readTimeout: 5000
      # 从连接池获取连接的timeout,不宜过大,ms
      connectionRequestTimout: 200
      # 重试次数
      retryTimes: 3
      charset: UTF-8
      # 长连接保持时间 单位s,不宜过长
      keepAliveTime: 10
      # 针对不同的网址,长连接保持的存活时间,单位s,如果是频繁而持续的请求,可以设置小一点,不建议设置过大,避免大量无用连接占用内存资源
      keepAliveTargetHost:
        www.baidu.com: 5
```
## 3.API 注入
```java
@Autowired
private RestTemplateUtils restTemplateUtils;
```

## 4.自定义Request 拦截器
 实现ClientHttpRequestInterceptor，并放到Spring 容器中
```java
/**
 * 记录 restTemplate 访问信息
 * @author Rong.Jia
 * @date 2020/02/05 14:33
 */
@Slf4j
@Component
public class TrackLogClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        trackRequest(request,body);
        ClientHttpResponse httpResponse = execution.execute(request, body);
        trackResponse(httpResponse);
        return httpResponse;
    }

    private void trackResponse(ClientHttpResponse httpResponse)throws IOException {
        log.debug("============================response begin==========================================");
        log.debug("Status code  : {}", httpResponse.getStatusCode());
        log.debug("Status text  : {}", httpResponse.getStatusText());
        log.debug("Headers      : {}", httpResponse.getHeaders());
        log.debug("=======================response end=================================================");
    }

    private void trackRequest(HttpRequest request, byte[] body)throws UnsupportedEncodingException {
        log.debug("======= request begin ========");
        log.debug("uri : {}", request.getURI());
        log.debug("method : {}", request.getMethod());
        log.debug("headers : {}", request.getHeaders());
        log.debug("request body : {}", new String(body, StandardCharsets.UTF_8));
        log.debug("======= request end ========");
    }

}

```



 
 
 

















