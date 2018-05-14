package com.fun.yzss.db.engine.impl;

import com.fun.yzss.db.engine.DataSource;
import com.fun.yzss.db.engine.DataSourceDescriptor;
import com.fun.yzss.util.Spliters;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.netflix.config.DynamicPropertyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by fanqq on 2016/9/23.
 */
@Service("jdbcDataSource")
public class JdbcDataSource implements DataSource {
    private ComboPooledDataSource cpds;

    private DataSourceDescriptor dataSourceDescriptor;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DynamicPropertyFactory factory = DynamicPropertyFactory.getInstance();

    @Override
    public void dispose() {
        cpds.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }

    @PostConstruct
    private void initializeByDefault() {
        JdbcDataSourceDescriptor d = new JdbcDataSourceDescriptor();
        String url = factory.getStringProperty("mysql.url", null).get();
        String connectionProperties = factory.getStringProperty("mysql.connection.properties", null).get();

        if (connectionProperties != null && connectionProperties.length() > 0) {
            d.setProperty("url", url + "?" + connectionProperties);
        } else {
            d.setProperty("url", url);
        }

        d.setId(factory.getStringProperty("mysql.id", "default").get());
        d.setType(factory.getStringProperty("mysql.type", null).get());
        d.setProperty("driver", factory.getStringProperty("mysql.driver", null).get());
        d.setProperty("user", factory.getStringProperty("mysql.user", null).get());
        d.setProperty("password", factory.getStringProperty("mysql.password", null).get());
        d.setProperty("login-timeout", factory.getIntProperty("mysql.login.time.out", 30).get());
        d.setProperty("max-idle-time", factory.getIntProperty("mysql.max.idle.time", 10 * 60).get());
        d.setProperty("min-pool-size", factory.getIntProperty("mysql.min.pool.size", 1).get());
        d.setProperty("max-pool-size", factory.getIntProperty("mysql.max.pool.size", 20).get());

        initialize(d);
    }

    @Override
    public void initialize(DataSourceDescriptor d) {
        dataSourceDescriptor = d;
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        String id = d.getId();
        String url = d.getProperty("url", null);
        String driver = d.getProperty("driver", null);
        String user = d.getProperty("user", null);

        try {
            cpds.setDriverClass(driver);
            cpds.setJdbcUrl(url);
            cpds.setUser(user);
            cpds.setPassword(d.getProperty("password", null));
            cpds.setMinPoolSize(d.getIntProperty("min-pool-size", 1));
            cpds.setMaxPoolSize(d.getIntProperty("max-pool-size", 3));
            cpds.setInitialPoolSize(d.getIntProperty("initial-pool-size", 1));
            cpds.setMaxIdleTime(d.getIntProperty("max-idle-time", 10 * 60));
            cpds.setIdleConnectionTestPeriod(d.getIntProperty("idel-connection-test-period", 60));
            cpds.setAcquireRetryAttempts(d.getIntProperty("accquire-retry-attempts", 1));
            cpds.setAcquireRetryDelay(d.getIntProperty("accquire-retry-delay", 30));
            cpds.setMaxStatements(0);
            cpds.setMaxStatementsPerConnection(1000);
            cpds.setNumHelperThreads(6);
            cpds.setMaxAdministrativeTaskTime(5);
            cpds.setPreferredTestQuery("SELECT 1");
            cpds.setLoginTimeout(d.getIntProperty("login-timeout", 30));

            setConnectionProperties(cpds, d.getProperty("connectionProperties", null));

            logger.info(String.format("Connecting to JDBC data source(%s) "
                    + "with properties(driver=%s, url=%s, user=%s) ...", id, driver, url, user));
            this.cpds = cpds;
            this.cpds.getConnection().close();
            logger.info(String.format("Connected to JDBC data source(%s).", id));
        } catch (Throwable e) {
            cpds.close();

            throw new RuntimeException(String.format("Error when connecting to JDBC data source(%s) "
                    + "with properties (driver=%s, url=%s, user=%s). Error message=%s", id, driver, url, user, e), e);
        }
    }

    private void setConnectionProperties(ComboPooledDataSource cpds, String connectionProperties) {
        Map<String, String> properties = Spliters.by('&', '=').trim().split(connectionProperties);
        boolean hasRewriteBatchedStatements = false;

        for (Map.Entry<String, String> e : properties.entrySet()) {
            String key = e.getKey();

            if (key.equals("rewriteBatchedStatements")) {
                hasRewriteBatchedStatements = true;
            }

            cpds.getProperties().setProperty(key, e.getValue());
        }

        if (!hasRewriteBatchedStatements) {
            cpds.getProperties().setProperty("rewriteBatchedStatements", "true");
        }
    }
}
