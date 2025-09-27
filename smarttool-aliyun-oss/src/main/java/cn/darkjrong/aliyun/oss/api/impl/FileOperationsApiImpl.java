package cn.darkjrong.aliyun.oss.api.impl;

import cn.darkjrong.aliyun.oss.api.BucketApi;
import cn.darkjrong.aliyun.oss.api.FileOperationsApi;
import cn.darkjrong.aliyun.oss.api.PresignedUrlApi;
import cn.darkjrong.aliyun.oss.callback.ProgressCallBack;
import cn.darkjrong.aliyun.oss.domain.FileInfo;
import cn.darkjrong.core.enums.ErrorEnum;
import cn.darkjrong.aliyun.oss.exceptions.AliyunOssClientException;
import cn.darkjrong.core.lang.constants.FileConstant;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * 上传，下载，管理 API 接口实现类
 *
 * @author Rong.Jia
 * @date 2024/08/12
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class FileOperationsApiImpl extends BaseApiImpl implements FileOperationsApi {

    private final BucketApi bucketApi;

    public FileOperationsApiImpl(OSS ossClient, PresignedUrlApi presignedUrlApi, BucketApi bucketApi) {
        super(ossClient, presignedUrlApi);
        this.bucketApi = bucketApi;
    }

    @Override
    public FileInfo uploadFile(String bucketName, String directory, File file, Map<String, String> tags) throws AliyunOssClientException {
        if (!FileUtil.exist(file)) {
            log.error("********,uploadFile(), bucket【{}】directory【{}】 file does not exist", bucketName, directory);
            throw new AliyunOssClientException(ErrorEnum.FILE_DOES_NOT_EXIST.getMessage());
        }
        String extName = StrUtil.DOT + FileUtil.extName(file);
        return this.uploadFile(bucketName, directory, FileUtil.getInputStream(file), extName, tags);
    }

    @Override
    public FileInfo uploadFile(String bucketName, String directory, byte[] bytes, Map<String, String> tags) throws AliyunOssClientException {
        if (ArrayUtil.isEmpty(bytes)) {
            log.error("********,uploadFile(), bucket【{}】directory【{}】 bytes cannot be empty", bucketName, directory);
            throw new IllegalArgumentException(ErrorEnum.BYTES_CANNOT_BE_EMPTY.getMessage());
        }
        return this.uploadFile(bucketName, directory, new ByteArrayInputStream(bytes), tags);
    }

    @Override
    public FileInfo uploadFile(String bucketName, String directory, InputStream inputStream, String extName, Map<String, String> tags) throws AliyunOssClientException {

        Assert.notNull(inputStream, ErrorEnum.THE_INPUT_STREAM_CANNOT_BE_EMPTY.getMessage());
        Assert.notBlank(bucketName, ErrorEnum.BUCKET_NAME_CANNOT_BE_NULL.getMessage());
        Assert.notBlank(directory, ErrorEnum.DIRECTORY_CANNOT_BE_EMPTY.getMessage());

        if (!bucketApi.doesBucketExist(bucketName)) {
            bucketApi.createBucket(bucketName);
        }

        String fileName = getFileName(directory, extName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream);

        if (ObjectUtil.isNotNull(tags) && CollectionUtil.isNotEmpty(tags) && tags.size() > 0) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setObjectTagging(tags);
            putObjectRequest.setMetadata(metadata);
        }

        try {

            PutObjectResult putObjectResult = getOssClient().putObject(putObjectRequest);

            FileInfo FileInfo = new FileInfo();
            FileInfo.setUrl(fileName);
            FileInfo.setVersionId(putObjectResult.getVersionId());

            return FileInfo;
        }catch (Exception e) {
            log.error(String.format("文件上传bucket【%s】directory【%s】, 异常 【%s】", bucketName, directory, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public FileInfo uploadFile(String bucketName, String directory, InputStream inputStream, Map<String, String> tags) throws AliyunOssClientException {
        return this.uploadFile(bucketName, directory, inputStream, FileConstant.JPEG_SUFFIX, tags);
    }

    @Override
    public FileInfo shardUploadFile(String bucketName, String directory, File file, Map<String, String> tags) throws AliyunOssClientException {
        return this.shardUploadFile(bucketName, directory, file, FileConstant.PART_SIZE, tags);
    }

    @Override
    public FileInfo shardUploadFile(String bucketName, String directory, File file, Long partSize, Map<String, String> tags) throws AliyunOssClientException {
        if (!FileUtil.exist(file)) {
            log.error("********,shardUploadFile(), bucket【{}】directory【{}】 bytes cannot be empty", bucketName, directory);
            throw new AliyunOssClientException(ErrorEnum.FILE_DOES_NOT_EXIST.getMessage());
        }

        Assert.notBlank(bucketName, ErrorEnum.BUCKET_NAME_CANNOT_BE_NULL.getMessage());
        Assert.notBlank(directory, ErrorEnum.DIRECTORY_CANNOT_BE_EMPTY.getMessage());

        if (!bucketApi.doesBucketExist(bucketName)) {
            bucketApi.createBucket(bucketName);
        }

        if (Validator.isNull(partSize) || partSize <=0) {
            partSize = FileConstant.PART_SIZE;
        }

        String objectName = getFileName(directory, file);

        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags = new ArrayList<>();

        long fileLength = FileUtil.size(file);
        int partCount = (int) (fileLength / partSize);

        if (fileLength % partSize != 0) {
            partCount++;
        }

        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);

        if (ObjectUtil.isNotNull(tags) && CollectionUtil.isNotEmpty(tags) &&tags.size() > 0) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setObjectTagging(tags);
            request.setObjectMetadata(metadata);
        }

        try {

            InitiateMultipartUploadResult result = getOssClient().initiateMultipartUpload(request);

            // 返回uploadId，它是分片上传事件的唯一标识
            String uploadId = result.getUploadId();

            for (int i = 0; i < partCount; i++) {

                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;

                InputStream instep = FileUtil.getInputStream(file);

                instep.skip(startPos);

                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(instep);
                uploadPartRequest.setPartSize(curPartSize);
                uploadPartRequest.setPartNumber(i + 1);
                UploadPartResult uploadPartResult = getOssClient().uploadPart(uploadPartRequest);
                partETags.add(uploadPartResult.getPartETag());

                IoUtil.close(instep);
            }

            partETags.sort(Comparator.comparingInt(PartETag::getPartNumber));

            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);
            CompleteMultipartUploadResult completeMultipartUploadResult = getOssClient().completeMultipartUpload(completeMultipartUploadRequest);

            FileInfo FileInfo = new FileInfo(uploadId, objectName);
            FileInfo.setVersionId(completeMultipartUploadResult.getVersionId());

            return FileInfo;
        }catch (Exception e) {
            log.error(String.format("文件分片上传bucket【%s】directory【%s】, 异常 【%s】", bucketName, directory, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean abortShardUpload(String bucketName, String objectName, String uploadId) {
        AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(bucketName, objectName, uploadId);
        try {
            getOssClient().abortMultipartUpload(abortMultipartUploadRequest);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】uploadId【%s】取消分片上传事件, 异常【%s】",
                    bucketName, objectName, uploadId, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public FileInfo uploadFile(String bucketName, String directory, File file, Map<String, String> tags, ProgressCallBack progressCallBack) throws AliyunOssClientException {

        Assert.notBlank(bucketName, ErrorEnum.BUCKET_NAME_CANNOT_BE_NULL.getMessage());
        Assert.notBlank(directory, ErrorEnum.DIRECTORY_CANNOT_BE_EMPTY.getMessage());

        if (!FileUtil.exist(file)) {
            log.error("********,uploadFile()#ProgressCallBack, bucket【{}】directory【{}】 bytes cannot be empty", bucketName, directory);
            throw new AliyunOssClientException(ErrorEnum.FILE_DOES_NOT_EXIST.getMessage());
        }

        if (!bucketApi.doesBucketExist(bucketName)) {
            bucketApi.createBucket(bucketName);
        }

        String objectName = getFileName(directory, file);
        FileProgressListener fileProgressListener = new FileProgressListener(progressCallBack, objectName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file).<PutObjectRequest>withProgressListener(fileProgressListener);

        if (ObjectUtil.isNotNull(tags) && CollectionUtil.isNotEmpty(tags) &&tags.size() > 0) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setObjectTagging(tags);
            putObjectRequest.setMetadata(metadata);
        }

        try {

            getOssClient().putObject(putObjectRequest);

            FileInfo FileInfo = new FileInfo();
            FileInfo.setUrl(objectName);
            FileInfo.setSucceed(fileProgressListener.isSucceed());

            return FileInfo;
        } catch (Exception e) {
            log.error(String.format("文件分片上传带进度 bucket【%s】directory【%s】, 异常 【%s】", bucketName, directory, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public byte[] downloadFile(String bucketName, String objectName) throws AliyunOssClientException {
        OSSObject ossObject = null;
        try {
            ossObject = getOssClient().getObject(bucketName, objectName);
            return IoUtil.readBytes(ossObject.getObjectContent());
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】下载文件, 异常【%s】", bucketName, objectName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }finally {
            if (ObjectUtil.isNotNull(ossObject)) {
                IoUtil.close(ossObject);
            }
        }
    }

    @Override
    public File downloadFile(String bucketName, String objectName, String localFileName) throws AliyunOssClientException {
        Assert.notBlank(localFileName, ErrorEnum.FILE_NAME_CANNOT_BE_EMPTY.getMessage());
        try {
            File file = new File(localFileName);
            getOssClient().getObject(new GetObjectRequest(bucketName, objectName), file);
            return file;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】下载到本地文件【%s】, 异常【%s】",
                    bucketName, objectName, localFileName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public File downloadFile(String bucketName, String objectName, File desFile) throws AliyunOssClientException {
        try {
            getOssClient().getObject(new GetObjectRequest(bucketName, objectName), desFile);
            return desFile;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】下载到本地文件【%s】, 异常【%s】",
                    bucketName, objectName, desFile.getName(), e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean downloadFile(String bucketName, String objectName, File localFile, ProgressCallBack progressCallBack) {
        try {

            FileProgressListener fileProgressListener = new FileProgressListener(progressCallBack, objectName);
            getOssClient().getObject(new GetObjectRequest(bucketName, objectName).<GetObjectRequest>withProgressListener(fileProgressListener), localFile);
            return fileProgressListener.isSucceed();
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】是否成功下载到本地文件【%s】, 异常【%s】",
                    bucketName, objectName, localFile.getName(), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean doesObjectExist(String bucketName, String objectName) {
        try {
            return getOssClient().doesObjectExist(bucketName, objectName);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】是否存在, 异常【%s】", bucketName, objectName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setObjectAcl(String bucketName, String objectName, CannedAccessControlList cannedAccessControlList) {
        try {
            getOssClient().setObjectAcl(bucketName, objectName, cannedAccessControlList);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】设置文件访问权限【%s】, 异常【%s】",
                    bucketName, objectName, JSON.toJSONString(cannedAccessControlList), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public ObjectPermission getObjectAcl(String bucketName, String objectName) throws AliyunOssClientException {
        try {
            ObjectAcl objectAcl = getOssClient().getObjectAcl(bucketName, objectName);
            return objectAcl.getPermission();
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】获取文件访问权限, 异常【%s】", bucketName, objectName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public ObjectMetadata getObjectMetadata(String bucketName, String objectName) throws AliyunOssClientException {
        try {
            return getOssClient().getObjectMetadata(bucketName, objectName);
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】获取文件的全部元信息, 异常【%s】", bucketName, objectName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean putObjectStorageClass(String bucketName, String objectName, StorageClass storageClass) {
        CopyObjectRequest request = new CopyObjectRequest(bucketName, objectName, bucketName, objectName) ;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setHeader("x-oss-storage-class", storageClass);
        request.setNewObjectMetadata(objectMetadata);

        try {
            if (restoreObject(bucketName, objectName)) {
                getOssClient().copyObject(request);
                return Boolean.TRUE;
            }
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】转换文件存储类型【%s】, 异常【%s】",
                    bucketName, objectName, JSON.toJSONString(storageClass), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean restoreObject(String bucketName, String objectName) {
        try {

            ObjectMetadata objectMetadata = getOssClient().getObjectMetadata(bucketName, objectName);

            StorageClass storageClass = objectMetadata.getObjectStorageClass();
            if (storageClass == StorageClass.Archive) {
                getOssClient().restoreObject(bucketName, objectName);
                do {
                    Thread.sleep(1000);
                    objectMetadata = getOssClient().getObjectMetadata(bucketName, objectName);
                } while (!objectMetadata.isRestoreCompleted());
            }

            return objectMetadata.isRestoreCompleted();
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】解冻文件, 异常【%s】", bucketName, objectName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean deleteObject(String bucketName, String objectName) {
        try {
            getOssClient().deleteObject(bucketName, objectName);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】删除文件【%s】, 异常【%s】", bucketName, objectName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public List<String> deleteObject(String bucketName, List<String> objectNames) {
        if (CollUtil.isEmpty(objectNames)) {
            log.error("bucket【{}】待删除文件列表不能为空", bucketName);
            throw new AliyunOssClientException(ErrorEnum.THE_FILE_TO_BE_DELETED_CANNOT_BE_EMPTY.getMessage());
        }

        try {
            DeleteObjectsResult deleteObjectsResult = getOssClient()
                    .deleteObjects(new DeleteObjectsRequest(bucketName).withQuiet(Boolean.TRUE).withKeys(objectNames));

            return deleteObjectsResult.getDeletedObjects();
        }catch (Exception e) {
            log.error(String.format("bucket【%s】批量删除文件【%s】, 异常【%s】", bucketName, JSON.toJSONString(objectNames), e.getMessage()), e);
            log.error("deleteObject {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean copyObject(String sourceBucketName, String sourceObjectName,
                              String destinationBucketName, String destinationObjectName) {
        try {
            getOssClient().copyObject(sourceBucketName, sourceObjectName,
                    destinationBucketName, destinationObjectName);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("原bucket【%s】object【%s】复制到新bucket【%s】object【%s】, 异常【%s】",
                    sourceBucketName, sourceObjectName, destinationBucketName, destinationObjectName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean copyBigObject(String sourceBucketName, String sourceObjectName,
                                 String destinationBucketName, String destinationObjectName) {
        try {
            ObjectMetadata objectMetadata = getOssClient().getObjectMetadata(sourceBucketName, sourceObjectName);
            long contentLength = objectMetadata.getContentLength();
            long partSize = FileConstant.PART_SIZE;

            int partCount = (int) (contentLength / partSize);
            if (contentLength % partSize != 0) {
                partCount++;
            }

            InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(destinationBucketName, destinationObjectName);
            InitiateMultipartUploadResult initiateMultipartUploadResult = getOssClient().initiateMultipartUpload(initiateMultipartUploadRequest);
            String uploadId = initiateMultipartUploadResult.getUploadId();

            List<PartETag> partETags = new ArrayList<>();

            for (int i = 0; i < partCount; i++) {
                long skipBytes = partSize * i;
                long size = Math.min(partSize, contentLength - skipBytes);

                UploadPartCopyRequest uploadPartCopyRequest = new UploadPartCopyRequest(sourceBucketName, sourceObjectName, destinationBucketName, destinationObjectName);
                uploadPartCopyRequest.setUploadId(uploadId);
                uploadPartCopyRequest.setPartSize(size);
                uploadPartCopyRequest.setBeginIndex(skipBytes);
                uploadPartCopyRequest.setPartNumber(i + 1);
                UploadPartCopyResult uploadPartCopyResult = getOssClient().uploadPartCopy(uploadPartCopyRequest);

                partETags.add(uploadPartCopyResult.getPartETag());
            }

            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(
                    destinationBucketName, destinationObjectName, uploadId, partETags);
            getOssClient().completeMultipartUpload(completeMultipartUploadRequest);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("原bucket【%s】object【%s】复制到新bucket【%s】object【%s】, 异常【%s】",
                    sourceBucketName, sourceObjectName, destinationBucketName, destinationObjectName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean setObjectTagging(String bucketName, String objectName, Map<String, String> tags) {
        try {
            getOssClient().setObjectTagging(bucketName, objectName, tags);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】设置标签【%s】, 异常【%s】",
                    bucketName, objectName, JSON.toJSONString(tags), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public Map<String, String> getObjectTagging(String bucketName, String objectName) {
        try {
            TagSet tagSet = getOssClient().getObjectTagging(bucketName, objectName);
            return tagSet.getAllTags();
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】设置标签, 异常【%s】", bucketName, objectName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public boolean deleteObjectTagging(String bucketName, String objectName) {
        try {
            getOssClient().deleteObjectTagging(bucketName, objectName);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】删除标签, 异常【%s】", bucketName, objectName, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public FileInfo speedLimitUploadFile(String bucketName, String directory, File file, Integer limitSpeed, Map<String, String> tags) throws AliyunOssClientException {
        if (!FileUtil.exist(file)) {
            log.error("********,speedLimitUploadFile(),bucket【{}】 directory【{}】file does not exist", bucketName, directory);
            throw new AliyunOssClientException(ErrorEnum.FILE_DOES_NOT_EXIST.getMessage());
        }
        return this.speedLimitUploadFile(bucketName, directory, FileUtil.getInputStream(file), limitSpeed, tags, FileUtil.extName(file));
    }

    @Override
    public FileInfo speedLimitUploadFile(String bucketName, String directory, byte[] bytes, Integer limitSpeed, Map<String, String> tags) throws AliyunOssClientException {
        if (ArrayUtil.isEmpty(bytes)) {
            log.error("********,speedLimitUploadFile(),bucket【{}】 directory【{}】Bytes cannot be empty", bucketName, directory);
            throw new AliyunOssClientException(ErrorEnum.BYTES_CANNOT_BE_EMPTY.getMessage());
        }
        return this.speedLimitUploadFile(bucketName, directory, new ByteArrayInputStream(bytes), limitSpeed, tags, FileConstant.JPEG_SUFFIX);
    }

    @Override
    public FileInfo speedLimitUploadFile(String bucketName, String directory, InputStream inputStream, Integer limitSpeed, Map<String, String> tags) throws AliyunOssClientException {
        return this.speedLimitUploadFile(bucketName, directory, inputStream, limitSpeed, tags, FileConstant.JPEG_SUFFIX);
    }

    @Override
    public FileInfo speedLimitUploadFile(String bucketName, String directory, File file, Map<String, String> tags) throws AliyunOssClientException {
        if (!FileUtil.exist(file)) {
            log.error("********,speedLimitUploadFile(),bucket【{}】 directory【{}】file does not exist", bucketName, directory);
            throw new AliyunOssClientException(ErrorEnum.FILE_DOES_NOT_EXIST.getMessage());
        }
        return this.speedLimitUploadFile(bucketName, directory, FileUtil.getInputStream(file), FileConstant.LIMIT_SPEED, tags, FileUtil.extName(file));
    }

    @Override
    public FileInfo speedLimitUploadFile(String bucketName, String directory, byte[] bytes, Map<String, String> tags) throws AliyunOssClientException {
        if (ArrayUtil.isEmpty(bytes)) {
            log.error("********,speedLimitUploadFile(),bucket【{}】 directory【{}】Bytes cannot be empty", bucketName, directory);
            throw new AliyunOssClientException(ErrorEnum.BYTES_CANNOT_BE_EMPTY.getMessage());
        }
        return this.speedLimitUploadFile(bucketName, directory, new ByteArrayInputStream(bytes), FileConstant.LIMIT_SPEED, tags, FileConstant.JPEG_SUFFIX);
    }

    @Override
    public FileInfo speedLimitUploadFile(String bucketName, String directory, InputStream inputStream, Map<String, String> tags) throws AliyunOssClientException {
        return this.speedLimitUploadFile(bucketName, directory, inputStream, FileConstant.LIMIT_SPEED, tags, FileConstant.JPEG_SUFFIX);
    }

    @Override
    public FileInfo speedLimitUploadFile(String bucketName, String directory, byte[] bytes, Integer limitSpeed, Map<String, String> tags, String extName) throws AliyunOssClientException {
        if (ArrayUtil.isEmpty(bytes)) {
            log.error("********,speedLimitUploadFile(),bucket【{}】 directory【{}】file does not exist", bucketName, directory);
            throw new AliyunOssClientException(ErrorEnum.FILE_DOES_NOT_EXIST.getMessage());
        }
        return this.speedLimitUploadFile(bucketName, directory, new ByteArrayInputStream(bytes), limitSpeed, tags, extName);
    }

    @Override
    public FileInfo speedLimitUploadFile(String bucketName, String directory, InputStream inputStream, Integer limitSpeed, Map<String, String> tags, String extName) throws AliyunOssClientException {
        Assert.notBlank(bucketName, ErrorEnum.BUCKET_NAME_CANNOT_BE_NULL.getMessage());
        Assert.notBlank(directory, ErrorEnum.DIRECTORY_CANNOT_BE_EMPTY.getMessage());
        Assert.notNull(inputStream, ErrorEnum.THE_INPUT_STREAM_CANNOT_BE_EMPTY.getMessage());
        if (Validator.isNull(limitSpeed) || limitSpeed <= 0) {
            limitSpeed = FileConstant.LIMIT_SPEED;
        }
        limitSpeed = limitSpeed * 8;
        String fileName = getFileName(directory, extName);
        if (!bucketApi.doesBucketExist(bucketName)) {
            bucketApi.createBucket(bucketName);
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream);
        putObjectRequest.setTrafficLimit(limitSpeed);

        if (ObjectUtil.isNotNull(tags) && CollectionUtil.isNotEmpty(tags) &&tags.size() > 0) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setObjectTagging(tags);
            putObjectRequest.setMetadata(metadata);
        }

        try {

            PutObjectResult putObjectResult = getOssClient().putObject(putObjectRequest);

            FileInfo FileInfo = new FileInfo();
            FileInfo.setVersionId(putObjectResult.getVersionId());
            FileInfo.setUrl(fileName);

            return FileInfo;
        }catch (Exception e) {
            log.error(String.format("限速上传文件 bucket【%s】directory【%s】, 异常 【%s】", bucketName, directory, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public FileInfo speedLimitUploadFile(String bucketName, String directory, byte[] bytes, Map<String, String> tags, String extName) throws AliyunOssClientException {
        if (ArrayUtil.isEmpty(bytes)) {
            log.error("********,speedLimitUploadFile(),bucket【{}】 directory【{}】Bytes cannot be empty", bucketName, directory);
            throw new AliyunOssClientException(ErrorEnum.BYTES_CANNOT_BE_EMPTY.getMessage());
        }
        return this.speedLimitUploadFile(bucketName, directory, new ByteArrayInputStream(bytes), tags, extName);
    }

    @Override
    public FileInfo speedLimitUploadFile(String bucketName, String directory, InputStream inputStream, Map<String, String> tags, String extName) throws AliyunOssClientException {
        return this.speedLimitUploadFile(bucketName, directory, inputStream, FileConstant.LIMIT_SPEED, tags, extName);
    }

    @Override
    public boolean speedLimitDownloadFile(String bucketName, String objectName, Integer limitSpeed, File desFile) {
        if (Validator.isNull(limitSpeed) || limitSpeed <= 0) {
            limitSpeed = FileConstant.LIMIT_SPEED;
        }
        limitSpeed = limitSpeed * 8;
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectName);
            getObjectRequest.setTrafficLimit(limitSpeed);
            getOssClient().getObject(getObjectRequest, desFile);
            return Boolean.TRUE;
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】限速下载到文件【%s】, 异常【%s】",
                    bucketName, objectName, desFile.getName(), e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean speedLimitDownloadFile(String bucketName, String objectName, File desFile) {
        return this.speedLimitDownloadFile(bucketName, objectName, FileConstant.LIMIT_SPEED, desFile);
    }

    @Override
    public byte[] speedLimitDownloadFile(String bucketName, String objectName, Integer limitSpeed) throws AliyunOssClientException {
        if (Validator.isNull(limitSpeed) || limitSpeed <= 0) {
            limitSpeed = FileConstant.LIMIT_SPEED;
        }
        limitSpeed = limitSpeed * 8;
        try {

            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectName);
            getObjectRequest.setTrafficLimit(limitSpeed);
            OSSObject ossObject = getOssClient().getObject(getObjectRequest);
            return IoUtil.readBytes(ossObject.getObjectContent());
        }catch (Exception e) {
            log.error(String.format("bucket【%s】object【%s】限速下载文件, 异常【%s】", bucketName, objectName, e.getMessage()), e);
            throw new AliyunOssClientException(exception(e));
        }
    }

    @Override
    public byte[] speedLimitDownloadFile(String bucketName, String objectName) throws AliyunOssClientException {
        return this.speedLimitDownloadFile(bucketName, objectName, FileConstant.LIMIT_SPEED);
    }



}
