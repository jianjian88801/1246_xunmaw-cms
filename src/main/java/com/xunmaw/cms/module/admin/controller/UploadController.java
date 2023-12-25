package com.xunmaw.cms.module.admin.controller;

import cn.hutool.core.map.MapUtil;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.ResultUtil;
import com.xunmaw.cms.module.admin.service.OssService;
import com.xunmaw.cms.module.admin.vo.CloudStorageConfigVo;
import com.xunmaw.cms.module.admin.vo.UploadResponse;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 后台文件上传接口、云存储配置
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Slf4j
@Controller
@RequestMapping("/attachment")
@AllArgsConstructor
public class UploadController {

    private final OssService ossService;


    @ResponseBody
    @PostMapping("/upload")
    public UploadResponse upload(@RequestParam(value = "file") MultipartFile file) {
        return ossService.upload(file);
    }

    /**
     * 上传成功的返回格式：
     * {
     *     "errno": 0, // 注意：值是数字，不能是字符串
     *     "data": {
     *         "url": "xxx", // 图片 src ，必须
     *         "alt": "yyy", // 图片描述文字，非必须
     *         "href": "zzz" // 图片的链接，非必须
     *     }
     * }
     * 上传失败的返回格式：
     * {
     *     "errno": 1, // 只要不等于 0 就行
     *     "message": "失败信息"
     * }
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping("/uploadForEditor")
    public Object upload2QiniuForMd(@RequestParam(value = "file")MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<>(2);
        UploadResponse responseVo = upload(file);
        if (CoreConst.SUCCESS_CODE.equals(responseVo.getStatus())) {
            resultMap.put("errno", 0);
            resultMap.put("data", MapUtil.builder().put("url", responseVo.getUrl()).build());
            return resultMap;
        } else {
            resultMap.put("errno", 1);
            resultMap.put("message", "上传失败!" + responseVo.getMsg());
        }
        return resultMap;
    }

    @GetMapping("/config")
    public String config(Model model) {
        model.addAttribute("cloudStorageConfig", ossService.getOssConfig());
        return CoreConst.ADMIN_PREFIX + "upload/config";
    }

    @ResponseBody
    @PostMapping("/saveConfig")
    public ResponseVo saveConfig(CloudStorageConfigVo cloudStorageConfig) {
        boolean success = ossService.updateOssConfig(cloudStorageConfig);
        if (success) {
            return ResultUtil.success("云存储配置保存成功！");
        } else {
            return ResultUtil.error("云存储配置保存失败！");
        }
    }

}
