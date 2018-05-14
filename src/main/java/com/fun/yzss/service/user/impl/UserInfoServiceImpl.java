package com.fun.yzss.service.user.impl;

import com.fun.yzss.model.user.User;
import com.fun.yzss.service.user.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * Created by fanqq on 2016/7/29.
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public User getUser(String name) {
        return null;
    }

    @Override
    public User getUser(Long id) {
        return null;
    }

    @Override
    public User getUserByAuthTypeAndOpenId(String type, String openid) {
        return null;
    }
}
