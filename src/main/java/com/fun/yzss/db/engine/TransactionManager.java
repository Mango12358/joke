package com.fun.yzss.db.engine;

import java.sql.Connection;

/**
 * Created by fanqq on 2016/9/23.
 */
public interface TransactionManager {
    public void closeConnection();

    public void commitTransaction();

    public Connection getConnection();

    public boolean isInTransaction();

    public void reset();

    public void rollbackTransaction();

    public void startTransaction();
}
