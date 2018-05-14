package com.fun.yzss.service.joke.model;

import java.util.HashMap;
import java.util.Map;

public class MetaData {
    public Map<String, Long> base = new HashMap<>();
    public Map<String, Long> fresh = new HashMap<>();
    public Long max = 0L;

    public Map<String, Long> getBase() {
        return base;
    }

    public void setBase(Map<String, Long> base) {
        this.base = base;
    }

    public Map<String, Long> getFresh() {
        return fresh;
    }

    public void setFresh(Map<String, Long> fresh) {
        this.fresh = fresh;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }
}