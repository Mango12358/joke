package com.fun.yzss.db.dao;

import com.fun.yzss.db.entity.TagDo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fanqq on 2016/10/10.
 */
@Service("tagDao")
public class TagDao {
    public List<TagDo> findAllByNames(String[] strings) {
        return null;
    }

    public TagDo[] findAllByIds(Long[] targetIds) {
        return new TagDo[0];
    }

    public TagDo findByName(String name) {
        return null;
    }

    public void delete(TagDo d) {

    }

    public void insert(TagDo d) {

    }
}
