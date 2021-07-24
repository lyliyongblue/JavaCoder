package com.yong.user.service;

import com.yong.service.commons.core.exception.BusinessException;
import com.yong.user.dao.UserInfoDao;
import com.yong.user.entity.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl {
    private final UserInfoDao userInfoDao;

    public UserServiceImpl(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    public UserInfo getUserById(long id) {
        return userInfoDao.selectByPrimaryKey(id);
    }

    public UserInfo getUserByName(String username) {
        return userInfoDao.selectByUsername(username);
    }

    @Transactional
    public long registerUser(UserInfo userInfo) {
        UserInfo existUserInfo = getUserByName(userInfo.getUsername());
        if(existUserInfo != null) {
            throw new BusinessException("用户名已存在");
        }
        userInfoDao.insertSelective(userInfo);
        return userInfo.getId();
    }
}
