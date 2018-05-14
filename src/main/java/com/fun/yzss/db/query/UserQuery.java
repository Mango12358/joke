package com.fun.yzss.db.query;

import com.fun.yzss.db.engine.model.QueryType;

/**
 * Created by fanqq on 2016/9/23.
 */
public class UserQuery {
    final public static QueryDef GET_USER_BY_NAME = new QueryDef(QueryType.SELECT, "SELECT name FROM `group` WHERE name = {name}", new String[]{"name"});
}
