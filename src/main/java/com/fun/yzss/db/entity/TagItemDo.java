package com.fun.yzss.db.entity;

/**
 * Created by fanqq on 2016/10/10.
 */
public class TagItemDo extends DataObject {
    Long targetId;
    Long tagId;
    String type;

    public Long getTagId() {
        return tagId;
    }

    public TagItemDo setTagId(Long tagId) {
        this.tagId = tagId;
        return this;
    }

    public String getType() {
        return type;
    }

    public TagItemDo setType(String type) {
        this.type = type;
        return this;
    }

    public Long getTargetId() {
        return targetId;
    }

    public TagItemDo setTargetId(Long targetId) {
        this.targetId = targetId;
        return this;
    }
}
