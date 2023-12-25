package com.xunmaw.cms.exception;

/**
 * 自定义上传文件未找到异常
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public class UploadFileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -7812372861340782170L;

    public UploadFileNotFoundException() {
    }

    public UploadFileNotFoundException(String message) {
        super(message);
    }

}
