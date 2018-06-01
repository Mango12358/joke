package com.fun.yzss.db.query;

import com.fun.yzss.db.engine.model.QueryType;

public class ImageQuery {
    final public static QueryDef INSERT_INTO = new QueryDef(QueryType.INSERT, "INSERT INTO `images` (`id`,`type`," +
            " `url`, `source_id`,`cos_uri`,`tag_list`,`height`,`width`,`status`) VALUES ({id},{type}, {url}, {sourceId}," +
            "{cosURI},{tagList},{height},{width},{status})  ON DUPLICATE KEY UPDATE type={type} , url={url},source_id={sourceId} , " +
            "cos_uri = {cosURI} , tag_list = {tagList} , height = {height},width = {width},status={status}",
            new String[]{"id", "type", "url", "sourceId", "cosURI", "tagList", "height", "width", "status"});
    final public static QueryDef DELETE = new QueryDef(QueryType.DELETE, "DELETE FROM`images` WHERE source_id = {sourceId}", new String[]{"sourceId"});
    final public static QueryDef UPDATE_BY_ID = new QueryDef(QueryType.UPDATE, "UPDATE `images` SET type={type} ," +
            " url={url},source_id={sourceId} , cos_uri = {cosURI} , tag_list = {tagList} , height = {height},width = {width}," +
            "status={status} WHERE id = {id}", new String[]{"id", "type", "url", "sourceId", "cosURI", "tagList", "height", "width", "status"});
    final public static QueryDef FIND_ALL = new QueryDef(QueryType.SELECT, "SELECT * FROM `images`",
            new String[]{"id", "type", "url", "sourceId", "cosURI", "tagList", "randomIndex", "height", "width", "status"});
    final public static QueryDef FIND_BY_TYPE = new QueryDef(QueryType.SELECT, "SELECT * FROM `images` where type={type}",
            new String[]{"id", "type", "url", "sourceId", "cosURI", "tagList", "randomIndex", "height", "width", "status"});
    final public static QueryDef UPDATE_RANDOM_IDX = new QueryDef(QueryType.UPDATE, "UPDATE `images` SET random_index = RAND() * 2100000000",
            new String[]{"id", "type", "url", "sourceId", "cosURI", "tagList", "randomIndex", "height", "width", "status"});


    final public static QueryDef FIND_BY_TYPE_WITH_LIMIT = new QueryDef(QueryType.SELECT, "SELECT * FROM `images` where type={type} limit {offset} , {len}",
            new String[]{"id", "type", "url", "sourceId", "cosURI", "tagList", "randomIndex", "height", "width", "status", "choice"});


}
