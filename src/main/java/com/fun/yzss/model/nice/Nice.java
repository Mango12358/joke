package com.fun.yzss.model.nice;

import java.util.Date;

/**
 * Created by fanqq on 2016/10/9.
 */
public class Nice {
    Long id;
    String type;
    Long targetId;
    Long userId;
    boolean nice;
    Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isNice() {
        return nice;
    }

    public void setNice(boolean nice) {
        this.nice = nice;
    }

}
