package com.fun.yzss.service.image.impl;

import com.fun.yzss.client.ImageClient;
import com.fun.yzss.db.dao.ImageDao;
import com.fun.yzss.db.entity.ImageDo;
import com.fun.yzss.service.image.ImageService;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
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
//                            logger.warn("Type is Null.ID:" + i);
                            type = downloadUrl.substring(0, startIndex);
                            imageDo.setType(type);
                        }
                        String sourceId = downloadUrl.substring(startIndex + 1, endIndex);
                        imageDo.setSourceId(sourceId);
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
    public void test() throws Exception {
        imageDao.update(new ImageDo().setId(11L).setSourceId("Test2").setType("旅游3").setUrl("test1").setCosURI("uri2").setTagList("list3"));
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
