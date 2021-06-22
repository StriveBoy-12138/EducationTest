package com.njbandou.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oss")
public class OssConfig {
    @Value("${oss.access-id}")
    private String accessId;

    @Value("${oss.access-key}")
    private String accessKey;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.bucket}")
    private String bucket;

    @Value("${oss.host}")
    private String host;

    @Value("${oss.callback-url}")
    private String callbackUrl;

    public String getAccessId() {
        return accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getBucket() {
        return bucket;
    }

    public String getHost() {
        return host;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
