package com.fun.yzss.service.oss;

import java.util.Map;

public interface OSSService {
    void uploadJokeFiles(Map<String, String> datas);

    void uploadMetaFile(String data);

    String getURL(String key);
}
