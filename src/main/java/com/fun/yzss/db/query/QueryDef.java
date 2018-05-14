package com.fun.yzss.db.query;

import com.fun.yzss.db.engine.model.QueryType;

/**
 * Created by fanqq on 2016/9/26.
 */
public class QueryDef {
    String query;

    QueryType type;

    String[] objName;

    QueryDef(QueryType type, String query, String[] objNames) {
        this.query = query;
        this.type = type;
        this.objName = objNames;
    }

    public QueryType getType() {
        return type;
    }

    public void setType(QueryType type) {
        this.type = type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getObjNames() {
        return objName;
    }

    public void setObjNames(String[] objName) {
        this.objName = objName;
    }

}
