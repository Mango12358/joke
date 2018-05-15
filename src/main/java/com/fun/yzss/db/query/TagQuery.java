package com.fun.yzss.db.query;

import com.fun.yzss.db.engine.model.QueryType;

public class TagQuery {
    final public static QueryDef INSERT_INTO = new QueryDef(QueryType.INSERT, "INSERT INTO `tags` (`target_type`, `target_id`, `tag`) VALUES ({targetType}, {targetId}, {tag})  ON DUPLICATE KEY UPDATE tag = {tag}", new String[]{"targetType", "targetId", "tag"});
    final public static QueryDef DELETE = new QueryDef(QueryType.INSERT, "DELETE FROM`tags` WHERE WHERE target_type = {targetType} AND target_id = {targetId} AND tag = {tag}", new String[]{"targetType", "targetId", "tag"});
    final public static QueryDef FIND_ALL = new QueryDef(QueryType.SELECT, "SELECT * FROM `tags`", new String[]{"targetId", "targetType", "tag"});
    final public static QueryDef FIND_BY_TAG = new QueryDef(QueryType.SELECT, "SELECT * FROM `tags` WHERE tag = {tag}", new String[]{"tag"});
    final public static QueryDef FIND_BY_TARGET = new QueryDef(QueryType.SELECT, "SELECT * FROM `tags` WHERE target_type = {targetType} AND target_id = {targetId}", new String[]{"targetType","targetId"});
}
