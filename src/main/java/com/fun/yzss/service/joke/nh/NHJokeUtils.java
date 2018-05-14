package com.fun.yzss.service.joke.nh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fun.yzss.service.joke.model.Joke;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class NHJokeUtils {
    public static Pair<String, List<Joke>> parser(String data) {
        List<Joke> result = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(data);
        jsonObject = jsonObject.getJSONObject("data");
        Long maxTime = jsonObject.getLong("max_time");
        JSONArray objects = jsonObject.getJSONArray("data");
        for (int i = 0; i < objects.size(); i++) {
            JSONObject group = objects.getJSONObject(i).getJSONObject("group");
            String content = group.getString("content");
            Long up = group.getLong("digg_count");
            Long down = group.getLong("bury_count");
            JSONArray comments = objects.getJSONObject(i).getJSONArray("comments");
            List<String> commentList = new ArrayList<>();
            if (comments != null && comments.size() > 0) {
                for (int j = 0; j < comments.size(); j++) {
                    commentList.add(comments.getJSONObject(j).getString("text"));
                }
            }

            Joke joke = new Joke();
            joke.setSource("NH");
            joke.setContent(content);
            joke.setComment(commentList);
            joke.setDown(down);
            joke.setUp(up);
            result.add(joke);
        }
        return new Pair<>(String.valueOf(maxTime), result);
    }
}
