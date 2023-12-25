package com.xunmaw.cms.common.config;

import cn.hutool.core.util.StrUtil;
import com.xunmaw.cms.common.config.properties.FileUploadProperties;
import com.xunmaw.cms.common.config.properties.StaticizeProperties;
import com.xunmaw.cms.common.intercepter.CommonDataInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Nobita
 * @date 2020/4/18 11:58 上午
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({FileUploadProperties.class, StaticizeProperties.class})
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileUploadProperties fileUploadProperties;
    private final StaticizeProperties staticizeProperties;
    private final CommonDataInterceptor commonDataInterceptor;

    /**
     * 配置本地文件上传的虚拟路径和静态化的文件生成路径
     * 备注：这是一种图片上传访问图片的方法，实际上也可以使用nginx反向代理访问图片
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 文件上传
        String uploadFolder = fileUploadProperties.getUploadFolder();
        uploadFolder = StrUtil.appendIfMissing(uploadFolder, File.separator);
        registry.addResourceHandler(fileUploadProperties.getAccessPathPattern())
                .addResourceLocations("file:" + uploadFolder);
        // 静态化
        String staticFolder = staticizeProperties.getFolder();
        staticFolder = StrUtil.appendIfMissing(staticFolder, File.separator);
        registry.addResourceHandler(staticizeProperties.getAccessPathPattern())
                .addResourceLocations("file:" + staticFolder);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonDataInterceptor).addPathPatterns("/**");
    }
}
