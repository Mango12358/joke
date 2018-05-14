package com.fun.yzss.resources;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.fun.yzss.client.AbstractRestClient;
import com.fun.yzss.client.BSJokeClient;
import com.fun.yzss.client.NHJokeClient;
import com.fun.yzss.db.dao.JokeDao;
import com.fun.yzss.service.joke.JokeService;
import com.fun.yzss.service.joke.bs.BSJokeUtils;
import com.fun.yzss.service.joke.model.Joke;
import com.fun.yzss.service.joke.nh.NHJokeUtils;
import com.fun.yzss.service.oss.OSSService;
import javafx.util.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("/joke")
public class JokeResource {

    @Resource
    private JokeDao jokeDao;
    @Resource
    private JokeService jokeService;

    @GET
    @Path("/bs")
    public Response bs(@Context HttpServletRequest request,
                       @Context HttpHeaders hh) throws Exception {
        for (int i = 0; i < 8500; i++) {
            String data = BSJokeClient.getInstance().getPage("" + i);
            List<Joke> jokeList = BSJokeUtils.parser(data);
            List<Joke> addList = new ArrayList<>();
            for (Joke joke : jokeList) {
                if (joke.getContent().length() > 50) {
                    addList.add(joke);
                }
            }
            if (addList.size() > 0) {
                jokeDao.add(addList);
            }
        }
        return Response.status(200).entity("success").build();
    }

    @GET
    @Path("/increase")
    public Response nh(@Context HttpServletRequest request,
                       @Context HttpHeaders hh) throws Exception {
        jokeService.fetchJokes(true);
        return Response.status(200).entity("success").build();
    }

    @GET
    @Path("/query")
    public Response get(@Context HttpServletRequest request,
                        @Context HttpHeaders hh) throws Exception {
        List<Joke> all = jokeDao.getAll();
        return Response.status(200).entity(JSON.toJSONString(all)).build();
    }

    @GET
    @Path("/update")
    public Response update(@Context HttpServletRequest request,
                           @Context HttpHeaders hh) throws Exception {
        jokeService.pushJokes();

        return Response.status(200).entity("success").encoding("UTF-8").type(MediaType.APPLICATION_JSON).build();
    }


}
