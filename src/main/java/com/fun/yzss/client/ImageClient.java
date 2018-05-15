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

    public static ImageClient getInstance() {
        return instance;
    }

    public static ImageClient create() {
        return new ImageClient();
    }


    public String getImage(String page) {
        WebTarget target = getTarget().path("/zh/photos/?").queryParam("order", "popular").queryParam("pagi", page);

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
                .cookie("sessionid", ".eJxVy0sOgjAUQNG9vDEh9EM_bsWY5rUUWi1gaDvRuHeRgQnje-4bTMJlqjh5uMArQANDgsuVMU2JULcGDNYSTM1-MwFz2JVl3BHP7Ci1JHykyGzHe0F05wV1TCH2yAVjcJrjsK9KS0WFPBeL7uGXX35u69270tYSU25dzWWdD9jGgy44e7Nuxs8Y0__7fAGYzkDl:1fIPzH:33DA_efsfmd90i1nDZ8boe3U3Oc")
                .get();
    }
}


