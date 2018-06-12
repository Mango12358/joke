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
import com.fun.yzss.service.image.ImageNewService;
import com.fun.yzss.service.image.ImageService;
import com.fun.yzss.service.joke.bs.BSJokeUtils;
import com.fun.yzss.service.joke.model.Joke;
import com.google.common.util.concurrent.Runnables;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    private ImageNewService imageNewService;
    @Resource
    private ImageDao imageDao;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 100, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1));

    private static LinkedHashMap<String, String> typeMap = new LinkedHashMap<>();

    static {
        typeMap.put("science", "科技");
        typeMap.put("feelings", "表情");
        typeMap.put("computer", "计算机");
        typeMap.put("religion", "宗教");
        typeMap.put("industry", "工业");
        typeMap.put("business", "商务");
        typeMap.put("health", "健康");
        typeMap.put("music", "音乐");
        typeMap.put("transportation", "交通");
        typeMap.put("backgrounds", "背景");
        typeMap.put("fashion", "时尚");
        typeMap.put("education", "教育");
        typeMap.put("sports", "运动");
        typeMap.put("people", "人物");
        typeMap.put("food", "食物");
        typeMap.put("animals", "动物");
        typeMap.put("buildings", "建筑");
        typeMap.put("places", "地标");
        typeMap.put("travel", "旅游");
        typeMap.put("nature", "风景");
    }

    @GET
    @Path("/getImages")
    public Response test(@Context HttpServletRequest request,
                         @Context HttpHeaders hh,
                         @QueryParam("count") Long maxCount) throws Exception {
        if (maxCount == null) {
            maxCount = Long.MAX_VALUE;
        }
        for (final String type : typeMap.keySet()) {
            long count = imageNewService.getPageCount(type);
            if (count > maxCount) {
                count = maxCount;
            }
            int threadCount = 50;
            final int base = 1;
            if (count < threadCount) {
                threadCount = (int) count / 2;
                if (threadCount == 0) {
                    threadCount = 1;
                }
            }
            final int step = (int) count / threadCount;

            for (int i = 0; i <= threadCount; i++) {
                final int j = i;
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            imageNewService.parser(typeMap.get(type), j * step + base, (j + 1) * step + base, ImageClient.create(), type);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            while (threadPoolExecutor.getActiveCount() > 0) {
                Thread.sleep(10000);
            }
        }
        return Response.status(200).entity("success").build();
    }


    @GET
    @Path("/getChoice")
    public Response getChoice(@Context HttpServletRequest request,
                              @Context HttpHeaders hh,
                              @QueryParam("count") Integer maxCount) throws Exception {
        if (maxCount == null) {
            maxCount = 100;
        }
        imageNewService.parserChoice(0, maxCount, ImageClient.getInstance());
        return Response.status(200).entity("success").build();
    }

    @GET
    @Path("/downloadImages")
    public Response downloadImages(@Context HttpServletRequest request,
                                   @Context HttpHeaders hh) throws Exception {
        final ConcurrentLinkedQueue<ImageDo> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < 20; i++) {
            threadPoolExecutor.execute(new DownloadThread(queue));
        }

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (String type : typeMap.values()) {
                    long count = 0;
                    long step = 1000;
                    while (true) {
                        if (queue.size() > 200) {
                            try {
                                Thread.sleep(5000);
                                continue;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.gc();
                        try {
                            List<ImageDo> imageDos = imageDao.getByTypeWithLimit(type, count, step);
                            if (imageDos == null || imageDos.size() == 0) break;
                            queue.addAll(imageDos);
                            count += imageDos.size();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        return Response.status(200).entity("success").build();
    }

    class DownloadThread implements Runnable {

        private ConcurrentLinkedQueue<ImageDo> queue;
        ImageClient client;

        DownloadThread(ConcurrentLinkedQueue<ImageDo> queue) {
            this.queue = queue;
            client = ImageClient.createCDN();
        }

        @Override
        public void run() {
            while (true) {
                if (!queue.isEmpty()) {
                    ImageDo tmp = queue.poll();
                    try {
                        imageNewService.download(tmp, client);
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
