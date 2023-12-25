package com.xunmaw.cms.module.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Data
public class UploadResponse {

    private String fileName;
    private String originalFileName;
    private Long size;
    private String type;
    private String url;
    private Integer status;
    private String msg;

    private UploadResponse(String fileName, String originalFileName, String type, String url, Integer status) {
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.type = type;
        this.url = url;
        this.status = status;
    }

    private UploadResponse(String originalFileName, Integer status, String msg) {
        this.originalFileName = originalFileName;
        this.status = status;
        this.msg = msg;
    }

    public static UploadResponse success(String fileName, String originalFileName, String type, String url, Integer status) {
        return new UploadResponse(fileName, originalFileName, type, url, status);
    }

    public static UploadResponse failed(String originalFileName, Integer status, String msg) {
        return new UploadResponse(originalFileName, status, msg);
    }

    @AllArgsConstructor
    public enum ErrorEnum {
        //无
        NONE("None"),
        // 文件超出大小限制
        OVER_SIZE("File Size largger than the maximum"),
        // 文件格式不合规范
        ILLEGAL_EXTENSION("Unsupported file type"),
        // 文件不存在
        FILE_NOT_FOUND("File Not Found");

        public final String msg;

    }
}
