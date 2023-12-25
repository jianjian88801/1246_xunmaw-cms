package com.xunmaw.cms.module.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xunmaw.cms.common.config.properties.FileUploadProperties;
import com.xunmaw.cms.common.util.*;
import com.xunmaw.cms.enums.SysConfigKey;
import com.xunmaw.cms.exception.UploadFileNotFoundException;
import com.xunmaw.cms.module.admin.service.OssService;
import com.xunmaw.cms.module.admin.service.SysConfigService;
import com.xunmaw.cms.module.admin.vo.CloudStorageConfigVo;
import com.xunmaw.cms.module.admin.vo.UploadResponse;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @author linzhaoguan
 * @date 2020/3/31 2:41 下午
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    protected SysConfigService sysConfigService;
    @Autowired
    private FileUploadProperties fileUploadProperties;

    @Override
    @SneakyThrows
    public UploadResponse upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new UploadFileNotFoundException(UploadResponse.ErrorEnum.FILE_NOT_FOUND.msg);
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String value = sysConfigService.selectAll().get(SysConfigKey.CLOUD_STORAGE_CONFIG.getValue());
        CloudStorageConfigVo cloudStorageConfig = JSON.parseObject(value, CloudStorageConfigVo.class);

        String md5 = MD5.getMessageDigest(file.getBytes());
        String dir;
        String filePath;
        String domain;
        String url = null;
        ResponseVo<?> responseVo;
        switch (cloudStorageConfig.getType()) {
            case CoreConst.UPLOAD_TYPE_QINIUYUN:
                domain = cloudStorageConfig.getQiniuDomain();
                dir = cloudStorageConfig.getQiniuPrefix();
                filePath = String.format("%1$s/%2$s%3$s", dir, md5, suffix);
                responseVo = QiNiuYunUtil.uploadFile(cloudStorageConfig, filePath, file.getBytes());
                url = String.format("%1$s/%2$s", domain, filePath);
                break;
            case CoreConst.UPLOAD_TYPE_ALIYUN:
                domain = cloudStorageConfig.getAliyunDomain();
                dir = cloudStorageConfig.getAliyunPrefix();
                filePath = String.format("%1$s/%2$s%3$s", dir, md5, suffix);
                responseVo = AliYunUtil.uploadFile(cloudStorageConfig, filePath, file.getBytes());
                url = String.format("%1$s/%2$s", domain, filePath);
                break;
            case CoreConst.UPLOAD_TYPE_LOCAL:
                String relativePath = FileUploadUtil.uploadLocal(file, fileUploadProperties.getUploadFolder());
                String accessPrefixUrl = fileUploadProperties.getAccessPrefixUrl();
                if (!StrUtil.endWith(accessPrefixUrl, "/")) {
                    accessPrefixUrl += '/';
                }
                url = accessPrefixUrl + relativePath;
                responseVo = ResultUtil.success();
                break;
            default:
                responseVo = ResultUtil.error("未配置云存储类型");
        }

        if (responseVo.getStatus().equals(CoreConst.SUCCESS_CODE)) {
            return UploadResponse.success(url, originalFilename, suffix, url, CoreConst.SUCCESS_CODE);
        } else {
            return UploadResponse.failed(originalFilename, CoreConst.FAIL_CODE, responseVo.getMsg());
        }
    }

    @Override
    @Cacheable(value = "oss", key = "'config'")
    public CloudStorageConfigVo getOssConfig() {
        String value = sysConfigService.selectAll().get(SysConfigKey.CLOUD_STORAGE_CONFIG.getValue());
        return JSON.parseObject(value, CloudStorageConfigVo.class);
    }

    @Override
    @CacheEvict(value = "oss", key = "'config'")
    public boolean updateOssConfig(CloudStorageConfigVo cloudStorageConfig) {
        String value = JSON.toJSONString(cloudStorageConfig);
        return sysConfigService.updateByKey(SysConfigKey.CLOUD_STORAGE_CONFIG.getValue(), value);
    }

    @Override
    public int getOssConfigType() {
        return getOssConfig().getType();
    }


}
