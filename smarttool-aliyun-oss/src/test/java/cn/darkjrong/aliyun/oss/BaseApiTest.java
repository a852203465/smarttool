package cn.darkjrong.aliyun.oss;

import com.aliyun.oss.OSS;
import cn.darkjrong.spring.boot.autoconfigure.AliyunOssFactoryBean;
import cn.darkjrong.spring.boot.autoconfigure.AliyunOssProperties;

/**
 * 测试类初始化
 *
 * @author Rong.Jia
 * @date 2024/08/12
 */
public class BaseApiTest {

    protected static AliyunOssProperties aliyunOSSProperties;
    protected static OSS ossClient;

    static {
        aliyunOSSProperties = new AliyunOssProperties();
        aliyunOSSProperties.setEndpoint("http://oss-cn-shenzhen.aliyuncs.com");
        aliyunOSSProperties.setOpenIntranet(false);
        aliyunOSSProperties.setAccessKeyId("123131");
        aliyunOSSProperties.setAccessKeySecret("123213121221");
        AliyunOssFactoryBean aliyunOSSFactoryBean = new AliyunOssFactoryBean(aliyunOSSProperties);
        aliyunOSSFactoryBean.afterPropertiesSet();
        ossClient = aliyunOSSFactoryBean.getObject();
    }
















}
