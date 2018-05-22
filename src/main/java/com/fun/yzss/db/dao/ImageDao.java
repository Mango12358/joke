package com.fun.yzss.db.dao;

import com.fun.yzss.db.engine.QueryEngine;
import com.fun.yzss.db.entity.ImageDo;
import com.fun.yzss.db.query.ImageQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    public void update(ImageDo d) {
        queryEngine.updateSingle(ImageQuery.UPDATE_BY_ID, d);
    }

    public List<ImageDo> getAll() {
        return queryEngine.queryMultiple(ImageQuery.FIND_ALL, new ImageDo(), ImageDo.class);
    }

    public List<ImageDo> getByType(String type) {
        return queryEngine.queryMultiple(ImageQuery.FIND_BY_TYPE, new ImageDo().setType(type), ImageDo.class);
    }

    public List<ImageDo> getByTypeWithLimit(String type, Long offSet, Long len) {
        return queryEngine.queryMultiple(ImageQuery.FIND_BY_TYPE_WITH_LIMIT, new ImageDo().setType(type).setOffset(offSet).setLen(len), ImageDo.class);
    }

    public void updateRandomIndex() {
        queryEngine.updateSingle(ImageQuery.UPDATE_RANDOM_IDX, new ImageDo());
    }
}
