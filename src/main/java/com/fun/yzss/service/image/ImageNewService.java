package com.fun.yzss.service.image;

import com.fun.yzss.client.ImageClient;
import com.fun.yzss.db.entity.ImageDo;

import java.io.InputStream;

public interface ImageNewService {
    /**
     * New Version
     * */
    /**
     * get total page count by query type;
     * **/
    int getPageCount(String type);

    /***
     * Parser Image Data.
     * **/
    void parser(String type, int start, int end, ImageClient client,String pageType) throws Exception;

    /**
     * Download and update image info
     * ***/
    void download(ImageDo imageDo, ImageClient client) throws Exception;

    /**
     * Push To COS
     */
    void pushToCos(ImageDo imageDo, InputStream in, long contentLen) throws Exception;

    void parserChoice(int start, int end, ImageClient client) throws Exception;
}
