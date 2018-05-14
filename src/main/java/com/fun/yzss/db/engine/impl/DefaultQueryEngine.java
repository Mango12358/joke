package com.fun.yzss.db.engine.impl;

import com.fun.yzss.db.engine.QueryEngine;
import com.fun.yzss.db.engine.QueryExecutor;
import com.fun.yzss.db.engine.model.QueryContext;
import com.fun.yzss.db.entity.DataObject;
import com.fun.yzss.db.query.QueryDef;
import com.fun.yzss.exception.DalException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanqq on 2016/9/26.
 */
@Service("queryEngine")
public class DefaultQueryEngine implements QueryEngine {
    @Resource
    QueryExecutor queryExecutor;


    @Override
    public <T extends DataObject> int[] deleteBatch(QueryDef query, T[] protos) throws DalException {
        if (protos.length == 0) {
            return new int[0];
        }

        QueryContext ctx = new QueryContext();
        ctx.setQuery(query);
        ctx.setDataObject(protos[0]);
        resolveSqlStatement(ctx);

        return queryExecutor.executeUpdateBatch(ctx, protos);
    }

    @Override
    public <T extends DataObject> int deleteSingle(QueryDef query, T proto) throws DalException {
        QueryContext ctx = new QueryContext();
        ctx.setQuery(query);
        ctx.setDataObject(proto);
        resolveSqlStatement(ctx);

        return queryExecutor.executeUpdate(ctx);
    }

    @Override
    public <T extends DataObject> int[] insertBatch(QueryDef query, T[] protos) throws DalException {
        if (protos.length == 0) {
            return new int[0];
        }

        QueryContext ctx = new QueryContext();
        ctx.setQuery(query);
        ctx.setDataObject(protos[0]);
        resolveSqlStatement(ctx);

        return queryExecutor.executeUpdateBatch(ctx, protos);
    }

    @Override
    public <T extends DataObject> int insertSingle(QueryDef query, T proto) throws DalException {
        QueryContext ctx = new QueryContext();
        ctx.setQuery(query);
        ctx.setDataObject(proto);
        resolveSqlStatement(ctx);

        return queryExecutor.executeUpdate(ctx);
    }

    @Override
    public <T extends DataObject> List<T> queryMultiple(QueryDef query, DataObject proto, Class<T> resultClass) throws DalException {
        QueryContext ctx = new QueryContext();
        ctx.setDataObject(proto);
        ctx.setQuery(query);
        resolveSqlStatement(ctx);
        List<T> result = queryExecutor.executeQuery(ctx, resultClass);
        if (result == null || result.size() == 0) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public <T extends DataObject> T querySingle(QueryDef query, DataObject proto, Class<T> resultClass) throws DalException {
        QueryContext ctx = new QueryContext();
        ctx.setDataObject(proto);
        ctx.setQuery(query);
        ctx.setFetchSize(1);
        resolveSqlStatement(ctx);
        List<T> result = queryExecutor.executeQuery(ctx, resultClass);
        if (result == null || result.size() == 0) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public <T extends DataObject> int[] updateBatch(QueryDef query, T[] protos) throws DalException {
        if (protos.length == 0) {
            return new int[0];
        }

        QueryContext ctx = new QueryContext();
        ctx.setQuery(query);
        ctx.setDataObject(protos[0]);
        resolveSqlStatement(ctx);

        return queryExecutor.executeUpdateBatch(ctx, protos);
    }

    @Override
    public <T extends DataObject> int updateSingle(QueryDef query, T proto) throws DalException {
        QueryContext ctx = new QueryContext();
        ctx.setQuery(query);
        ctx.setDataObject(proto);
        resolveSqlStatement(ctx);

        return queryExecutor.executeUpdate(ctx);
    }

    @Override
    public void resolveSqlStatement(QueryContext ctx) throws DalException {
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(ctx.getQuery().getQuery());
        String sql = ctx.getQuery().getQuery();
        try {
            while (matcher.find()) {
                String name = matcher.group(1);
                Object obj = ctx.getDataObject().getByName(name);
                int size = 1;
                if (obj instanceof List) {
                    size = ((List) obj).size();
                }
                String to = "";
                for (int i = 0; i < size; i++) {
                    if (i == 0) {
                        to += "?";
                    } else {
                        to += ",?";
                    }
                }
                sql = sql.replaceFirst("\\{" + name + "\\}", to);
            }
            ctx.setSqlStatement(sql);
        } catch (Exception e) {
            throw new DalException("Parser Query failed.", e);
        }
    }
}
