package com.fun.yzss.db.entity;

public class ImageDo extends DataObject {
    Long id;
    String type;
    String url;
    Long sourceId;
    String cosURI;
    String tagList;

    Long height;
    Long width;
    String status;
    Long randomIndex;
    Integer choice;



    Long offset;
    Long len;

    public Long getOffset() {
        return offset;
    }

    public ImageDo setOffset(Long offset) {
        this.offset = offset;
        return this;
    }

    public Long getLen() {
        return len;
    }

    public ImageDo setLen(Long len) {
        this.len = len;
        return this;
    }

    public String getCosURI() {
        return cosURI;
    }

    public ImageDo setCosURI(String cosURI) {
        this.cosURI = cosURI;
        return this;
    }

    public String getTagList() {
        return tagList;
    }

    public ImageDo setTagList(String tagList) {
        this.tagList = tagList;
        return this;
    }

    public Long getId() {
        return id;
    }

    public ImageDo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public ImageDo setType(String type) {
        this.type = type;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ImageDo setUrl(String url) {
        this.url = url;
        return this;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public ImageDo setSourceId(Long sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public Long getHeight() {
        return height;
    }

    public ImageDo setHeight(Long height) {
        this.height = height;
        return this;
    }

    public Long getWidth() {
        return width;
    }

    public ImageDo setWidth(Long width) {
        this.width = width;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ImageDo setStatus(String status) {
        this.status = status;
        return this;
    }

    public Long getRandomIndex() {
        return randomIndex;
    }

    public ImageDo setRandomIndex(Long randomIndex) {
        this.randomIndex = randomIndex;
        return this;
    }
}
