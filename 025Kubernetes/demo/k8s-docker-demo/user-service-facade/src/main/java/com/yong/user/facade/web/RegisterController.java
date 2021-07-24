package com.yong.user.facade.web;

import com.yong.service.commons.core.exception.BusinessException;
import com.yong.user.facade.client.FeignUserService;
import com.yong.user.facade.service.ValidateCodeService;
import com.yong.user.facade.vo.UserInfoVo;
import com.yong.service.commons.core.data.ResultCode;
import com.yong.service.commons.core.data.ResultHandler;
import com.yong.user.provider.dto.UserInfoDto;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final FeignUserService userService;
    private final ValidateCodeService validateCodeService;

    public RegisterController(FeignUserService userService,
                              ValidateCodeService validateCodeService) {
        this.userService = userService;
        this.validateCodeService = validateCodeService;
    }

    @PostMapping("/user/register")
    public ResultHandler<UserInfoVo> register(@RequestBody UserInfoVo vo) {
        String validateCode = vo.getValidateCode();
        String mobile = vo.getMobile();
        boolean checkResult = validateCodeService.checkCode(mobile, validateCode);
        if(!checkResult) {
            return ResultHandler.getInstance(ResultCode.ERROR.getCode(), "验证码错误或已经过期", null);
        }
        String username = vo.getUsername();
        ResultHandler<UserInfoDto> existUserInfoResult = userService.getUserByName(username);
        if(existUserInfoResult != null && existUserInfoResult.getData() != null) {
            throw new BusinessException("用户名已存在");
        }
        UserInfoDto dto = new UserInfoDto();
        BeanUtils.copyProperties(vo, dto);
        ResultHandler<Long> registerResult = userService.registerUser(dto);
        Long userId = ResultHandler.getResultData(registerResult);
        validateCodeService.removeCode(mobile);

        ResultHandler<UserInfoDto> userInfoResult = userService.getUserById(userId);
        UserInfoDto userInfoDto = ResultHandler.getResultData(userInfoResult);
        UserInfoVo result = new UserInfoVo();
        BeanUtils.copyProperties(userInfoDto, result);
        return ResultHandler.success(result);
    }
}
