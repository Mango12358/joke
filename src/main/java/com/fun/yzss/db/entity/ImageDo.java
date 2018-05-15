package com.fun.yzss.db.entity;

public class ImageDo extends DataObject{
    Long id;
    String type;
    String url;
    String sourceId;
    String cosURI;
    String tagList;

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

    public String getSourceId() {
        return sourceId;
    }

    public ImageDo setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }
}
