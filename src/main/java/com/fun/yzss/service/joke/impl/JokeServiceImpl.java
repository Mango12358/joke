package com.fun.yzss.service.joke.impl;

import com.alibaba.fastjson.JSON;
import com.fun.yzss.client.AbstractRestClient;
import com.fun.yzss.client.BSJokeClient;
import com.fun.yzss.client.NHJokeClient;
import com.fun.yzss.db.dao.JokeDao;
import com.fun.yzss.service.joke.JokeService;
import com.fun.yzss.service.joke.bs.BSJokeUtils;
import com.fun.yzss.service.joke.model.Joke;
import com.fun.yzss.service.joke.model.MetaData;
import com.fun.yzss.service.joke.nh.NHJokeUtils;
import com.fun.yzss.service.oss.OSSService;
import com.fun.yzss.service.oss.impl.COSServiceImpl;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("jokeService")
public class JokeServiceImpl implements JokeService {
    @Resource
    private JokeDao jokeDao;
    @Resource
    private OSSService OSSService;

    private static final int MIN_CONTENT_LEN = 50;
    private static final int MIN_CONTENT_UP = 50;
    private static final int JOKE_STEP = 50;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void fetchJokes(boolean increment) {
        fetchBsJokes(increment);
//        fetchNHJokes(increment);
    }

    @Override
    public void pushJokes() {
        MetaClient metaClient = new MetaClient(OSSService.getURL(COSServiceImpl.TYPE_META));
        String meta = metaClient.getMeta();
        List<Joke> all = jokeDao.getAll();
        Map<String, String> data = new HashMap<>();
        long newMax = 0L;
        if (meta == null || meta.isEmpty()) {
            for (int i = 0; i + JOKE_STEP < all.size(); i += JOKE_STEP) {
                List<Joke> sub = all.subList(i, i + JOKE_STEP);
                data.put("" + (i / JOKE_STEP), JSON.toJSONString(sub));
                newMax = i / JOKE_STEP;
            }
            MetaData newMetaData = new MetaData();
            newMetaData.base.put("from", 0L);
            OSSService.uploadJokeFiles(data);
            newMetaData.base.put("to", newMax);
            newMetaData.max = newMax;
            newMetaData.fresh.put("from", newMax + 1);
            newMetaData.fresh.put("to", newMax + 1);
            System.out.println(JSON.toJSONString(newMetaData));
            OSSService.uploadMetaFile(JSON.toJSONString(newMetaData));
        } else {
            MetaData metaData = JSON.parseObject(meta, MetaData.class);
            for (int i = (int) (metaData.max * JOKE_STEP); i + JOKE_STEP < all.size(); i += JOKE_STEP) {
                List<Joke> sub = all.subList(i, i + JOKE_STEP);
                data.put("" + (i / JOKE_STEP), JSON.toJSONString(sub));
                newMax = (i / JOKE_STEP);
            }
            if (newMax > metaData.max) {
                OSSService.uploadJokeFiles(data);
                metaData.fresh.put("to", newMax);
                metaData.max = newMax;
                OSSService.uploadMetaFile(JSON.toJSONString(metaData));
            }
        }

    }

    private void fetchBsJokes(boolean increment) {
        int page = 8500;
        if (increment) {
            page = 300;
        }
        for (int i = 0; i < page; i++) {
            try {
                String data = BSJokeClient.getInstance().getPage(String.valueOf(i));
                List<Joke> jokes = BSJokeUtils.parser(data);
                List<Joke> addList = new ArrayList<>();
                for (Joke joke : jokes) {
                    if (joke.getContent().length() > MIN_CONTENT_LEN) {
                        addList.add(joke);
                    }
                }
                if (addList.size() > 0) {
                    jokeDao.add(addList);
                }
            } catch (Exception e) {
                logger.error("Fetch BS Failed.", e);
            }
        }
    }

    private void fetchNHJokes(boolean increment) {
        int index = 8500;
        if (increment) {
            index = 300;
        }
        String maxTime = null;
        int i = 0;
        while (true) {
            try {
                if (i > index) break;
                String data = NHJokeClient.getInstance().getJokes(maxTime);
                Pair<String, List<Joke>> jokes = NHJokeUtils.parser(data);
                maxTime = jokes.getKey();
                List<Joke> list = jokes.getValue();
                if (list == null || list.size() == 0) {
                    break;
                }
                List<Joke> addList = new ArrayList<>();
                for (Joke j : list) {
                    if (j.getContent().length() > MIN_CONTENT_LEN && j.getUp() > MIN_CONTENT_UP) {
                        addList.add(j);
                    }
                }
                if (addList.size() > 0) {
                    jokeDao.add(addList);
                }
                i++;
            } catch (Exception e) {
                logger.error("Fetch NH Failed.", e);
            }
        }
    }

    class MetaClient extends AbstractRestClient {

        protected MetaClient(String url) {
            super(url);
        }

        String getMeta() {
            try {
                return getTarget().request().get(String.class);
            } catch (Exception e) {
                return null;
            }
        }
    }

}
