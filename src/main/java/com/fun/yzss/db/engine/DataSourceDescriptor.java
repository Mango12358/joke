package com.fun.yzss.db.engine;

import java.util.Map;

/**
 * Created by fanqq on 2016/9/23.
 */
public interface DataSourceDescriptor {
    public String getId();

    public String getType();

    public Map<String, Object> getProperties();

    public String getProperty(String name, String defaultValue);

    public boolean getBooleanProperty(String name, boolean defaultValue);

    public double getDoubleProperty(String name, double defaultValue);

    public int getIntProperty(String name, int defaultValue);

    public long getLongProperty(String name, long defaultValue);
}
