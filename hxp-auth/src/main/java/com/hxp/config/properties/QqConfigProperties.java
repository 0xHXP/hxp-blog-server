package com.hxp.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * qq配置属性
 *
 * @author hxp
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "qq")
public class QqConfigProperties {

    /**
     * QQ appId
     */
    private String appId;
    /**
     * QQ appkey
     */
    private String appKey;
    /**
     * 回调地址
     */
    private String redirectUrl;

}
