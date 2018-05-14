package com.fun.yzss.service.tag;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fanqq on 2016/10/10.
 */
public interface TagService {

    public void tagging(String tagName, String type, Long targetId) throws Exception;

    public void tagging(String tagName, String type, Long[] targetIds) throws Exception;

    public void untagging(String tagName, String type, Long targetId) throws Exception;

    public void untagging(String tagName, String type, Long[] targetIds) throws Exception;

    public void clear(String type, Long itemId) throws Exception;

    public void removeTag(String name, boolean force) throws Exception;

    public Set<Long> queryByType(String type) throws Exception;

    public Set<Long> queryByTypeAndTag(String tagName, String type) throws Exception;

    public Set<Long> unionQuery(List<String> tagNames, String type) throws Exception;

    public Set<Long> joinQuery(List<String> tagNames, String type) throws Exception;

    public Set<String> getTags(Long targetId) throws Exception;

    public Set<String> getTags(Long[] targetIds) throws Exception;

    public Set<String> getTags(String type, Long itemId) throws Exception;

    public Map<Long, List<String>> getTags(String type, Long[] itemIds) throws Exception;
}
