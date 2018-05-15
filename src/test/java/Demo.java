import com.alibaba.fastjson.JSON;
import com.fun.yzss.client.BSJokeClient;
import com.fun.yzss.client.ImageClient;
import com.fun.yzss.service.joke.bs.BSJokeUtils;

public class Demo {
    public static void main(String[] args) {
        String data = ImageClient.getInstance().getImage("7076");
        System.out.println(data);
        System.out.println(JSON.toJSON(BSJokeUtils.parser(data).toString()));
    }
}
