package com.fun.yzss.service.image;

import com.fun.yzss.client.ImageClient;

public interface ImageService {
    void parserData(int start , int end, ImageClient client) throws Exception;
    void test() throws Exception;
}
