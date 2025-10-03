package cn.darkjrong.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS属性
 *
 * @author Rong.Jia
 * @date 2024/08/12
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "stl.aliyun.oss")
public class AliyunOssProperties {

    /**
     * 是否启用，默认:false
     */
    private Boolean enabled = Boolean.FALSE;

    /**
     * 是否使用内网模式上传
     * 开启：true, 关闭：false
     */
    private Boolean openIntranet = false;

    /**
     * 外网域名
     */
    private String endpoint;

    /**
     * 内网地址
     */
    private String intranet;

    /**
     * ak
     */
    private String accessKeyId;

    /**
     * aks
     */
    private String accessKeySecret;






}
