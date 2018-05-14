package com.fun.yzss.db.engine;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fanqq on 2016/9/23.
 */
public interface DataSource {
    public void dispose();

    public Connection getConnection() throws SQLException;

    public void initialize(DataSourceDescriptor d);
}
