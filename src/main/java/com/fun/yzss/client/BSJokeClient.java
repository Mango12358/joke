package com.fun.yzss.client;

import com.netflix.config.ConfigurationManager;

public class BSJokeClient extends AbstractRestClient {

    private static BSJokeClient instance = new BSJokeClient();

    private BSJokeClient() {
        super(ConfigurationManager.getConfigInstance().getString("bs.host", "http://m.budejie.com/text/"));
    }

    public static BSJokeClient getInstance() {
        return instance;
    }

    public String getPage(String page) {
        return getTarget().path("/" + page).request().get(String.class);
    }
}
