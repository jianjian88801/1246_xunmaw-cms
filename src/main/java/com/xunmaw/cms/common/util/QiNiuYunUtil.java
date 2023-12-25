package com.xunmaw.cms.common.util;

import com.alibaba.fastjson.JSON;
import com.xunmaw.cms.module.admin.vo.CloudStorageConfigVo;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 七牛云存储工具类
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Slf4j
@UtilityClass
public class QiNiuYunUtil {

    /**
     * 上传文件
     * @param cloudStorageConfig
     * @param filePath
     * @param uploadBytes
     * @return
     */
    public static ResponseVo<?> uploadFile(CloudStorageConfigVo cloudStorageConfig, String filePath, byte[] uploadBytes) {
        // 上传文件到七牛云
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration();
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = cloudStorageConfig.getQiniuAccessKey();
        String secretKey = cloudStorageConfig.getQiniuSecretKey();
        String bucket = cloudStorageConfig.getQiniuBucketName();
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            uploadManager.put(uploadBytes, filePath, upToken);
            return ResultUtil.success("上传成功");
        } catch (QiniuException e) {
            log.error("七牛云-上传文件发生异常", e);
            Response r = e.response;
            try {
                return ResultUtil.error(r.bodyString());
            } catch (QiniuException ex) {
                return ResultUtil.error("七牛云异常！", ex.error());
            }
        }
    }

    /**
     * 删除文件
     * @param cloudStorageConfigVo
     * @param filePath
     * @return
     */
    public static boolean deleteFile(CloudStorageConfigVo cloudStorageConfigVo, String filePath) {
        Configuration cfg = new Configuration();
        String accessKey = cloudStorageConfigVo.getQiniuAccessKey();
        String secretKey = cloudStorageConfigVo.getQiniuSecretKey();
        String bucket = cloudStorageConfigVo.getQiniuBucketName();
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, filePath);
            return true;
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            log.error("七牛云-删除文件发生异常", ex);
        }
        return false;
    }

    /**
     * 批量删除文件
     * @param cloudStorageConfigVo
     * @param keyList
     * @return
     */
    public static boolean deleteFileBatch(CloudStorageConfigVo cloudStorageConfigVo, String[] keyList) {
        Configuration cfg = new Configuration();
        String accessKey = cloudStorageConfigVo.getQiniuAccessKey();
        String secretKey = cloudStorageConfigVo.getQiniuSecretKey();
        String bucket = cloudStorageConfigVo.getQiniuBucketName();
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //单次批量请求的文件数量不得超过1000
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                /*key对应的状态*/
                if (status.code == 200) {
                    //
                } else {
                    //
                }
            }
            return true;
        } catch (QiniuException ex) {
            log.error("七牛云-批量删除文件发生异常", ex);
        }
        return false;
    }

}
