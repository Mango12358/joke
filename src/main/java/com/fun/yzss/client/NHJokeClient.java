package com.fun.yzss.client;

import com.netflix.config.ConfigurationManager;

import javax.ws.rs.client.WebTarget;

public class NHJokeClient extends AbstractRestClient {
    private static NHJokeClient instance = new NHJokeClient();

    protected NHJokeClient() {
        super(ConfigurationManager.getConfigInstance().getString("nh.host", "http://neihanshequ.com/joke/"));
    }

    public static NHJokeClient getInstance() {
        return instance;
    }

    public String getJokes(String maxTime) {
        WebTarget target = getTarget().queryParam("is_json", 1).queryParam("app_name", "neihanshequ_web");
        if (maxTime != null) {
            target = target.queryParam("max_time", maxTime);
        }
        return target.request().cookie("csrftoken", "79c60231e560dc59e5b5267a0f446160").cookie("tt_webid", "6535297431768663560").get(String.class);
    }
}
