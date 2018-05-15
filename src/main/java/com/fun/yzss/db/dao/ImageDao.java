package com.fun.yzss.db.dao;

import com.fun.yzss.db.engine.QueryEngine;
import com.fun.yzss.db.entity.ImageDo;
import com.fun.yzss.db.query.ImageQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("imageDao")
public class ImageDao {

    @Resource
    private QueryEngine queryEngine;

    public int insert(ImageDo d) {
        return queryEngine.insertSingle(ImageQuery.INSERT_INTO, d.setId(0L));
    }

    public int[] insertBatch(ImageDo[] d) {
        return queryEngine.insertBatch(ImageQuery.INSERT_INTO, d);
    }

    public void  update(ImageDo d){
        queryEngine.updateSingle(ImageQuery.UPDATE_BY_ID, d);
    }
}
