package com.fun.yzss.db.engine.impl;

import com.fun.yzss.db.engine.QueryExecutor;
import com.fun.yzss.db.engine.TransactionManager;
import com.fun.yzss.db.engine.model.QueryContext;
import com.fun.yzss.db.engine.model.QueryType;
import com.fun.yzss.db.entity.DataObject;
import com.fun.yzss.exception.DalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanqq on 2016/9/23.
 */
@Service("queryExecutor")
public class DefaultQueryExecutor implements QueryExecutor {

    @Resource
    TransactionManager transactionManager;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public <T extends DataObject> List<T> executeQuery(QueryContext ctx, Class<T> resultClass) {
        PreparedStatement ps = null;
        try {
//            resolveSqlStatement(ctx);
            ps = createPreparedStatement(ctx, transactionManager.getConnection());
            if (ctx.getFetchSize() > 0) {
                ps.setFetchSize(ctx.getFetchSize());
            }
            setInParameters(ps, ctx, ctx.getDataObject());
            ResultSet rs = ps.executeQuery();
            List<T> result = assembleResultSet(rs, ctx, resultClass);
            return result;
        } catch (Exception e) {
            transactionManager.reset();
            logger.error("Execute Query Failed.", e);
            throw new DalException(String.format("Error when executing query(%s) failed, proto: %s, message: %s.",
                    ctx.getSqlStatement(), ctx.getDataObject(), e), e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DalException("Error when closing PreparedStatement, message: " + e, e);
                }
            }
            transactionManager.closeConnection();
        }
    }


    @Override
    public int executeUpdate(QueryContext ctx) {
        PreparedStatement ps = null;
        try {
//            resolveSqlStatement(ctx);
            ps = createPreparedStatement(ctx, transactionManager.getConnection());

            setInParameters(ps, ctx, ctx.getDataObject());

            int rowCount = ps.executeUpdate();

            if (ctx.getQuery().getType() == QueryType.INSERT && ps.getGeneratedKeys().next()) {
                ctx.getDataObject().setId(ps.getGeneratedKeys().getLong(1));
            }
            return rowCount;
        } catch (Exception e) {
            transactionManager.reset();
            logger.error("Execute Query Failed.", e);
            throw new DalException(String.format("Error when executing update(%s) failed, proto: %s, message: %s.",
                    ctx.getSqlStatement(), ctx.getDataObject(), e), e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DalException("Error when closing PreparedStatement, message: " + e, e);
                }
            }
            transactionManager.closeConnection();
        }
    }

    @Override
    public <T extends DataObject> int[] executeUpdateBatch(QueryContext ctx, T[] protos) {
        if (protos == null || protos.length == 0) return new int[0];
        PreparedStatement ps = null;
        boolean inTransaction = transactionManager.isInTransaction();
        int[] result;
        try {
//            resolveSqlStatement(ctx);
            ps = createPreparedStatement(ctx, transactionManager.getConnection());
            if (!inTransaction) {
                ps.getConnection().setAutoCommit(false);
            }
            for (int i = 0; i < protos.length; i++) {
                // Setup IN/OUT parameters
                setInParameters(ps, ctx, protos[i]);

                ps.addBatch();
            }

            result = ps.executeBatch();

            // Retrieve Generated Keys if have
            if (ctx.getQuery().getType() == QueryType.INSERT) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                for (T pro : protos) {
                    if (generatedKeys != null && generatedKeys.next()) {
                        pro.setId(generatedKeys.getLong(1));
                    }
                }
            }

            if (!inTransaction) {
                ps.getConnection().commit();
                ps.getConnection().setAutoCommit(true);
            }
            return result;
        } catch (Exception e) {
            if (!inTransaction) {
                try {
                    if (ps != null) {
                        ps.getConnection().rollback();
                        ps.getConnection().setAutoCommit(true);
                    }
                } catch (SQLException sqle) {
                    if (e instanceof SQLException) {
                        ((SQLException) e).setNextException(sqle);
                    }
                }
            }
            transactionManager.reset();

            throw new DalException(String.format("Error when executing batch update(%s) failed, proto: %s, message: %s.",
                    ctx.getSqlStatement(), protos, e), e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DalException("Error when closing PreparedStatement, message: " + e, e);
                }
            }
            transactionManager.closeConnection();
        }
    }


    protected PreparedStatement createPreparedStatement(QueryContext ctx, Connection conn) throws SQLException {
        QueryType type = ctx.getQuery().getType();
        PreparedStatement ps;

        if (type == QueryType.SELECT) {
            ps = conn.prepareStatement(ctx.getSqlStatement(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        } else {
            ps = conn.prepareStatement(ctx.getSqlStatement(), PreparedStatement.RETURN_GENERATED_KEYS);
        }
        return ps;
    }

    private <T extends DataObject> void setInParameters(PreparedStatement ps, QueryContext ctx, T prod) throws DalException {
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(ctx.getQuery().getQuery());
        int index = 1;
        try {
            while (matcher.find()) {
                String name = matcher.group(1);
                Object obj = prod.getByName(name);
                if (obj instanceof List) {
                    for (Object o : (List) obj) {
                        ps.setObject(index, o);
                        index++;
                    }
                } else {
                    ps.setObject(index, obj);
                    index++;
                }
            }
        } catch (Exception e) {
            throw new DalException("Add Parameters failed.", e);
        }
    }

    private <T extends DataObject> List<T> assembleResultSet(ResultSet rs, QueryContext ctx, Class<T> resultClass) throws Exception {
        List<T> res = new ArrayList<>();
        int i;
        T row;
        while (rs.next()) {
            row = resultClass.newInstance();
            i = 1;
            for (String name : ctx.getQuery().getObjNames()) {
                row.setByName(name, rs.getObject(i++));
            }
            res.add(row);
        }

        return res;
    }
}
