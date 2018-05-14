package com.fun.yzss.db.engine.impl;

import com.fun.yzss.db.engine.DataSource;
import com.fun.yzss.db.engine.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fanqq on 2016/9/23.
 */
@Service("transactionManager")
public class DefaultTransactionManager implements TransactionManager {

    @Resource
    DataSource jdbcDataSource;

    private static ThreadLocalTransactionInfo threadLocalData = new ThreadLocalTransactionInfo();

    private final static Logger logger = LoggerFactory.getLogger(DefaultTransactionManager.class);

    @Override
    public void closeConnection() {
        TransactionInfo trxInfo = threadLocalData.get();

        if (trxInfo.isInTransaction()) {
            // do nothing when in transaction
        } else {
            try {
                trxInfo.reset();
            } catch (SQLException e) {
                logger.warn("Error when closing Connection, message: " + e, e);
            }
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignore it
            }
        }
    }

    @Override
    public void commitTransaction() {
        TransactionInfo trxInfo = threadLocalData.get();

        if (!trxInfo.isInTransaction()) {
            throw new RuntimeException("There is no active transaction open, can't commit");
        }

        try {
            if (trxInfo.getConnection() != null) {
                trxInfo.getConnection().commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to commit transaction, message: " + e, e);
        } finally {
            try {
                trxInfo.reset();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Connection getConnection() {
        TransactionInfo trxInfo = threadLocalData.get();

        if (trxInfo.isInTransaction()) {
            return trxInfo.getConnection();

        } else { // Not in transaction
            Connection connection = null;
            SQLException exception = null;

            try {
                connection = trxInfo.getConnection();

                if (connection == null) {
                    connection = jdbcDataSource.getConnection();
                }

                connection.setAutoCommit(true);
            } catch (SQLException e) {
                exception = e;
            }

            // retry once if pooled connection is closed by server side
            if (exception != null) {
                closeConnection(connection);

                logger.warn("Iffy database connection closed, try to reconnect.",
                        exception);

                try {
                    connection = jdbcDataSource.getConnection();
                    connection.setAutoCommit(true);
                    exception = null;
                } catch (SQLException e) {
                    closeConnection(connection);
                    logger.warn("Unable to reconnect to database.", e);
                }
            }

            if (exception != null) {
                throw new RuntimeException("Error when getting connection from DataSource, message: " + exception, exception);
            } else {
                trxInfo.setConnection(connection);
                trxInfo.setInTransaction(false);
                return connection;
            }
        }
    }

    @Override
    public boolean isInTransaction() {
        TransactionInfo trxInfo = threadLocalData.get();

        return trxInfo.isInTransaction();
    }

    @Override
    public void reset() {
        TransactionInfo trxInfo = threadLocalData.get();

        if (trxInfo != null) {
            Connection conn = trxInfo.getConnection();

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error when closing connection!", e);
                }
            }
        }
        threadLocalData.remove();
    }

    @Override
    public void rollbackTransaction() {
        TransactionInfo trxInfo = threadLocalData.get();

        if (!trxInfo.isInTransaction()) {
            throw new RuntimeException("There is no active transaction open, can't rollback");
        }

        try {
            if (trxInfo.getConnection() != null) {
                trxInfo.getConnection().rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to rollback transaction, message: " + e, e);
        } finally {
            try {
                trxInfo.reset();
            } catch (SQLException e) {
                logger.error("rollback transaction failed.", e);
            }
        }
    }

    @Override
    public void startTransaction() {
        TransactionInfo trxInfo = threadLocalData.get();

        if (trxInfo.isInTransaction()) {
            throw new RuntimeException(
                    "Can't start transaction while another transaction has not been committed or rollbacked!");
        } else {
            Connection connection = null;

            try {
                connection = jdbcDataSource.getConnection();
                connection.setAutoCommit(false);
                trxInfo.setConnection(connection);
                trxInfo.setInTransaction(true);
            } catch (SQLException e) {
                closeConnection(connection);
                throw new RuntimeException("Error when getting connection from DataSource, message: " + e, e);
            }
        }
    }

    static class ThreadLocalTransactionInfo extends ThreadLocal<TransactionInfo> {
        @Override
        protected TransactionInfo initialValue() {
            return new TransactionInfo();
        }
    }

    static class TransactionInfo {

        private Connection connection;

        private boolean inTransaction;

        public Connection getConnection() {
            return connection;
        }

        public boolean isInTransaction() {
            try {
                if (connection != null && connection.isClosed()) {
                    return false;
                }
            } catch (SQLException e) {
                logger.error("check connection is closed failed.", e);
            }

            return inTransaction;
        }

        public void reset() throws SQLException {
            if (connection != null) {
                connection.close();
            }

            connection = null;
            inTransaction = false;
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        }

        public void setInTransaction(boolean inTransaction) {
            this.inTransaction = inTransaction;
        }
    }
}
