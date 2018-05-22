package com.fun.yzss.service.image;

import com.fun.yzss.client.ImageClient;

public interface ImageService {
    //慢，弃用
    void parserData(int start, int end, ImageClient client) throws Exception;

    //读取列表也刷新page，无法获取type，速度快，type可以考虑通过限定类型查询
    void parserData2(int start, int end, ImageClient client) throws Exception;


    void test() throws Exception;

    void cdnDownload(String uri, String type, ImageClient cdnClient, String id) throws Exception;

    void pushToCOS(String type, String id) throws Exception;

    void download(String url, ImageClient client) throws Exception;



}
