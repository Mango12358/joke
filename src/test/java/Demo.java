import com.alibaba.fastjson.JSON;
import com.fun.yzss.client.BSJokeClient;
import com.fun.yzss.client.ImageClient;
import com.fun.yzss.service.joke.bs.BSJokeUtils;

import javax.ws.rs.core.Response;
import java.io.*;

public class Demo {
    public static void main(String[] args) throws IOException {
        String url = "greece-2749909_1280.jpg";
        Response response = ImageClient.getInstance().getDownloadUrl(url);
        if (response.getStatus() / 100 == 2) {
            File file = new File("E:\\images\\" + url);
            InputStream in = (InputStream) response.getEntity();
            FileOutputStream fileWriter = new FileOutputStream(file);
            byte[] tmp = new byte[1024];
            int i = 0;
            while ((i = in.read(tmp)) > 0) {
                fileWriter.write(tmp, 0, i);
            }
        }
    }
}
