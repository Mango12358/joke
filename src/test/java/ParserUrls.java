import com.alibaba.fastjson.JSON;
import com.fun.yzss.client.ImageClient;
import com.fun.yzss.service.joke.bs.BSJokeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ParserUrls {
    public static void main(String[] args) throws Exception {
        int count = 1;
        for (int i = 1; i <= count; i++) {
            String path = "data/" + i;
            File file = new File(path);
            FileInputStream stream = new FileInputStream(file);
            byte[] data = new byte[1024];
            StringBuilder sb = new StringBuilder(1024);
            int j = 0;
            while ((j = stream.read(data)) > 0) {
                sb.append(new String(data, 0, j));
            }


            System.out.println(sb.toString());
        }
    }
}
