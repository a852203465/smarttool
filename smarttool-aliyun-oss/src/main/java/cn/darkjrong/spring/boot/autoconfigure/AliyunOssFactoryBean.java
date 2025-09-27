package cn.darkjrong.spring.boot.autoconfigure;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 阿里云oss 工厂类
 *
 * @author Rong.Jia
 * @date 2024/08/12
 */
public class AliyunOssFactoryBean implements FactoryBean<OSS>, InitializingBean, DisposableBean {

    private OSS ossClient;
    private final AliyunOssProperties aliyunOssProperties;

    public AliyunOssFactoryBean(AliyunOssProperties aliyunOssProperties) {
        this.aliyunOssProperties = aliyunOssProperties;
    }

    @Override
    public OSS getObject() {
        return this.ossClient;
    }

    @Override
    public Class<?> getObjectType() {
        return OSS.class;
    }

    @Override
    public boolean isSingleton() {
        return Boolean.TRUE;
    }

    @Override
    public void destroy() {
        if (ObjectUtil.isNotNull(ossClient)) {
            this.ossClient.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notBlank(aliyunOssProperties.getEndpoint(), "'endpoint' must be not null");
        Assert.notBlank(aliyunOssProperties.getAccessKeyId(), "'accessKeyId' must be not null");
        Assert.notBlank(aliyunOssProperties.getAccessKeySecret(), "'accessKeySecret' must be not null");

        Boolean openIntranet = aliyunOssProperties.getOpenIntranet();
        if (openIntranet) {
            Assert.notBlank(aliyunOssProperties.getIntranet(), "'intranet' must be not null");
            this.ossClient = new OSSClientBuilder().build(aliyunOssProperties.getIntranet(), aliyunOssProperties.getAccessKeyId(), aliyunOssProperties.getAccessKeySecret());
        }else {
            this.ossClient = new OSSClientBuilder().build(aliyunOssProperties.getEndpoint(), aliyunOssProperties.getAccessKeyId(), aliyunOssProperties.getAccessKeySecret());
        }
    }

}
