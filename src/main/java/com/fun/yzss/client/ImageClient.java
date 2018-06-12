package com.fun.yzss.client;

import com.netflix.config.ConfigurationManager;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class ImageClient extends AbstractRestClient {
    private static SSLContext sslContext;

    static {
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new X509TrustManager[]{new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }}, new SecureRandom());
        } catch (Exception e) {
        }
    }

    private static final HostnameVerifier alwaysPassedHostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession sslSession) {
            return true;
        }
    };

    private static ImageClient instance = new ImageClient();


    protected ImageClient() {
        super(ConfigurationManager.getConfigInstance().getString("image.host", "https://pixabay.com"),
                900000, sslContext, alwaysPassedHostnameVerifier);
    }
    protected ImageClient(String url) {
        super(url,900000, sslContext, alwaysPassedHostnameVerifier);
    }

    public static ImageClient getInstance() {
        return instance;
    }

    public static ImageClient create() {
        return new ImageClient();
    }

    public static ImageClient createCDN() {
        return new ImageClient("https://cdn.pixabay.com");
    }


    public String getImagesByType(String page,String type) {
        WebTarget target = getTarget().path("/zh/photos/?").queryParam("order", "popular").queryParam("pagi", page)
                .queryParam("image_type","photo").queryParam("orientation","horizontal").queryParam("cat",type);

        return target.request().header("User-Agent", " Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36").accept("*/*")
                .cookie("is_human", "1").cookie("g_rated", "1").cookie("csrftoken", "yCSZkdNZA0magG0W6Qf9I6ZCXA23QYHbtXfSHwJqDnSISkyfr94s2ESTlLqAh0MH").get(String.class);
    }

    public String getImagesByChoice(String page) {
        WebTarget target = getTarget().path("/zh/photos/?").queryParam("order", "ec").queryParam("pagi", page)
                .queryParam("image_type","photo").queryParam("orientation","horizontal");

        return target.request().header("User-Agent", " Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36").accept("*/*")
                .cookie("is_human", "1").cookie("g_rated", "1").cookie("csrftoken", "yCSZkdNZA0magG0W6Qf9I6ZCXA23QYHbtXfSHwJqDnSISkyfr94s2ESTlLqAh0MH").get(String.class);
    }


    public Response downloadPage(String url) {
        WebTarget target = getTarget().path(url);
        return target.request().accept("*/*").get();
    }

    public String getImage(String page) {
        WebTarget target = getTarget().path("/zh/photos/?").queryParam("order", "popular").queryParam("pagi", page)
                .queryParam("image_type","photo").queryParam("orientation","horizontal");

        return target.request().header("User-Agent", " Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36").accept("*/*")
                .cookie("is_human", "1").cookie("g_rated", "1").cookie("csrftoken", "yCSZkdNZA0magG0W6Qf9I6ZCXA23QYHbtXfSHwJqDnSISkyfr94s2ESTlLqAh0MH").get(String.class);
    }

    public String getDetail(String uri) {
        WebTarget target = getTarget().path(uri);
        return target.request().header("User-Agent", " Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36").accept("*/*").get(String.class);
    }

    public Response getDownloadUrl(String uri) {
        WebTarget target = getTarget().path("/zh/photos/download/" + uri);
        return target.request().header("User-Agent", " Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36").accept("*/*")
                .cookie("sessionid", ".eJxVy00OgyAQQOG7zNoYFQTxKk1DBhyFFn8isGnTu9e6aOL6fe8NOuAyZZwIeng5KGAI0N9YpbhQ4l6AxpyczpF27TC6QxnGbU3MjFLJmo8NMlPxVtSqItFY1iG2yAVjcJn9cKydkl0j5LUYtE9afnnb1wfZVObkQyxtjmmdT1j6ky44k153TTP68P8-X5wVQOo:1fJAAo:Pt9oiuM5JBB8me1lJmX8E8-3jf0")
                .get();
    }



    public String getApiData(String category, String order, int page) {
        return getTarget().path("/api/").queryParam("key","8978267-7793b5ba0ed1d84489bb2190d")
                .queryParam("image_type", "photo").queryParam("pretty", "true")
                .queryParam("category", category).queryParam("safesearch", true).queryParam("order", order)
                .queryParam("page", page).queryParam("per_page", 200).request().get(String.class);
    }
}


