package com.fun.yzss.db.engine;

import com.fun.yzss.db.entity.DataObject;
import com.fun.yzss.db.engine.model.QueryContext;

import java.util.List;

/**
 * Created by fanqq on 2016/9/23.
 */
public interface QueryExecutor {
    public <T extends DataObject> List<T> executeQuery(QueryContext ctx, Class<T> resultClass);

    public int executeUpdate(QueryContext ctx) ;

    public <T extends DataObject> int[] executeUpdateBatch(QueryContext ctx, T[] protos) ;
}
