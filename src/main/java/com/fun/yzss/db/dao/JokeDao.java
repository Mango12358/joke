package com.fun.yzss.db.dao;

import com.fun.yzss.db.engine.QueryEngine;
import com.fun.yzss.db.entity.JokeDo;
import com.fun.yzss.db.query.JokeQuery;
import com.fun.yzss.service.joke.model.Joke;
import com.google.common.base.Joiner;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("jokeDao")
public class JokeDao {
    @Resource
    private QueryEngine queryEngine;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public int add(Joke joke) {
        return queryEngine.insertSingle(JokeQuery.INSERT_INTO, toDo(joke));
    }

    public List<Joke> getAll() {
        List<JokeDo> list = queryEngine.queryMultiple(JokeQuery.FIND_ALL, new JokeDo(), JokeDo.class);
        List<Joke> result = new ArrayList<>();
        for (JokeDo jokeDo : list) {
            result.add(toJoke(jokeDo));
        }
        return result;
    }

    public int[] add(List<Joke> joke) {
        JokeDo[] dos = new JokeDo[joke.size()];
        for (int i = 0; i < joke.size(); i++) {
            dos[i] = toDo(joke.get(i));
        }
        return queryEngine.insertBatch(JokeQuery.INSERT_INTO, dos);
    }

    private JokeDo toDo(Joke joke) {
        JokeDo jokeDo = new JokeDo();
        String content = joke.getContent();
        String comment = Joiner.on(";").join(joke.getComment());

        try {
            jokeDo.setId(joke.getId())
                    .setComment(comment.getBytes("utf-8"))
                    .setContent(content.getBytes("utf-8"))
                    .setDown(joke.getDown())
                    .setSource(joke.getSource())
                    .setUp(joke.getUp())
                    .setHashCode(DigestUtils.md5Hex(content));
        } catch (UnsupportedEncodingException e) {
            logger.error("Get Bytes Failed.", e);
            return null;
        }
        return jokeDo;
    }

    private Joke toJoke(JokeDo jokeDo) {
        Joke joke = new Joke();
        try {
            String content = new String(jokeDo.getContent(), "UTF-8");
            String comment = new String(jokeDo.getComment(), "UTF-8");
            joke.setId(jokeDo.getId());
            if (comment != null && !comment.isEmpty()) {
                comment = comment.replaceAll("<br />","\n");
                comment = comment.replaceAll("<br/>","\n");
                joke.setComment(Arrays.asList(comment.split(";")));
            }
            content = content.replaceAll("<br />","\n");
            content = content.replaceAll("<br/>","\n");
            joke.setContent(content);
            joke.setDown(joke.getDown());
            joke.setSource(joke.getSource());
            joke.setUp(joke.getUp());
        } catch (UnsupportedEncodingException e) {
            logger.error("Get Bytes Failed.", e);
            return null;
        }
        return joke;
    }

}
