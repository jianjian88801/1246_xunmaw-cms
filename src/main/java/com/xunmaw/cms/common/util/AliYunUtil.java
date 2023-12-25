package com.xunmaw.cms.common.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.xunmaw.cms.module.admin.vo.CloudStorageConfigVo;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author linzhaoguan
 * @date 2020/3/31 3:56 下午
 */
@Slf4j
@UtilityClass
public class AliYunUtil {

    public static ResponseVo<?> uploadFile(CloudStorageConfigVo cloudStorageConfig, String filePath, byte[] uploadBytes) {
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String endpoint = cloudStorageConfig.getAliyunEndPoint();
        String accessKeyId = cloudStorageConfig.getAliyunAccessKeyId();
        String accessKeySecret = cloudStorageConfig.getAliyunAccessKeySecret();
        String bucketName = cloudStorageConfig.getAliyunBucketName();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
        try {
            ossClient.putObject(bucketName, filePath, new ByteArrayInputStream(uploadBytes));
        } catch (OSSException | ClientException e) {
            log.error("阿里云-上传文件发生异常", e);
            return ResultUtil.error(e.getMessage());
        }
        // 关闭OSSClient。
        ossClient.shutdown();

        return ResultUtil.success("上传成功");
    }
}
