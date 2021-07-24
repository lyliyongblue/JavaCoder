package com.yong.user.facade.config;

import com.yong.user.facade.client.FeignMessageService;
import com.yong.user.facade.client.FeignUserService;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Value("${feign.client.user-url}")
    private String userUrl;

    @Value("${feign.client.message-url}")
    private String messageUrl;

    @Bean
    public FeignUserService getFeignUserService() {
        return Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .retryer(Retryer.NEVER_RETRY)
                .target(FeignUserService.class, userUrl);
    }

    @Bean
    public FeignMessageService getFeignMessageService() {
        return Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .retryer(Retryer.NEVER_RETRY)
                .target(FeignMessageService.class, messageUrl);
    }
}
