package com.fun.yzss.model.user;

import java.util.Date;

/**
 * Created by fanqq on 2016/7/29.
 */
public class User {

    Long id;
    String authId;

    //认证相关数据
    String code;
    String accessToken;
    Long expiresIn;
    String refreshToken;
    String openid;
    String scope;

    //用户额外信息
    String nickName;
    Date lastLogin;
}
