package com.fun.yzss.service.image.impl;

import com.fun.yzss.client.ImageClient;
import com.fun.yzss.db.dao.ImageDao;
import com.fun.yzss.db.entity.ImageDo;
import com.fun.yzss.service.image.ImageNewService;
import com.fun.yzss.util.SHAEncoder;
import com.google.common.base.Joiner;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("imageNewService")
public class ImageNewServiceImpl implements ImageNewService {

    @Resource
    private ImageDao imageDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String FILE_ROOT = "E:\\\\images\\\\";
    private final String KEY_ID = "AKIDqelmcIvCP3ZuusJBbpuwt2ISQGuMIefV";
    private final String KEY_SEC = "mNqAIEzYyDJExg4JMLCXnxvhCNREqGUW";
    private final String BUCKET_NAME = "image-1256364513";

    @Override
    public int getPageCount(String type) {
        String data = ImageClient.getInstance().getImagesByType("1", type);
        Pattern pattern = Pattern.compile("/ (\\d*?)&nbsp;");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    @Override
    public void parser(String type, int start, int end, ImageClient client, String pageType) throws Exception {
        Pattern pattern = Pattern.compile("2x\\\" (src|data-lazy)=\"(.*?)__(.*?)\\.(.*?)\\\" alt=\"(.*?)\"");
        for (int i = start; i < end; ) {
            try {
                String data = client.getImagesByType(String.valueOf(i), pageType);
                if (data == null) {
                    logger.error("DataFailed.ID:" + i);
                    continue;
                }
                Matcher matcher = pattern.matcher(data);
                List<ImageDo> imageDos = new ArrayList<>();
                while (matcher.find()) {
                    String downloadUrl = matcher.group(2);
                    String imageType = matcher.group(4);
                    String tagStr = matcher.group(5).replaceAll("性质", "自然,风景");

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
                    imageDo.setType(type);
                    imageDos.add(imageDo);
                }
                imageDao.insertBatch(imageDos.toArray(new ImageDo[imageDos.size()]));
                i++;
            } catch (Exception e) {
                logger.error("Failed.", e);
            }
        }
    }

    @Override
    public void parserChoice(int start, int end, ImageClient client) throws Exception {
        Pattern pattern = Pattern.compile("2x\\\" (src|data-lazy)=\"(.*?)__(.*?)\\.(.*?)\\\" alt=\"(.*?)\"");
        for (int i = start; i < end; ) {
            try {
                String data = client.getImagesByChoice(String.valueOf(i));
                if (data == null) {
                    logger.error("DataFailed.ID:" + i);
                    continue;
                }
                Matcher matcher = pattern.matcher(data);
                while (matcher.find()) {
                    String downloadUrl = matcher.group(2);

                    ImageDo imageDo = new ImageDo();
                    int startIndex = downloadUrl.lastIndexOf("-");
                    String sourceId = downloadUrl.substring(startIndex + 1);
                    imageDo.setSourceId(Long.parseLong(sourceId));
                    imageDo.setChoice(1);
                    imageDao.updateChoice(imageDo);
                }
                i++;
            } catch (Exception e) {
                logger.error("Failed.", e);
            }
        }
    }

    @Override
    public void download(ImageDo imageDo, ImageClient client) throws Exception {
        String uri = imageDo.getUrl().replaceAll("https://cdn.pixabay.com", "");
        Long id = imageDo.getSourceId();
        Response response = client.downloadPage(uri);
        if (response.getStatus() / 100 == 2) {
            int x = uri.lastIndexOf(".");

            InputStream in = (InputStream) response.getEntity();

            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int rc = 0;
            while ((rc = in.read(buff, 0, 1024)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] imageBytes = swapStream.toByteArray();

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            imageDo.setHeight((long) image.getHeight());
            imageDo.setWidth((long) image.getWidth());

            String key = SHAEncoder.getSHA256Str(new String(imageBytes));

            //To File
            File file = new File(FILE_ROOT + key + uri.substring(x));
            ImageIO.write(image, uri.substring(x + 1), file);


            imageDo.setCosURI(key + uri.substring(x));

            if (imageDo.getHeight() < 600) {
                imageDo.setStatus(String.valueOf(0));
            } else {
                //To InputStream
//            InputStream is = new ByteArrayInputStream(imageBytes);
//            pushToCos(imageDo, is, imageBytes.length);
                imageDo.setStatus(String.valueOf(imageBytes.length / 1024));
            }

            imageDao.update(imageDo);
        }
    }

    @Override
    public void pushToCos(ImageDo imageDo, InputStream in, long contentLen) throws Exception {
        String uri = imageDo.getUrl();
        COSCredentials cred = new BasicCOSCredentials(KEY_ID, KEY_SEC);
        ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));
        COSClient cosclient = new COSClient(cred, clientConfig);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            int x = uri.lastIndexOf(".");
            String key = SHAEncoder.getSHA256Str(uri + RandomStringUtils.random(20, true, false)) + uri.substring(x);
            metadata.setContentLength(contentLen > 0 ? contentLen : in.available());
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, in, metadata);
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            cosclient.putObject(putObjectRequest);
            imageDo.setCosURI(key);
            imageDao.update(imageDo);
        } finally {
            cosclient.shutdown();
        }
    }
}
