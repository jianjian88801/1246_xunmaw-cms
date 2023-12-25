package com.xunmaw.cms.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Nobita
 * @date 2020/4/18 12:10 下午
 */
@Data
@ConfigurationProperties(prefix = "file")
public class FileUploadProperties {

    private String accessPathPattern = "/u/**";
    private String uploadFolder;
    private String accessPrefixUrl;
}
