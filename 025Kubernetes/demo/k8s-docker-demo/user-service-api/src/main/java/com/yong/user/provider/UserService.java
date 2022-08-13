package com.yong.user.provider;

import com.yong.service.commons.core.data.ResultHandler;
import com.yong.user.provider.dto.UserInfoDto;

public interface UserService {

    ResultHandler<UserInfoDto> getUserById(long id);

    ResultHandler<UserInfoDto> getUserByName(String username);

    ResultHandler<Long> registerUser(UserInfoDto userInfo);
}
