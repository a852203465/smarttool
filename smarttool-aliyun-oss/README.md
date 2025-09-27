# Aliyun OSS模块
阿里云OSS对象存储 Java SDK

## 1.使用方式
### 1.1引入依赖
```xml
<dependencies>
    <dependency>
        <groupId>cn.darkjrong</groupId>
        <artifactId>smarttool-aliyun-oss</artifactId>
        <version>${latestversion}</version>
    </dependency>
</dependencies>
```

### 2.配置参数(application.properties) yml配置
```yaml
stl:
  aliyun:
    oss:
      # 必须为true
      enabled: true
      # 外网域名
      endpoint: http://oss-cn-shenzhen.aliyuncs.com
      # ak
      accessKeyId: 
      # aks
      accessKeySecret: 
      # 内网地址
      intranet: http://oss-cn-shenzhen.aliyuncs.com
      # 是否使用内网模式上传， 开启：true, 关闭：false
      openIntranet: true
```

### 3.API 注入
```java
    
    // 存储空间 API
    @Autowired
    private BucketApi bucketApi;

    // 文件操作 API
    @Autowired
    private FileOperationsApi fileOperationsApi;
    
    // 签名URL API
    @Autowired
    private PresignedUrlApi presignedUrlApi;



```

### 4.进度回调
```java
/**
 * 进度回调接口 实现
 */
@Component
public class ProgressCallBackImpl implements ProgressCallBack {

    private static final Logger logger = LoggerFactory.getLogger(ProgressCallBackImpl.class);

    /**
     *
     * @param objectName 正在上传的文件名
     * @param totalBytes 总大小
     * @param completeBytes 已完成大小
     * @param succeed 是否上传/下载成功， true:成功，false：失败
     */
    @Override
    public void progress(String objectName, Long totalBytes, Long completeBytes, Boolean succeed) {

    }
}
```









