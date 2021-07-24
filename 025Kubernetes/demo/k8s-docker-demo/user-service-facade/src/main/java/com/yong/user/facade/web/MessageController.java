package com.yong.user.facade.web;

import com.yong.user.facade.client.FeignMessageService;
import com.yong.user.facade.service.ValidateCodeService;
import com.yong.service.commons.core.data.ResultCode;
import com.yong.service.commons.core.data.ResultHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final FeignMessageService messageService;
    private final ValidateCodeService validateCodeService;

    public MessageController(FeignMessageService messageService,
                             ValidateCodeService validateCodeService) {
        this.messageService = messageService;
        this.validateCodeService = validateCodeService;
    }

    @PostMapping("/register/mobile/msg")
    public ResultHandler<Boolean> sendMobileRegisterMessage(String mobile) {
        if(StringUtils.isBlank(mobile)) {
            return ResultHandler.getInstance(ResultCode.ERROR.getCode(), "手机号码不能为空", Boolean.FALSE);
        }
        String validateCode = validateCodeService.generateCode();
        String message = generateMessage(validateCode);
        ResultHandler<Boolean> result = messageService.sendMobileMessage(mobile, message);
        if(result != null && result.isSuccess() && result.getData() != null && result.getData()) {
            validateCodeService.saveCode(mobile, validateCode);
        }
        return result;
    }


    private String generateMessage(String code) {
        return "您正在使用验证码操作X站点，您的验证码是：" + code + " 10分钟内有效";
    }

}
