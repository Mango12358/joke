package com.fun.yzss.db.query;

import com.fun.yzss.db.engine.model.QueryType;

public class JokeQuery {
    final public static QueryDef INSERT_INTO = new QueryDef(QueryType.INSERT, "INSERT INTO `jokes` (`source`, `up`, `down`, `content`, `comment` ,`hash_code` ) VALUES ({source}, {up}, {down}, {content}, {comment},{hashCode})  ON DUPLICATE KEY UPDATE up = {up} , down = {down}", new String[]{"source", "up", "down", "content", "comment", "hashCode"});
    final public static QueryDef FIND_ALL = new QueryDef(QueryType.SELECT, "SELECT * FROM `jokes`", new String[]{"id","source", "up", "down", "content", "comment", "createTime","hashCode"});
}
