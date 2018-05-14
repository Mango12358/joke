package com.fun.yzss.service.oauth;

/**
 * Created by fanqq on 2016/7/29.
 */
public interface OAuthService {
    boolean loginAuth(String authId, String refreshToken, String accessToken, String openId) throws Exception;

    boolean requestAuth(String authId, Long userId, String accessToken, String openId) throws Exception;

    boolean requestAuth(String authId, String userName, String accessToken, String openId) throws Exception;
}
