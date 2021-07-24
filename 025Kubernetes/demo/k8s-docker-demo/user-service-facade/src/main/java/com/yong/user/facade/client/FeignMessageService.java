package com.yong.user.facade.client;

import com.yong.message.service.provider.MessageService;
import com.yong.service.commons.core.data.ResultHandler;
import feign.Param;
import feign.RequestLine;

public interface FeignMessageService extends MessageService {
    @Override
    @RequestLine("GET /message/mobile/{mobile}?message={message}")
    ResultHandler<Boolean> sendMobileMessage(@Param("mobile") String mobile, @Param("message") String message);

    @Override
    @RequestLine("POST /message/email/{email}")
    ResultHandler<Boolean> sendEmailMessage(@Param("email") String email, String message);
}
