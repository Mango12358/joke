package com.fun.yzss.service.joke.model;

import java.util.ArrayList;
import java.util.List;

public class Joke {
    Long id;
    String source;
    String content;
    Long up;
    Long down;
    List<String> comment;

    public Joke() {
        id = 0L;
        source = null;
        content = null;
        up = 0L;
        down = 0L;
        comment = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUp() {
        return up;
    }

    public void setUp(Long up) {
        this.up = up;
    }

    public Long getDown() {
        return down;
    }

    public void setDown(Long down) {
        this.down = down;
    }

    public List<String> getComment() {
        return comment;
    }

    public void setComment(List<String> comment) {
        this.comment = comment;
    }
}
