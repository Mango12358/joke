package com.fun.yzss.service.image;

import com.fun.yzss.client.ImageClient;

public interface ImageService {
    void parserData(int start , int end, ImageClient client) throws Exception;
    void parserData2(int start , int end, ImageClient client) throws Exception;
    void test() throws Exception;

    void download(String url , ImageClient client)throws Exception;

    void downloadImage(String url ,String type,  ImageClient client,String id)throws Exception;
}
