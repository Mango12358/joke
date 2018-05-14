package com.fun.yzss.service.oss.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.fun.yzss.service.oss.OSSService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Map;

@Service("OSSService")
public class OSSServiceImpl implements OSSService {
    private final String END_POINT = "http://oss-cn-hangzhou.aliyuncs.com";
    private final String KEY_ID = "LTAIEtnfGpfe6e4Q";
    private final String KEY_SEC = "BjsIXMRPliQrreYslJsgMagBFKfrJy";
    private final String BUCKET_NAME = "nhjoke";
    private final String TYPE_JOKE = "jokes";
    public static final String TYPE_META = "metadata";
    private final String URL_PRIFIX = "https://nhjoke.oss-cn-hangzhou.aliyuncs.com/";


    @Override
    public void uploadJokeFiles(Map<String, String> datas) {
        if (datas == null || datas.size() == 0) return;
        OSSClient ossClient = new OSSClient(END_POINT, KEY_ID, KEY_SEC);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("text/plain; charset=\"UTF-8\"");
            metadata.setObjectAcl(CannedAccessControlList.PublicRead);
            for (String name : datas.keySet()) {
                String key = TYPE_JOKE + "/" + name + ".json";
                ossClient.putObject(BUCKET_NAME, key, new ByteArrayInputStream(datas.get(name).getBytes()), metadata);
            }
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public void uploadMetaFile(String data) {
        OSSClient ossClient = new OSSClient(END_POINT, KEY_ID, KEY_SEC);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/json; charset=\"UTF-8\"");
            metadata.setObjectAcl(CannedAccessControlList.PublicRead);
            ossClient.putObject(BUCKET_NAME, TYPE_META, new ByteArrayInputStream(data.getBytes()), metadata);

        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public String getURL(String key) {
        if (TYPE_META.equalsIgnoreCase(key)) {
            return URL_PRIFIX + TYPE_META;
        } else {
            return URL_PRIFIX + TYPE_JOKE + "/" + key;
        }
    }
}
