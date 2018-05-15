package com.fun.yzss.db.query;

import com.fun.yzss.db.engine.model.QueryType;

public class ImageQuery {
    final public static QueryDef INSERT_INTO = new QueryDef(QueryType.INSERT, "INSERT INTO `images` (`id`,`type`, `url`, `source_id`,`cos_uri`,`tag_list`) VALUES ({id},{type}, {url}, {sourceId},{cosURI},{tagList})  ON DUPLICATE KEY UPDATE type={type} , url={url},source_id={sourceId} , cos_uri = {cosURI} , tag_list = {tagList}" , new String[]{"id","type", "url", "sourceId","cosURI","tagList"});
    final public static QueryDef DELETE = new QueryDef(QueryType.DELETE, "DELETE FROM`images` WHERE source_id = {sourceId}", new String[]{"sourceId"});
    final public static QueryDef UPDATE_BY_ID = new QueryDef(QueryType.UPDATE, "UPDATE `images` SET type={type} , url={url},source_id={sourceId} , cos_uri = {cosURI} , tag_list = {tagList} WHERE id = {id}", new String[]{"id","type", "url", "sourceId","cosURI","tagList"});

}
