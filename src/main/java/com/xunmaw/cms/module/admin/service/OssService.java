package com.xunmaw.cms.module.admin.service;

import com.xunmaw.cms.module.admin.vo.CloudStorageConfigVo;
import com.xunmaw.cms.module.admin.vo.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @author linzhaoguan
 * @date 2020/3/31 11:12 上午
 */
public interface OssService {

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    UploadResponse upload(MultipartFile file);

    /**
     * 获取云存储配置内容
     *
     * @return
     */
    CloudStorageConfigVo getOssConfig();

    /**
     * 更新云存储配置内容
     *
     * @param cloudStorageConfig
     * @return
     */
    boolean updateOssConfig(CloudStorageConfigVo cloudStorageConfig);

    /**
     * 获取文件存储方式
     * @return
     */
    int getOssConfigType();
}
