package com.yong.user.web;

import com.yong.user.entity.UserInfo;
import com.yong.user.service.UserServiceImpl;
import com.yong.service.commons.core.data.ResultHandler;
import com.yong.user.provider.UserService;
import com.yong.user.provider.dto.UserInfoDto;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController implements UserService {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/user/id/{id}")
    public ResultHandler<UserInfoDto> getUserById(@PathVariable long id) {
        UserInfo userInfo = userService.getUserById(id);
        return ResultHandler.success(toDto(userInfo));
    }

    @Override
    @GetMapping("/user/username/{username}")
    public ResultHandler<UserInfoDto> getUserByName(@PathVariable String username) {
        UserInfo userInfo = userService.getUserByName(username);
        return ResultHandler.success(toDto(userInfo));
    }

    @Override
    @PostMapping("/user/register")
    public ResultHandler<Long> registerUser(@RequestBody UserInfoDto userInfoDto) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoDto, userInfo);
        long userId = userService.registerUser(userInfo);
        return ResultHandler.success(userId);
    }

    private UserInfoDto toDto(UserInfo userInfo) {
        if(userInfo == null) {
            return null;
        }
        UserInfoDto dto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, dto);
        return dto;
    }
}
