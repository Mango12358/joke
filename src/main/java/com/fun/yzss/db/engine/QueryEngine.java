package com.fun.yzss.db.engine;

import com.fun.yzss.db.engine.model.QueryContext;
import com.fun.yzss.db.entity.DataObject;
import com.fun.yzss.db.query.QueryDef;
import com.fun.yzss.exception.DalException;

import java.util.List;

/**
 * Created by fanqq on 2016/9/26.
 */
public interface QueryEngine {

    public <T extends DataObject> int[] deleteBatch(QueryDef query, T[] protos) throws DalException;

    public <T extends DataObject> int deleteSingle(QueryDef query, T proto) throws DalException;

    public <T extends DataObject> int[] insertBatch(QueryDef query, T[] protos) throws DalException;

    public <T extends DataObject> int insertSingle(QueryDef query, T proto) throws DalException;

    public <T extends DataObject> List<T> queryMultiple(QueryDef query, DataObject proto, Class<T> resultClass) throws DalException;

    public <T extends DataObject> T querySingle(QueryDef query, DataObject proto, Class<T> resultClass) throws DalException;

    public <T extends DataObject> int[] updateBatch(QueryDef query, T[] protos) throws DalException;

    public <T extends DataObject> int updateSingle(QueryDef query, T proto) throws DalException;

    public void resolveSqlStatement(QueryContext ctx) throws DalException;
}
