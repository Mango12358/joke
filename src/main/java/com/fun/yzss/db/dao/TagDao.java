package com.fun.yzss.db.dao;

import com.fun.yzss.db.engine.QueryEngine;
import com.fun.yzss.db.entity.TagDo;
import com.fun.yzss.db.query.TagQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by fanqq on 2016/10/10.
 */
@Service("tagDao")
public class TagDao {

    @Resource
    private QueryEngine queryEngine;

    public List<TagDo> findAllByTarget(String targetType, Long targetId) {
        return queryEngine.queryMultiple(TagQuery.FIND_BY_TAG, new TagDo().setTargetType(targetType).setTargetId(targetId), TagDo.class);
    }

    public List<TagDo> findAll() {
        return queryEngine.queryMultiple(TagQuery.FIND_BY_TAG, new TagDo(), TagDo.class);
    }

    public List<TagDo> findByTag(String tag) {
        return queryEngine.queryMultiple(TagQuery.FIND_BY_TAG, new TagDo().setTag(tag), TagDo.class);
    }

    public void delete(TagDo d) {
        queryEngine.deleteSingle(TagQuery.DELETE, d);
    }

    public int insert(TagDo d) {
        return queryEngine.insertSingle(TagQuery.INSERT_INTO, d);
    }

    public int[] insertBatch(TagDo[] d) {
        return queryEngine.insertBatch(TagQuery.INSERT_INTO, d);
    }
}
