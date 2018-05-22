package com.fun.yzss.client;

import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.uri.UriComponent;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by fanqq on 2016/9/27.
 */
public class AbstractRestClient {
    private WebTarget webTarget;

    private static DynamicIntProperty connectTimeout = DynamicPropertyFactory.getInstance().getIntProperty("client.connect.timeout", 5000);
    private static DynamicIntProperty readTimeout = DynamicPropertyFactory.getInstance().getIntProperty("client.read.timeout", 30000);

    protected AbstractRestClient(String url) {
        Client client = ClientBuilder.newBuilder()
                .withConfig(new ClientConfig())
                .register(MultiPartFeature.class)
                .build();
        client.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout.get());
        client.property(ClientProperties.READ_TIMEOUT, readTimeout.get());
        webTarget = client.target(url);
    }

    protected AbstractRestClient(String url, int readTimeout) {
        Client client = ClientBuilder.newBuilder()
                .withConfig(new ClientConfig())
                .register(MultiPartFeature.class)
                .build();
        client.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout.get());
        client.property(ClientProperties.READ_TIMEOUT, readTimeout);
        webTarget = client.target(url);
    }

    protected AbstractRestClient(String url, int readTimeout, SSLContext sslContext, HostnameVerifier hostnameVerifier) {
        Client client = ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .hostnameVerifier(hostnameVerifier)
                .withConfig(new ClientConfig())
                .register(MultiPartFeature.class)
                .build();
        client.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout.get());
        client.property(ClientProperties.READ_TIMEOUT, readTimeout);
        webTarget = client.target(url);
    }

    public String urlEncode(String url) {
        return UriComponent.encode(url, UriComponent.Type.PATH_SEGMENT);
    }

    protected WebTarget getTarget() {
        return webTarget;
    }
}
