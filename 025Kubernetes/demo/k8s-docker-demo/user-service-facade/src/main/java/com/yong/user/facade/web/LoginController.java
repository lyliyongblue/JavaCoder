package com.yong.user.facade.web;

import com.yong.service.commons.core.exception.BusinessException;
import com.yong.user.facade.client.FeignUserService;
import com.yong.service.commons.core.data.ResultHandler;
import com.yong.user.facade.service.TokenService;
import com.yong.user.facade.vo.UserInfoVo;
import com.yong.user.provider.dto.UserInfoDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yong.user.facade.service.TokenService.CACHE_KEY_USER;

@RestController
public class LoginController {

    private final FeignUserService userService;
    private final TokenService tokenService;

    public LoginController(FeignUserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResultHandler<String> login(String username, String password) {
        if(StringUtils.isBlank(username)) {
            throw new BusinessException("账号不能为空");
        }
        if(StringUtils.isBlank(password)) {
            throw new BusinessException("密码不能为空");
        }
        ResultHandler<UserInfoDto> userInfoResult = userService.getUserByName(username);
        if(userInfoResult == null || !userInfoResult.isSuccess() || userInfoResult.getData() == null) {
            throw new BusinessException("账号/密码错误");
        }
        UserInfoDto userInfoDto = userInfoResult.getData();
        if(!userInfoDto.getPassword().equals(password)) {
            throw new BusinessException("账号/密码错误");
        }
        String token = tokenService.generateToken();
        tokenService.save(token, CACHE_KEY_USER, userInfoDto);
        return ResultHandler.success(token);
    }

    @GetMapping("/current_login_user")
    public ResultHandler<UserInfoVo> currentLoginUser(String token) {
        UserInfoDto userInfoDto = tokenService.getData(token, UserInfoDto.class);
        if(userInfoDto == null) {
            throw new BusinessException("用户未登录");
        }
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(userInfoDto, vo);
        return ResultHandler.success(vo);
    }

    @GetMapping("/has_login")
    public ResultHandler<Boolean> hasLogin(String token) {
        if(StringUtils.isBlank(token)) {
            return ResultHandler.success(Boolean.FALSE);
        }
        boolean hasToken = tokenService.hasToken(token);
        return ResultHandler.success(hasToken);
    }

}
