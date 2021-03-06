package com.fun.yzss.service.image.impl;

import com.fun.yzss.client.ImageClient;
import com.fun.yzss.db.dao.ImageDao;
import com.fun.yzss.db.entity.ImageDo;
import com.fun.yzss.service.image.ImageService;
import com.google.common.base.Joiner;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("imageService")
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageDao imageDao;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    private final String KEY_ID = "AKIDqelmcIvCP3ZuusJBbpuwt2ISQGuMIefV";
    private final String KEY_SEC = "mNqAIEzYyDJExg4JMLCXnxvhCNREqGUW";
    private final String BUCKET_NAME = "image-1256364513";
    private static final String FILE_ROOT = "E://images/";

    @Override
    public void parserData(int start, int end, ImageClient client) throws Exception {
        Pattern pattern = Pattern.compile("data-h=\"[\\d]+\"><a href=\"(.*?)\"><img");
        File file = new File("data/urls");
        FileWriter writer = new FileWriter(file);
        try {

            for (int i = start; i < end; ) {
                try {
                    String data = client.getImage(String.valueOf(i));
                    if (data == null) {
                        logger.error("DataFailed.ID:" + i);
                        continue;
                    }
                    Matcher matcher = pattern.matcher(data);
                    List<ImageDo> imageDos = new ArrayList<>();
                    while (matcher.find()) {
                        String uri = matcher.group(1);
                        writer.write(uri + "\n");
                        String imageDetails = client.getDetail(uri);
                        List<String> tags = getTags(imageDetails);
                        String type = getType(imageDetails);

                        String downloadUrl = getDownloadUrl(imageDetails);
                        if (downloadUrl == null) {
                            logger.error("Download Url Is null.ID:" + i + "URI:" + uri);
                            continue;
                        }

                        ImageDo imageDo = new ImageDo();
                        imageDo.setUrl(downloadUrl).setType(type).setTagList(Joiner.on(",").join(tags));
                        int startIndex = downloadUrl.lastIndexOf("-");
                        int endIndex = downloadUrl.lastIndexOf("_");
                        if (type == null) {
                            type = downloadUrl.substring(0, startIndex);
                            imageDo.setType(type);
                        }
                        String sourceId = downloadUrl.substring(startIndex + 1, endIndex);
                        imageDo.setSourceId(Long.parseLong(sourceId));
                        imageDos.add(imageDo);
                    }
                    imageDao.insertBatch(imageDos.toArray(new ImageDo[imageDos.size()]));
                    i++;
                } catch (Exception e) {
                }
            }
        } finally {
            writer.close();
        }
    }

    @Override
    public void parserData2(int start, int end, ImageClient client) throws Exception {
        Pattern pattern = Pattern.compile("2x\\\" (src|data-lazy)=\"(.*?)__(.*?)\\.(.*?)\\\" alt=\"(.*?)\"");
        for (int i = start; i < end; ) {
            try {
                String data = client.getImage(String.valueOf(i));
                if (data == null) {
                    logger.error("DataFailed.ID:" + i);
                    continue;
                }
                Matcher matcher = pattern.matcher(data);
                List<ImageDo> imageDos = new ArrayList<>();
                while (matcher.find()) {
                    String downloadUrl = matcher.group(2);
                    String imageType = matcher.group(4);
                    String tagStr = matcher.group(5).replaceAll("性质", "自然风景");

                    List<String> tags = new ArrayList<>();
                    String[] y = tagStr.split(", ");
                    if (y.length > 0) {
                        tags.addAll(Arrays.asList(y));
                    }

                    ImageDo imageDo = new ImageDo();
                    imageDo.setUrl(downloadUrl + "_1280." + imageType).setTagList(Joiner.on(",").join(tags));
                    int startIndex = downloadUrl.lastIndexOf("-");
                    String sourceId = downloadUrl.substring(startIndex + 1);
                    imageDo.setSourceId(Long.parseLong(sourceId));
                    imageDos.add(imageDo);
                }
                imageDao.insertBatch(imageDos.toArray(new ImageDo[imageDos.size()]));
                i++;
            } catch (Exception e) {
            }
        }
    }


    @Override
    public void test() throws Exception {
    }

    @Override
    public void cdnDownload(String uri, String type, ImageClient cdnClient, String id) throws Exception {
        Response response = cdnClient.downloadPage(uri);
        if (response.getStatus() / 100 == 2) {
            int x = uri.lastIndexOf(".");
            File file = new File(FILE_ROOT + type + "/" + id + uri.substring(x));
            InputStream in = (InputStream) response.getEntity();
            FileOutputStream fileWriter = new FileOutputStream(file);

            try {
                byte[] tmp = new byte[1024];
                int i = 0;
                while ((i = in.read(tmp)) > 0) {
                    fileWriter.write(tmp, 0, i);
                }
            } finally {
                fileWriter.close();
            }
        }
    }

    @Override
    public void pushToCOS(String type, String id) throws Exception {
//        if (type == null || id == null) return;
//        COSCredentials cred = new BasicCOSCredentials(KEY_ID, KEY_SEC);
//        ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));
//        COSClient cosclient = new COSClient(cred, clientConfig);
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            int x = uri.lastIndexOf(".");
//            String fileName = FILE_ROOT + type + "/" + id + uri.substring(x);
//            metadata.setContentLength(datas.get(name).getBytes().length);
//            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, new ByteArrayInputStream(datas.get(name).getBytes()), metadata);
//            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
//            cosclient.putObject(putObjectRequest);
//
//        } finally {
//            cosclient.shutdown();
//        }
    }

    @Override
    public void download(String url, ImageClient client) throws Exception {
        Response response = client.getDownloadUrl(url);
        if (response.getStatus() / 100 == 2) {
            File file = new File("E://images/" + url);
            InputStream in = (InputStream) response.getEntity();
            FileOutputStream fileWriter = new FileOutputStream(file);

            try {
                byte[] tmp = new byte[1024];
                int i = 0;
                while ((i = in.read(tmp)) > 0) {
                    fileWriter.write(tmp, 0, i);
                }
            } finally {
                fileWriter.close();
            }
        }

    }


    private String getDownloadUrl(String imageDetails) {
        Pattern downloadPattern = Pattern.compile("value=\"(.*?)\" data-perm=\"check\"(.*?)</tr>\n" +
                "                    \n" +
                "                    <tr class=\"no_default\">");
        Matcher matcher = downloadPattern.matcher(imageDetails);
        String baseUrl = null;
        if (matcher.find()) {
            baseUrl = matcher.group(1);
        }
        if (baseUrl == null) return null;
//        String data = ImageClient.getInstance().getDownloadUrl(baseUrl);
//        Pattern realUrlPattern = Pattern.compile("location=\"(.*?)\";show");
//        Matcher matcher1 = realUrlPattern.matcher(data);
//        if (matcher1.find()) {
//            return matcher1.group(1);
//        }
        return baseUrl;
    }

    private String getType(String imageDetails) {
        Pattern typePattern = Pattern.compile("类别(.*?)\">(.*?)</a></td></tr>");
        Matcher matcher = typePattern.matcher(imageDetails);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    private List<String> getTags(String imageDetails) {
        List<String> result = new ArrayList<>();
        Pattern tagPattern = Pattern.compile("\" alt=(.*?)\">\n" +
                "                <div class=\"overlay\">");
        Matcher matcher = tagPattern.matcher(imageDetails);
        if (matcher.find()) {
            String x = matcher.group(1);
            String[] y = x.split(", ");
            if (y.length > 0) {
                result.addAll(Arrays.asList(y));
            }
        }
        return result;
    }
}
