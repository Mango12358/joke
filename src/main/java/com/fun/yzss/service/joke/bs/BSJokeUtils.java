package com.fun.yzss.service.joke.bs;

import com.fun.yzss.service.joke.model.Joke;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BSJokeUtils {
    public static List<Joke> parser(String data) {
        Pattern pattern = Pattern.compile("<section class=\"ui-row-flex\">\\n  <p>\\n    (.*)\\n  </p>");
        Matcher matcher = pattern.matcher(data);
        List<Joke> result = new ArrayList<>();
        while (matcher.find()) {
            String content = matcher.group(1);
//            System.out.println(content);
            Joke joke = new Joke();
            joke.setContent(content);
            joke.setSource("BJ");
            result.add(joke);
        }
        return result;
    }
}
