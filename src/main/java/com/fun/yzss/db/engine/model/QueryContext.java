package com.fun.yzss.db.engine.model;

import com.fun.yzss.db.entity.DataObject;
import com.fun.yzss.db.query.QueryDef;

/**
 * Created by fanqq on 2016/9/23.
 */
public class QueryContext {
    private QueryDef query;
    private String sqlStatement;
    private DataObject dataObject;
    private int fetchSize;

    public QueryDef getQuery() {
        return query;
    }

    public void setQuery(QueryDef query) {
        this.query = query;
    }

    public String getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public DataObject getDataObject() {
        return dataObject;
    }

    public void setDataObject(DataObject dataObject) {
        this.dataObject = dataObject;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

}
