package com.fun.yzss.resources;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fun.yzss.client.BSJokeClient;
import com.fun.yzss.client.ImageClient;
import com.fun.yzss.db.dao.ImageDao;
import com.fun.yzss.db.entity.ImageDo;
import com.fun.yzss.service.image.ImageService;
import com.fun.yzss.service.joke.bs.BSJokeUtils;
import com.fun.yzss.service.joke.model.Joke;
import com.google.common.util.concurrent.Runnables;
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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Path("/image")
public class ImageResource {

    @Resource
    private ImageService imageService;
    @Resource
    private ImageDao imageDao;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 100, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1));

    @GET
    @Path("/url")
    public Response bs(@Context HttpServletRequest request,
                       @Context HttpHeaders hh) throws Exception {
        int threadCount = 28;
//        int total = 7875;
        int total = 28;
        final int base = 1543;
        final int step = total / threadCount;


        for (int i = 0; i <= threadCount; i++) {
            final int j = i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        imageService.parserData(j * step + base, (j + 1) * step + 1 + base, ImageClient.create());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return Response.status(200).entity("success").build();
    }
    @GET
    @Path("/new/url")
    public Response url(@Context HttpServletRequest request,
                       @Context HttpHeaders hh) throws Exception {
        int threadCount = 50;
        int total = 5414;
        final int base = 0;
        final int step = total / threadCount;


        for (int i = 0; i <= threadCount; i++) {
            final int j = i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        imageService.parserData2(j * step + base, (j + 1) * step + 1 + base, ImageClient.create());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return Response.status(200).entity("success").build();
    }

    @GET
    @Path("/download")
    public Response download(@Context HttpServletRequest request,
                             @Context HttpHeaders hh) throws Exception {
        final ConcurrentLinkedQueue<String[]> queue = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < 20; i++) {
            threadPoolExecutor.execute(new DownloadThread(queue));
        }

        threadPoolExecutor.execute(new Runnable() {
            String type = "travel";

            @Override
            public void run() {
                while (true) {
                    try {
                            List<ImageDo> imageDos = imageDao.getByType("旅游度假");
                            for (ImageDo imageDo : imageDos) {
                                String[] tmp = new String[3];
                                tmp[0] = imageDo.getUrl();
                                tmp[1] = type;
                                tmp[2] = imageDo.getSourceId();
                                queue.add(tmp);
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return Response.status(200).entity("success").build();
    }

    class DownloadThread implements Runnable {

        private ConcurrentLinkedQueue<String[]> queue;
        ImageClient client;

        DownloadThread(ConcurrentLinkedQueue<String[]> queue) {
            this.queue = queue;
            client = ImageClient.createCDN();
        }

        @Override
        public void run() {
            while (true) {
                if (!queue.isEmpty()) {
                    String[] tmp = queue.poll();
                    try {
                        imageService.cdnDownload(tmp[0].replaceFirst("https://cdn.pixabay.com", ""), tmp[1], client, tmp[2]);
                    } catch (Exception e) {
                        queue.add(tmp);
                    }
                } else {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
