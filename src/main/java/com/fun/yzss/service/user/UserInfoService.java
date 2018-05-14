package com.fun.yzss.service.user;

import com.fun.yzss.model.user.User;

/**
 * Created by fanqq on 2016/7/29.
 */
public interface UserInfoService {
    User getUser(String name);

    User getUser(Long id);

    User getUserByAuthTypeAndOpenId(String type, String openid);
}
