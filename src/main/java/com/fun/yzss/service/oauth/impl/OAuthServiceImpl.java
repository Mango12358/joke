package com.fun.yzss.service.oauth.impl;

import com.fun.yzss.service.oauth.OAuthService;
import com.fun.yzss.service.user.UserInfoService;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by fanqq on 2016/7/29.
 */
@Service("oAuthService")
public class OAuthServiceImpl implements OAuthService {

    @Resource
    UserInfoService userInfoService;

    DynamicStringProperty wxAuthUrl = DynamicPropertyFactory.getInstance().getStringProperty("wx.auth.url", "spring-context.xml");
    DynamicStringProperty qAuthUrl = DynamicPropertyFactory.getInstance().getStringProperty("q.auth.url", "spring-context.xml");


    @Override
    public boolean loginAuth(String authId, String refreshToken, String accessToken, String openId) throws Exception {
        return false;
    }

    @Override
    public boolean requestAuth(String authId, Long userId, String accessToken, String openId) throws Exception {
        return false;
    }

    @Override
    public boolean requestAuth(String authId, String userName, String accessToken, String openId) throws Exception {
        return false;
    }
}
