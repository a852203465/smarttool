package cn.darkjrong.aliyun.oss.api.impl;

import cn.darkjrong.aliyun.oss.api.BucketApi;
import cn.darkjrong.aliyun.oss.api.PresignedUrlApi;
import cn.darkjrong.aliyun.oss.domain.BucketPolicyInfo;
import cn.darkjrong.aliyun.oss.domain.BucketPolicyParam;
import cn.darkjrong.aliyun.oss.exceptions.AliyunOssClientException;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 存储空间 API 实现
 *
 * @author Rong.Jia
 * @date 2024/08/12
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class BucketApiImpl extends BaseApiImpl implements BucketApi {

    public BucketApiImpl(OSS ossClient, PresignedUrlApi presignedUrlApi) {
        super(ossClient, presignedUrlApi);
    }

    @Override
    public Bucket createBucket(String bucketName) throws AliyunOssClientException {
        return this.createBucket(bucketName, StorageClass.Standard,
                DataRedundancyType.ZRS, CannedAccessControlList.Private);
    }

    @Override
    public Bucket createBucket(String bucketName, StorageClass storageClass,
                               DataRedundancyType dataRedundancyType,
                               CannedAccessControlList cannedAccessControlList) throws AliyunOssClientException {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        createBucketRequest.setStorageClass(storageClass);
        createBucketRequest.setDataRedundancyType(dataRedundancyType);
        createBucketRequest.setCannedACL(cannedAccessControlList);

        try {
            return getOssClient().createBucket(createBucketRequest);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】创建异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public List<Bucket> getBuckets(String bucketPrefix, Boolean flag) throws AliyunOssClientException {
        try {
            List<Bucket> bucketList;
            if (!flag) {
                bucketList = getOssClient().listBuckets();
            }else {
                ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
                listBucketsRequest.setPrefix(bucketPrefix);
                bucketList = getOssClient().listBuckets(listBucketsRequest).getBucketList();
            }
            return bucketList;
        }catch (Exception e) {
            log.error(String.format("bucketPrefix【%s】flag【%s】创建buckets异常 【%s】", bucketPrefix, flag, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        try {
            return getOssClient().doesBucketExist(bucketName);
        }catch (Exception e) {
            log.error(String.format("判断【%s】是否存在异常【%s】", bucketName, e.getMessage()), e);
        }
        return Boolean.FALSE;
    }

    @Override
    public String getBucketLocation(String bucketName) throws AliyunOssClientException {
        try {
            return getOssClient().getBucketLocation(bucketName);
        }catch (Exception e) {
            log.error(String.format("获取bucket【%s】地域异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public BucketInfo getBucketInfo(String bucketName) throws AliyunOssClientException {
        try {
            return getOssClient().getBucketInfo(bucketName);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】获取异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean setBucketAcl(String bucketName, CannedAccessControlList cannedAccessControlList) {
        try {
            getOssClient().setBucketAcl(bucketName, cannedAccessControlList);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】访问权限【%s】设置异常【%s】", bucketName, cannedAccessControlList.toString(), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public AccessControlList getBucketAcl(String bucketName) throws AliyunOssClientException {
        try {
            return getOssClient().getBucketAcl(bucketName);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】访问权限获取异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean deleteBucket(String bucketName) {
        try {
            getOssClient().deleteBucket(bucketName);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】删除异常【%s】", bucketName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketTagging(String bucketName, Map<String, String> tags) {
        if (tags == null || CollUtil.isEmpty(tags)) {
            return Boolean.FALSE;
        }
        try {
            getOssClient().setBucketTagging(bucketName, tags);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置标签【%s】异常【%s】", bucketName, JSON.toJSONString(tags), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public Map<String, String> getBucketTagging(String bucketName) throws AliyunOssClientException {
        try {
            return getOssClient().getBucketTagging(bucketName).getAllTags();
        }catch (Exception e) {
            log.error(String.format("bucket【%s】获取标签信息异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public List<Bucket> getBuckets(String tagKey, String tagValue) throws AliyunOssClientException {
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        listBucketsRequest.setTag(tagKey, tagValue);
        return this.getBuckets(listBucketsRequest);
    }

    @Override
    public List<Bucket> getBuckets(ListBucketsRequest listBucketsRequest) throws AliyunOssClientException {
        try {
            return getOssClient().listBuckets(listBucketsRequest).getBucketList();
        }catch (Exception e) {
            log.error(String.format("【%s】获取bucket集合异常【%s】", JSON.toJSONString(listBucketsRequest), e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean deleteBucketTagging(String bucketName) {
        try {
            getOssClient().deleteBucketTagging(bucketName);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】删除标签信息异常【%s】", bucketName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketPolicy(String bucketName, BucketPolicyParam bucketPolicyParam) {
        try {
            getOssClient().setBucketPolicy(bucketName, JSON.toJSONString(bucketPolicyParam));
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置访问策略【%s】异常【%s】",
                    bucketName, JSON.toJSONString(bucketPolicyParam), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public BucketPolicyInfo getBucketPolicy(String bucketName) {
        try {
            GetBucketPolicyResult policyResult = getOssClient().getBucketPolicy(bucketName);
            String policyText = policyResult.getPolicyText();
            return JSONObject.parseObject(policyText, BucketPolicyInfo.class);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】获取访问权限异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean deleteBucketPolicy(String bucketName) {
        try {
            getOssClient().deleteBucketPolicy(bucketName);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】删除访问权限异常【%s】", bucketName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketInventoryConfiguration(String bucketName, InventoryConfiguration inventoryConfiguration) {
        try {
            getOssClient().setBucketInventoryConfiguration(bucketName, inventoryConfiguration);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】添加清单配置【%s】异常【%s】",
                    bucketName, JSON.toJSONString(inventoryConfiguration), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public GetBucketInventoryConfigurationResult getBucketInventoryConfiguration(String bucketName, String inventoryId) {
        try {
            return getOssClient().getBucketInventoryConfiguration(bucketName, inventoryId);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】inventory【%s】获取清单配置异常【%s】", bucketName, inventoryId, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean deleteBucketInventoryConfiguration(String bucketName, String inventoryId) {
        try {
            getOssClient().deleteBucketInventoryConfiguration(bucketName, inventoryId);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】inventory【%s】删除清单配置异常【%s】", bucketName, inventoryId, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketLifecycle(String bucketName, String ruleId,
                                      String matchPrefix, Map<String, String> matchTags,
                                      Integer day, boolean expiredDeleteMarker) {
        SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);

        LifecycleRule rule = new LifecycleRule(ruleId, matchPrefix,
                LifecycleRule.RuleStatus.Enabled, day);
        rule.setTags(matchTags);
        rule.setExpiredDeleteMarker(expiredDeleteMarker);
        request.AddLifecycleRule(rule);

        try {
            getOssClient().setBucketLifecycle(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置生命周期【%s】异常【%s】", bucketName, JSON.toJSONString(rule), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketLifecycle(String bucketName, String ruleId,
                                      String matchPrefix, Map<String, String> matchTags,
                                      Long time, boolean expiredDeleteMarker) {

        SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);
        LifecycleRule rule = new LifecycleRule(ruleId, matchPrefix, LifecycleRule.RuleStatus.Enabled);
        rule.setTags(matchTags);
        rule.setExpiredDeleteMarker(expiredDeleteMarker);
        rule.setCreatedBeforeDate(new Date(time));
        request.AddLifecycleRule(rule);

        try {
            getOssClient().setBucketLifecycle(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置生命周期【%s】异常【%s】", bucketName, JSON.toJSONString(rule), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketLifecycle(String bucketName, String ruleId,
                                      String matchPrefix, Integer day, boolean expiredDeleteMarker) {

        SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);
        LifecycleRule rule = new LifecycleRule(ruleId, matchPrefix, LifecycleRule.RuleStatus.Enabled);
        LifecycleRule.AbortMultipartUpload abortMultipartUpload = new LifecycleRule.AbortMultipartUpload();
        abortMultipartUpload.setExpirationDays(day);
        rule.setExpiredDeleteMarker(expiredDeleteMarker);
        rule.setAbortMultipartUpload(abortMultipartUpload);
        request.AddLifecycleRule(rule);

        try {
            getOssClient().setBucketLifecycle(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置生命周期【%s】异常【%s】", bucketName, JSON.toJSONString(rule), e.getMessage()), e);
        }

        return false;
    }

    @Override
    public boolean setBucketLifecycle(String bucketName, String ruleId,
                                      String matchPrefix, Long time, boolean expiredDeleteMarker) {

        SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);
        LifecycleRule rule = new LifecycleRule(ruleId, matchPrefix, LifecycleRule.RuleStatus.Enabled);
        LifecycleRule.AbortMultipartUpload abortMultipartUpload = new LifecycleRule.AbortMultipartUpload();
        abortMultipartUpload.setCreatedBeforeDate(new Date(time));
        rule.setAbortMultipartUpload(abortMultipartUpload);
        rule.setExpiredDeleteMarker(expiredDeleteMarker);
        request.AddLifecycleRule(rule);

        try {
            getOssClient().setBucketLifecycle(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置生命周期【%s】异常【%s】", bucketName, JSON.toJSONString(rule), e.getMessage()), e);
        }

        return false;
    }

    @Override
    public boolean setBucketLifecycle(String bucketName, String ruleId,
                                      String matchPrefix, Integer expirationDays,
                                      StorageClass storageClass, boolean expiredDeleteMarker) {

        SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);

        LifecycleRule rule = new LifecycleRule(ruleId, matchPrefix, LifecycleRule.RuleStatus.Enabled);

        List<LifecycleRule.StorageTransition> storageTransitions = new ArrayList<>();

        LifecycleRule.StorageTransition storageTransition = new LifecycleRule.StorageTransition();
        storageTransition.setStorageClass(storageClass);
        storageTransition.setExpirationDays(expirationDays);
        storageTransitions.add(storageTransition);
        rule.setExpiredDeleteMarker(expiredDeleteMarker);
        rule.setStorageTransition(storageTransitions);

        request.AddLifecycleRule(rule);

        try {
            getOssClient().setBucketLifecycle(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置生命周期【%s】异常【%s】", bucketName, JSON.toJSONString(rule), e.getMessage()), e);
        }

        return false;
    }

    @Override
    public boolean setBucketLifecycle(String bucketName, String ruleId,
                                      String matchPrefix, Long time,
                                      StorageClass storageClass, boolean expiredDeleteMarker) {

        SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);

        LifecycleRule rule = new LifecycleRule(ruleId, matchPrefix, LifecycleRule.RuleStatus.Enabled);

        List<LifecycleRule.StorageTransition> storageTransitions = new ArrayList<>();

        LifecycleRule.StorageTransition storageTransition = new LifecycleRule.StorageTransition();
        storageTransition.setStorageClass(storageClass);
        storageTransition.setCreatedBeforeDate(new Date(time));
        storageTransitions.add(storageTransition);
        rule.setExpiredDeleteMarker(expiredDeleteMarker);
        rule.setStorageTransition(storageTransitions);

        request.AddLifecycleRule(rule);

        try {
            getOssClient().setBucketLifecycle(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置生命周期【%s】异常【%s】", bucketName, JSON.toJSONString(rule), e.getMessage()), e);
        }

        return false;
    }

    @Override
    public boolean setBucketLifecycle(String bucketName, String ruleId,
                                      String matchPrefix, Integer storageClassDays,
                                      Integer deleteDays, StorageClass storageClass,
                                      boolean expiredDeleteMarker) {

        SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);

        LifecycleRule rule = new LifecycleRule(ruleId, matchPrefix, LifecycleRule.RuleStatus.Enabled);
        rule.setExpiredDeleteMarker(expiredDeleteMarker);

        LifecycleRule.NoncurrentVersionStorageTransition storageTransition =
                new LifecycleRule.NoncurrentVersionStorageTransition()
                        .withNoncurrentDays(storageClassDays).withStrorageClass(storageClass);

        if (Validator.isNotNull(deleteDays)) {
            LifecycleRule.NoncurrentVersionExpiration deleteExpiration = new LifecycleRule.NoncurrentVersionExpiration()
                    .withNoncurrentDays(deleteDays);

            rule.setNoncurrentVersionExpiration(deleteExpiration);
        }

        rule.setNoncurrentVersionStorageTransitions(CollectionUtil.newArrayList(storageTransition));

        request.AddLifecycleRule(rule);

        try {
            getOssClient().setBucketLifecycle(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置生命周期【%s】异常【%s】", bucketName, JSON.toJSONString(rule), e.getMessage()), e);
        }

        return false;
    }

    @Override
    public List<LifecycleRule> getBucketLifecycle(String bucketName) {
        try {
            return getOssClient().getBucketLifecycle(bucketName);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】获取生命周期异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean deleteBucketLifecycle(String bucketName) {
        try {
            getOssClient().deleteBucketLifecycle(bucketName);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】删除生命周期异常【%s】", bucketName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketRequestPayment(String bucketName, Payer payer) {
        try {
            getOssClient().setBucketRequestPayment(bucketName, payer);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置请求者付费模式【%s】, 异常【%s】", bucketName, JSON.toJSONString(payer), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public Payer getBucketRequestPayment(String bucketName) throws AliyunOssClientException {
        try {
            return getOssClient().getBucketRequestPayment(bucketName).getPayer();
        }catch (Exception e) {
            log.error(String.format("bucket【%s】获取请求者付费模式, 异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean setBucketReferer(String bucketName, List<String> refererList) {
        BucketReferer br = new BucketReferer(Boolean.TRUE, refererList);
        try {
            getOssClient().setBucketReferer(bucketName, br);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置防盗链【%s】, 异常【%s】", bucketName, JSON.toJSONString(refererList), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public List<String> getBucketReferer(String bucketName) throws AliyunOssClientException {
        try {
            BucketReferer bucketReferer = getOssClient().getBucketReferer(bucketName);
            return bucketReferer.getRefererList();
        }catch (Exception e) {
            log.error(String.format("bucket【%s】获取防盗链, 异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean deleteBucketReferer(String bucketName) {
        try {
            BucketReferer br = new BucketReferer();
            getOssClient().setBucketReferer(bucketName, br);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】删除防盗链, 异常【%s】", bucketName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketLogging(String bucketName, String logBucketName, String logDir) {
        try {
            if (!this.doesBucketExist(logBucketName)){
                this.createBucket(logBucketName);
            }
            SetBucketLoggingRequest request = new SetBucketLoggingRequest(bucketName);
            request.setTargetBucket(logBucketName);
            request.setTargetPrefix(logDir);
            getOssClient().setBucketLogging(request);

            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】日志bucket【%s】日志目录【%s】开启访问日志记录,异常【%s】",
                    bucketName, logBucketName, logDir, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public BucketLoggingResult getBucketLogging(String bucketName) throws AliyunOssClientException {
        try {
            return getOssClient().getBucketLogging(bucketName);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】查看访问日志设置, 异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean closeBucketLogging(String bucketName) {
        try {
            SetBucketLoggingRequest request = new SetBucketLoggingRequest(bucketName);
            request.setTargetBucket(null);
            request.setTargetPrefix(null);
            getOssClient().setBucketLogging(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】关闭访问日志记录, 异常【%s】", bucketName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketWebsite(String bucketName, String indexDocument, String errorDocument) {
        try {
            SetBucketWebsiteRequest request = new SetBucketWebsiteRequest(bucketName);
            request.setIndexDocument(indexDocument);
            request.setErrorDocument(errorDocument);

            getOssClient().setBucketWebsite(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】indexDoc【%s】errorDoc【%s】设置静态网站托管, 异常【%s】",
                    bucketName, indexDocument, errorDocument,e.getMessage()), e);
        }
        return false;
    }

    @Override
    public BucketWebsiteResult getBucketWebsite(String bucketName) throws AliyunOssClientException {
        try {
            return getOssClient().getBucketWebsite(bucketName);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】查看静态网站托管配置, 异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean deleteBucketWebsite(String bucketName) {
        try {
            getOssClient().deleteBucketWebsite(bucketName);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】删除静态网站托管配置, 异常【%s】", bucketName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean enableBucketReplication(String sourceBucketName, String replicationRuleId,
                                           String targetBucketName, String targetBucketLocation,
                                           boolean enableHistoricalObjectReplication) {
        AddBucketReplicationRequest request = new AddBucketReplicationRequest(sourceBucketName);
        request.setReplicationRuleID(replicationRuleId);
        request.setTargetBucketName(targetBucketName);
        request.setTargetBucketLocation(targetBucketLocation);
        request.setEnableHistoricalObjectReplication(enableHistoricalObjectReplication);
        try {
            getOssClient().addBucketReplication(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("【%s】开启跨区域复制,异常【%s】", JSON.toJSONString(request), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public List<ReplicationRule> getBucketReplication(String bucketName) throws AliyunOssClientException {
        try {
            return getOssClient().getBucketReplication(bucketName);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】查看跨区域复制, 异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public BucketReplicationProgress getBucketReplicationProgress(String bucketName, String replicationRuleId) throws AliyunOssClientException {
        try {
            return getOssClient().getBucketReplicationProgress(bucketName, replicationRuleId);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】replicationRule【%s】查看跨区域复制进度, 异常【%s】",
                    bucketName, replicationRuleId, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public List<String> getBucketReplicationLocation(String bucketName) {
        try {
            return getOssClient().getBucketReplicationLocation(bucketName);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】查看可同步的目标地域, 异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean closeBucketReplication(String bucketName, String replicationRuleId) {
        try {
            getOssClient().deleteBucketReplication(bucketName, replicationRuleId);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】replicationRule【%s】关闭跨区域复制, 异常【%s】", bucketName, replicationRuleId, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketCORS(String bucketName, List<String> allowedOrigin, List<String> allowedMethod, List<String> allowedHeader, List<String> exposedHeader, Integer maxAgeSeconds) {
        SetBucketCORSRequest request = new SetBucketCORSRequest(bucketName);

        List<SetBucketCORSRequest.CORSRule> putCorsRules = new ArrayList<>();
        SetBucketCORSRequest.CORSRule corRule = new SetBucketCORSRequest.CORSRule();

        corRule.setAllowedMethods(allowedMethod);
        corRule.setAllowedOrigins(allowedOrigin);
        corRule.setAllowedHeaders(allowedHeader);
        corRule.setExposeHeaders(exposedHeader);
        corRule.setMaxAgeSeconds(maxAgeSeconds);

        putCorsRules.add(corRule);
        request.setCorsRules(putCorsRules);

        try {
            getOssClient().setBucketCORS(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("【%s】设置跨域资源共享规则, 异常【%s】", JSON.toJSONString(request), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public List<SetBucketCORSRequest.CORSRule> getBucketCORSRules(String bucketName) throws AliyunOssClientException {
        try {
            return getOssClient().getBucketCORSRules(bucketName);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】获取跨域资源共享规则, 异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean deleteBucketCORSRules(String bucketName) {
        try {
            getOssClient().deleteBucketCORSRules(bucketName);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】删除跨域资源共享规则, 异常【%s】", bucketName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setBucketVersioning(String bucketName) {
        BucketVersioningConfiguration configuration = new BucketVersioningConfiguration();
        configuration.setStatus(BucketVersioningConfiguration.ENABLED);
        SetBucketVersioningRequest request = new SetBucketVersioningRequest(bucketName, configuration);

        try {
            getOssClient().setBucketVersioning(request);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】设置Bucket版本控制, 异常【%s】", bucketName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public String getBucketVersioning(String bucketName) {
        try {
            BucketVersioningConfiguration versionConfiguration = getOssClient().getBucketVersioning(bucketName);
            return versionConfiguration.getStatus();
        }catch (Exception e) {
            log.error(String.format("bucket【%s】获取Bucket版本控制状态, 异常【%s】", bucketName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean setBucketEncryption(String bucketName, String kmsMasterKeyId) {
        return this.setBucketEncryption(bucketName, SSEAlgorithm.KMS, kmsMasterKeyId);
    }

    @Override
    public boolean setBucketEncryption(String bucketName, SSEAlgorithm sseAlgorithm, String kmsMasterKeyId) {

        ServerSideEncryptionByDefault applyServerSideEncryptionByDefault = new ServerSideEncryptionByDefault(SSEAlgorithm.KMS);
        applyServerSideEncryptionByDefault.setSSEAlgorithm(sseAlgorithm);

        if (SSEAlgorithm.KMS == sseAlgorithm && StrUtil.isNotBlank(kmsMasterKeyId)) {
            applyServerSideEncryptionByDefault.setKMSMasterKeyID(kmsMasterKeyId);
        }

        ServerSideEncryptionConfiguration sseConfig = new ServerSideEncryptionConfiguration();
        sseConfig.setApplyServerSideEncryptionByDefault(applyServerSideEncryptionByDefault);

        try {

            SetBucketEncryptionRequest request = new SetBucketEncryptionRequest(bucketName, sseConfig);
            getOssClient().setBucketEncryption(request);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】配置Bucket加密【%s】kmsMasterKeyId【%s】, 异常【%s】",
                    bucketName, sseAlgorithm.toString(), kmsMasterKeyId, e.getMessage()), e);
        }
        return false;
    }


}
