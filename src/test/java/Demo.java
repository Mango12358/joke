import com.alibaba.fastjson.JSON;
import com.fun.yzss.client.BSJokeClient;
import com.fun.yzss.service.joke.bs.BSJokeUtils;

public class Demo {
    public static void main(String[] args) {
        String data = BSJokeClient.getInstance().getPage("2");
        System.out.println(data);
        System.out.println(JSON.toJSON(BSJokeUtils.parser(data).toString()));
    }
}
