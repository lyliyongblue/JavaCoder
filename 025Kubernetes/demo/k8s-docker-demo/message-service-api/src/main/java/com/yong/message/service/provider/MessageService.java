package com.yong.message.service.provider;


import com.yong.message.service.model.MessageRequest;
import com.yong.service.commons.core.data.ResultHandler;

public interface MessageService {
    ResultHandler<Boolean> sendMobileMessage(MessageRequest request);
}
