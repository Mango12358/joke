package com.fun.yzss.db.entity;

/**
 * Created by fanqq on 2016/10/10.
 */
public class TagDo extends DataObject {
    String tag;
    Long targetId;
    String targetType;

    public String getTag() {
        return tag;
    }

    public TagDo setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public Long getTargetId() {
        return targetId;
    }

    public TagDo setTargetId(Long targetId) {
        this.targetId = targetId;
        return this;
    }

    public String getTargetType() {
        return targetType;
    }

    public TagDo setTargetType(String targetType) {
        this.targetType = targetType;
        return this;
    }
}
