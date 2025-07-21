package com.hutu.shortlinkcommon.config;

import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.DownloadObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class MinioUtil {

    @Resource
    private MinioClient minioClient;

    /**
     * 检查存储桶是否存在
     * @param bucketName 存储桶名称
     * @return 是否存在
     */
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new RuntimeException("检查存储桶是否存在失败", e);
        }
    }

    /**
     * 创建存储桶
     * @param bucketName 存储桶名称
     */
    public void createBucket(String bucketName) {
        try {
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("创建存储桶失败", e);
        }
    }

    /**
     * 删除存储桶
     * @param bucketName 存储桶名称
     */
    public void deleteBucket(String bucketName) {
        try {
            if (bucketExists(bucketName)) {
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("删除存储桶失败", e);
        }
    }

    /**
     * 获取所有存储桶列表
     * @return 存储桶列表
     */
    public List<Bucket> listBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException("获取存储桶列表失败", e);
        }
    }

    /**
     * 文件上传
     * @param file       上传文件
     * @param bucketName 存储桶名称
     * @return 文件路径
     */
    public String uploadFile(MultipartFile file, String bucketName) {
        // 获取原始文件名以提取扩展名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 生成文件名：年月日-UUID
        String newFileName = LocalDate.now().toString().replace("-", "") + "-" + UUID.randomUUID() + fileExtension;
        // 调用上传方法并返回预览地址
        return uploadFile(file, bucketName, newFileName);
    }

    /**
     * 文件上传（指定文件名）
     * @param file       上传文件
     * @param bucketName 存储桶名称
     * @param fileName   存储文件名
     * @return 文件路径
     */
    public String uploadFile(MultipartFile file, String bucketName, String fileName) {
        try {
            createBucket(bucketName);
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
                return getPreviewUrl(bucketName, fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 文件上传（字节数组）
     * @param bucketName 存储桶名称
     * @param fileName   存储文件名
     * @param bytes      文件字节数组
     * @param contentType 文件类型
     * @return 文件路径
     */
    public String uploadFile(String bucketName, String fileName, byte[] bytes, String contentType) {
        try {
            createBucket(bucketName);
            try (InputStream inputStream = new java.io.ByteArrayInputStream(bytes)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, bytes.length, -1)
                                .contentType(contentType)
                                .build()
                );
                return fileName;
            }
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 获取文件流
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件流
     */
    public InputStream getFileStream(String bucketName, String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("获取文件流失败", e);
        }
    }

    /**
     * 下载文件到本地
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @param localPath  本地存储路径
     */
    public void downloadFile(String bucketName, String fileName, String localPath) {
        try {
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .filename(localPath)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }

    /**
     * 获取文件预览URL（有效期7天）
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 预览URL
     */
    public String getPreviewUrl(String bucketName, String fileName) {
        return getPreviewUrl(bucketName, fileName, 7, TimeUnit.DAYS);
    }

    /**
     * 获取文件预览URL（自定义有效期）
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @param duration   有效期数值
     * @param unit       有效期单位
     * @return 预览URL
     */
    public String getPreviewUrl(String bucketName, String fileName, int duration, TimeUnit unit) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(duration, unit)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("获取预览URL失败", e);
        }
    }

    /**
     * 删除文件
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     */
    public void deleteFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败", e);
        }
    }

    /**
     * 批量删除文件
     * @param bucketName 存储桶名称
     * @param fileNames  文件名列表
     */
    public void batchDeleteFiles(String bucketName, List<String> fileNames) {
        try {
            List<DeleteObject> objects = new ArrayList<>();
            for (String fileName : fileNames) {
                objects.add(new DeleteObject(fileName));
            }
            minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucketName)
                            .objects(objects)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("批量删除文件失败", e);
        }
    }

    /**
     * 获取文件信息
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件信息
     */
    public StatObjectResponse getFileInfo(String bucketName, String fileName) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("获取文件信息失败", e);
        }
    }

    /**
     * 列出存储桶中的所有对象
     * @param bucketName 存储桶名称
     * @return 对象列表
     */
    public List<Item> listObjects(String bucketName) {
        List<Item> items = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            
            for (Result<Item> result : results) {
                items.add(result.get());
            }
            return items;
        } catch (Exception e) {
            throw new RuntimeException("列出存储桶对象失败", e);
        }
    }

    /**
     * 复制文件
     * @param sourceBucket 源存储桶
     * @param sourceFile   源文件名
     * @param targetBucket 目标存储桶
     * @param targetFile   目标文件名
     */
    public void copyFile(String sourceBucket, String sourceFile, String targetBucket, String targetFile) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .source(CopySource.builder().bucket(sourceBucket).object(sourceFile).build())
                            .bucket(targetBucket)
                            .object(targetFile)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("复制文件失败", e);
        }
    }
}