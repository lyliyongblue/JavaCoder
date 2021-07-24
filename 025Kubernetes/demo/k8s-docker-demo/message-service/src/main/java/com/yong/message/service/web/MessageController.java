package com.yong.message.service.web;

import com.yong.message.service.provider.MessageService;
import com.yong.message.service.service.MessageServiceImpl;
import com.yong.service.commons.core.data.ResultHandler;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController implements MessageService {
    private final MessageServiceImpl messageService;

    public MessageController(MessageServiceImpl messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/message/mobile/{mobile}")
    public ResultHandler<Boolean> sendMobileMessage(@PathVariable String mobile, String message) {
        boolean result = messageService.sendMobileMessage(mobile, message);
        return ResultHandler.success(result);
    }

    @GetMapping("/message/email/{email}")
    public ResultHandler<Boolean> sendEmailMessage(@PathVariable String email, String message) {
        boolean result = messageService.sendEmailMessage(email, message);
        return ResultHandler.success(result);
    }

}
