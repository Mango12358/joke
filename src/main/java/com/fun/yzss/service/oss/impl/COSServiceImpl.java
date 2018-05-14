package com.fun.yzss.service.oss.impl;


import com.fun.yzss.service.oss.OSSService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Map;

@Service("COSService")
public class COSServiceImpl implements OSSService {
    private final String END_POINT = "http://oss-cn-hangzhou.aliyuncs.com";
    private final String KEY_ID = "AKIDqelmcIvCP3ZuusJBbpuwt2ISQGuMIefV";
    private final String KEY_SEC = "mNqAIEzYyDJExg4JMLCXnxvhCNREqGUW";
    private final String BUCKET_NAME = "nhjoke-1256364513";
    private final String TYPE_JOKE = "jokes";
    public static final String TYPE_META = "metadata";
    private final String URL_PRIFIX = "https://nhjoke-1256364513.cossh.myqcloud.com/";


    @Override
    public void uploadJokeFiles(Map<String, String> datas) {
        if (datas == null || datas.size() == 0) return;
        COSCredentials cred = new BasicCOSCredentials(KEY_ID, KEY_SEC);
        ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));
        COSClient cosclient = new COSClient(cred, clientConfig);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("text/plain; charset=\"UTF-8\"");
            for (String name : datas.keySet()) {
                String key = TYPE_JOKE + "/" + name + ".json";
                metadata.setContentLength(datas.get(name).getBytes().length);
                PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, new ByteArrayInputStream(datas.get(name).getBytes()), metadata);
                putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
                cosclient.putObject(putObjectRequest);
            }
        } finally {
            cosclient.shutdown();
        }
    }

    @Override
    public void uploadMetaFile(String data) {
        COSCredentials cred = new BasicCOSCredentials(KEY_ID, KEY_SEC);
        ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));
        COSClient cosclient = new COSClient(cred, clientConfig);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/json; charset=\"UTF-8\"");
            metadata.setContentLength(data.getBytes().length);
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, TYPE_META, new ByteArrayInputStream(data.getBytes()), metadata);
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            cosclient.putObject(putObjectRequest);

        } finally {
            cosclient.shutdown();
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
