package cn.darkjrong.aliyun.oss.api.impl;

import cn.darkjrong.aliyun.oss.api.PresignedUrlApi;
import cn.darkjrong.core.enums.ErrorEnum;
import cn.darkjrong.core.exceptions.StlWebException;
import cn.darkjrong.core.lang.constants.FileConstant;
import cn.darkjrong.spring.boot.autoconfigure.AliyunOssProperties;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 签名URL API
 *
 * @author Rong.Jia
 * @date 2024/08/12
 */
@Slf4j
@Service
public class PresignedUrlApiImpl implements PresignedUrlApi {

    private final AliyunOssProperties aliyunOSSProperties;
    private final OSS ossClient;

    public PresignedUrlApiImpl(OSS ossClient, AliyunOssProperties aliyunOSSProperties) {
        this.aliyunOSSProperties = aliyunOSSProperties;
        this.ossClient = ossClient;
    }

    @Override
    public String getUrl(String bucketName, String objectName) {
        return getUrl(bucketName, objectName, FileConstant.EXPIRATION_TIME, null);
    }

    @Override
    public String getUrl(String bucketName, String objectName, Long expirationTime) {
        return this.getUrl(bucketName, objectName, expirationTime, null);
    }

    @Override
    public String getUrl(String bucketName, String objectName, String style) {
        return getUrl(bucketName, objectName, FileConstant.EXPIRATION_TIME, style);
    }

    @Override
    public String getUrl(String bucketName, String objectName, Long expirationTime, String style) {
        if (Validator.isNull(expirationTime)) {
            expirationTime = FileConstant.EXPIRATION_TIME;
        }
        Date expiration = new Date(DateUtil.current() + expirationTime);
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);

        if (StrUtil.isNotBlank(style)) {
            req.setProcess(style);
        }
        req.setExpiration(expiration);
        try {
            String signedUrl = ossClient.generatePresignedUrl(req).toString();
            if (aliyunOSSProperties.getOpenIntranet()) {
                signedUrl = StringUtils.replace(signedUrl, aliyunOSSProperties.getIntranet(), aliyunOSSProperties.getEndpoint());
            }
            return signedUrl;
        } catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】获取访问连接, 异常【%s】", bucketName, objectName, e.getMessage()), e);
            throw new StlWebException(ErrorEnum.FILE_OBJECT_ACQUISITION_ACCESSIBLE_CONNECTION_EXCEPTION);
        }
    }



}
