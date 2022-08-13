package com.yong.message.service.web;

import com.yong.message.service.model.MessageRequest;
import com.yong.message.service.provider.MessageService;
import com.yong.service.commons.core.data.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController implements MessageService {
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @GetMapping("/message/mobile")
    public ResultHandler<Boolean> sendMobileMessage(MessageRequest request) {
        String mobile = request.getMobile();
        String message = request.getMessage();
        logger.info("mobile: {}, message: {}", mobile, message);
        return ResultHandler.success(Boolean.TRUE);
    }

}
