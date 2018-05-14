package com.fun.yzss.client;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * Created by fanqq on 2016/9/27.
 */
public class OAuthClient {

    private static OAuthClient instance;
    private DynamicStringProperty wxOAuthUrl = DynamicPropertyFactory.getInstance().getStringProperty("wx.oauth.url", "https://api.weixin.qq.com/sns");
    private DynamicStringProperty qqOAuthUrl = DynamicPropertyFactory.getInstance().getStringProperty("qq.oauth.url", "https://api.weixin.qq.com/sns");

    private AuthClient wxClient;
    private AuthClient qqClient;

    OAuthClient() {
        wxClient = new AuthClient(wxOAuthUrl.get());
        qqClient = new AuthClient(qqOAuthUrl.get());
    }

    public static OAuthClient getInstance() {
        if (instance == null) {
            instance = new OAuthClient();
        }
        return instance;
    }

    public String auth(String type) {
        switch (type) {
            //?access_token=ACCESS_TOKEN&openid=OPENID
            case "wx":
                wxClient.getTarget().path("/auth").request().post(Entity.entity("", MediaType.APPLICATION_JSON));
            case "qq":
                qqClient.getTarget().path("/auth").request().post(Entity.entity("", MediaType.APPLICATION_JSON));
            default:

        }
        return null;
    }

    class AuthClient extends AbstractRestClient {

        protected AuthClient(String url) {
            super(url);
        }
    }

}
