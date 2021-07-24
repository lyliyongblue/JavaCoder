package com.yong.user.facade.client;

import com.yong.service.commons.core.data.ResultHandler;
import com.yong.user.provider.UserService;
import com.yong.user.provider.dto.UserInfoDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface FeignUserService extends UserService {
    @Override
    @RequestLine("GET /user/id/{id}")
    ResultHandler<UserInfoDto> getUserById(@Param("id") long id);

    @Override
    @RequestLine("GET /user/username/{username}")
    ResultHandler<UserInfoDto> getUserByName(@Param("username") String username);

    @Override
    @RequestLine("POST /user/register")
    @Headers("Content-Type: application/json")
    ResultHandler<Long> registerUser(UserInfoDto userInfo);
}
