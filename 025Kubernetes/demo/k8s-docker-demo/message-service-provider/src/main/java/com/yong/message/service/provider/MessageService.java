package com.yong.message.service.provider;

import com.yong.service.commons.core.data.ResultHandler;

public interface MessageService {
    ResultHandler<Boolean> sendMobileMessage(String mobile, String message);

    ResultHandler<Boolean> sendEmailMessage(String email, String message);
}
