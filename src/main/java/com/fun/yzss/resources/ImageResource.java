package com.fun.yzss.resources;

import com.fun.yzss.client.BSJokeClient;
import com.fun.yzss.client.ImageClient;
import com.fun.yzss.service.image.ImageService;
import com.fun.yzss.service.joke.bs.BSJokeUtils;
import com.fun.yzss.service.joke.model.Joke;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Path("/image")
public class ImageResource {

    @Resource
    private ImageService imageService;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 100, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1));

    @GET
    @Path("/url")
    public Response bs(@Context HttpServletRequest request,
                       @Context HttpHeaders hh) throws Exception {
        int threadCount = 50;
        int total = 7875;
        final int step = total / threadCount;


        for (int i = 0; i <= threadCount; i++) {
            final int j = i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        imageService.parserData(j * step + 1, (j + 1) * step + 1, ImageClient.create());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return Response.status(200).entity("success").build();
    }

}
