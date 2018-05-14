package com.fun.yzss.db.entity;

/**
 * Created by fanqq on 2016/10/10.
 */
public class TagDo extends DataObject {
    String name;
    Long id;

    public Long getId() {
        return id;
    }

    public TagDo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TagDo setName(String name) {
        this.name = name;
        return this;
    }
}
