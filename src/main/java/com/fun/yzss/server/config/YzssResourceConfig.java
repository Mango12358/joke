package com.fun.yzss.server.config;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;
import javax.inject.Singleton;
import javax.servlet.annotation.MultipartConfig;

/**
 * Created by fanqq on 2016/7/13.
 */
public class YzssResourceConfig extends ResourceConfig{
    public YzssResourceConfig() {
        super(MultipartConfig.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(TrimmedQueryParamValueFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
                bind(TrimmedQueryParamValueFactoryProvider.TrimmedQueryParamInjectionResolver.class).to(new TypeLiteral<TrimmedQueryParam>() {
                }).in(Singleton.class);
            }
        });
        register(MultiPartFeature.class);
    }
}
