package com.fun.yzss.db.entity;

import java.util.Date;

public class JokeDo extends DataObject{
    Long id;
    String source;
    byte[] content;
    Long up;
    Long down;
    byte[] comment;
    String hashCode;
    Date createTime;

    public String getHashCode() {
        return hashCode;
    }

    public JokeDo setHashCode(String hashCode) {
        this.hashCode = hashCode;
        return this;
    }

    public Long getId() {
        return id;
    }

    public JokeDo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSource() {
        return source;
    }

    public JokeDo setSource(String source) {
        this.source = source;
        return this;
    }

    public byte[] getContent() {
        return content;
    }

    public JokeDo setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public Long getUp() {
        return up;
    }

    public JokeDo setUp(Long up) {
        this.up = up;
        return this;
    }

    public Long getDown() {
        return down;
    }

    public JokeDo setDown(Long down) {
        this.down = down;
        return this;
    }

    public byte[] getComment() {
        return comment;
    }

    public JokeDo setComment(byte[] comment) {
        this.comment = comment;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public JokeDo setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
}
