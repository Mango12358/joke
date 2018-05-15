//package com.fun.yzss.service.tag.impl;
//
//import com.fun.yzss.db.dao.TagDao;
//import com.fun.yzss.db.dao.TagItemDao;
//import com.fun.yzss.db.engine.InTransaction;
//import com.fun.yzss.db.entity.TagDo;
//import com.fun.yzss.db.entity.TagItemDo;
//import com.fun.yzss.exception.ValidateException;
//import com.fun.yzss.service.tag.TagService;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.*;
//
///**
// * Created by fanqq on 2016/10/10.
// */
//@Service("tagService")
//public class TagServiceImpl implements TagService {
//    @Resource
//    private TagDao tagDao;
//    @Resource
//    private TagItemDao tagItemDao;
//
//
//    @Override
//    @InTransaction
//    public void tagging(String tagName, String type, Long targetId) throws Exception {
//        TagDo d = tagDao.findByName(tagName);
//        if (d == null) {
//            d = new TagDo().setName(tagName);
//            tagDao.insert(d);
//        }
//        TagItemDo l = new TagItemDo().setTagId(d.getId()).setType(type).setTargetId(targetId);
//
//        tagItemDao.insert(l);
//    }
//
//    @Override
//    @InTransaction
//    public void tagging(String tagName, String type, Long[] targetIds) throws Exception {
//        TagDo d = tagDao.findByName(tagName);
//        if (d == null) {
//            d = new TagDo().setName(tagName);
//            tagDao.insert(d);
//        }
//        TagItemDo[] l = new TagItemDo[targetIds.length];
//        for (int i = 0; i < targetIds.length; i++) {
//            l[i] = new TagItemDo().setTagId(d.getId()).setType(type).setTargetId(targetIds[i]);
//        }
//        tagItemDao.insert(l);
//    }
//
//    @Override
//    @InTransaction
//    public void untagging(String tagName, String type, Long targetId) throws Exception {
//        TagDo d = tagDao.findByName(tagName);
//        if (d == null) {
//            throw new ValidateException("Tag named " + tagName + "is not found.");
//        }
//        if (targetId != null) {
//            TagItemDo l = new TagItemDo().setTagId(d.getId()).setType(type).setTargetId(targetId);
//            tagItemDao.deleteTagItems(l);
//        } else {
//            tagItemDao.deleteTagType(new TagItemDo().setTagId(d.getId()).setType(type));
//        }
//    }
//
//    @Override
//    @InTransaction
//    public void untagging(String tagName, String type, Long[] targetIds) throws Exception {
//        TagDo d = tagDao.findByName(tagName);
//        if (d == null) {
//            throw new ValidateException("Tag named " + tagName + "is not found.");
//        }
//        if (targetIds != null) {
//            TagItemDo[] l = new TagItemDo[targetIds.length];
//            for (int i = 0; i < targetIds.length; i++) {
//                l[i] = new TagItemDo().setTagId(d.getId()).setType(type).setTargetId(targetIds[i]);
//            }
//            tagItemDao.deleteTagItems(l);
//        } else {
//            tagItemDao.deleteTagType(new TagItemDo().setTagId(d.getId()).setType(type));
//        }
//    }
//
//    @Override
//    @InTransaction
//    public void clear(String type, Long itemId) throws Exception {
//        List<TagItemDo> list = tagItemDao.findByItemAndType(itemId, type);
//        if (list.size() > 0) {
//            tagItemDao.deleteById(list.toArray(new TagItemDo[list.size()]));
//        }
//    }
//
//    @Override
//    @InTransaction
//    public void removeTag(String name, boolean force) throws Exception {
//        TagDo d = tagDao.findByName(name);
//        if (d == null) return;
//        if (force) {
//            tagItemDao.deleteTag(new TagItemDo().setTagId(d.getId()));
//        } else {
//            List<TagItemDo> check = tagItemDao.findByTag(d.getId());
//            if (check.size() > 0)
//                throw new Exception("Combination exists with tag " + name + ".");
//        }
//        tagDao.delete(d);
//    }
//
//    @Override
//    @InTransaction
//    public Set<Long> queryByType(String type) throws Exception {
//        Set<Long> result = new HashSet<>();
//        for (TagItemDo d : tagItemDao.findAllByType(type)) {
//            result.add(d.getTagId());
//        }
//        return result;
//    }
//
//    @Override
//    @InTransaction
//    public Set<Long> queryByTypeAndTag(String tagName, String type) throws Exception {
//        Set<Long> result = new HashSet<>();
//        for (TagItemDo d : tagItemDao.findAllByType(tagName, type)) {
//            result.add(d.getTagId());
//        }
//        return result;
//    }
//
//    @Override
//    @InTransaction
//    public Set<Long> unionQuery(List<String> tagNames, String type) throws Exception {
//        List<TagDo> tags = tagDao.findAllByNames(tagNames.toArray(new String[tagNames.size()]));
//        if (tags.size() == 0) return new HashSet<>();
//
//        Long[] tagIds = new Long[tags.size()];
//        for (int i = 0; i < tagIds.length; i++) {
//            tagIds[i] = tags.get(i).getId();
//        }
//
//        Set<Long> result = new HashSet<>();
//        for (TagItemDo d : tagItemDao.findByTagsAndType(tagIds, type)) {
//            result.add(d.getTargetId());
//        }
//        return result;
//    }
//
//    @Override
//    @InTransaction
//    public Set<Long> joinQuery(List<String> tagNames, String type) throws Exception {
//        List<TagDo> tags = tagDao.findAllByNames(tagNames.toArray(new String[tagNames.size()]));
//        if (tags.size() == 0) return new HashSet<>();
//
//        Long[] tagIds = new Long[tags.size()];
//        for (int i = 0; i < tagIds.length; i++) {
//            tagIds[i] = tags.get(i).getId();
//        }
//        if (tagIds.length < tagNames.size()) return new HashSet<>();
//
//        int joinedValue = tagIds.length;
//        Map<Long, Counter> marker = new HashMap<>();
//        for (TagItemDo d : tagItemDao.findByTagsAndType(tagIds, type)) {
//            Counter m = marker.get(d.getTargetId());
//            if (m == null) {
//                marker.put(d.getTargetId(), new Counter());
//            } else {
//                m.incr();
//            }
//        }
//
//        Set<Long> result = new HashSet<>();
//        for (Map.Entry<Long, Counter> e : marker.entrySet()) {
//            if (e.getValue().get() == joinedValue) result.add(e.getKey());
//        }
//        return result;
//    }
//
//    @Override
//    @InTransaction
//    public Set<String> getTags(Long targetId) throws Exception {
//        Set<String> result = new HashSet<>();
//        for (TagDo d : tagDao.findAllByIds(new Long[]{targetId})) {
//            result.add(d.getName());
//        }
//        return result;
//    }
//
//    @Override
//    @InTransaction
//    public Set<String> getTags(Long[] targetIds) throws Exception {
//        Set<String> result = new HashSet<>();
//        for (TagDo d : tagDao.findAllByIds(targetIds)) {
//            result.add(d.getName());
//        }
//        return result;
//    }
//
//    @Override
//    @InTransaction
//    public Set<String> getTags(String type, Long itemId) throws Exception {
//        List<TagItemDo> list = tagItemDao.findByItemAndType(itemId, type);
//        Set<String> result = new HashSet<>();
//        if (list.size() == 0)
//            return result;
//        Long[] tagIds = new Long[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            tagIds[i] = list.get(i).getTagId();
//        }
//        for (TagDo tagDo : tagDao.findAllByIds(tagIds)) {
//            result.add(tagDo.getName());
//        }
//        return result;
//    }
//
//    @Override
//    @InTransaction
//    public Map<Long, List<String>> getTags(String type, Long[] itemIds) throws Exception {
//        Map<Long, List<Long>> rItemTag = new HashMap<>();
//        Set<Long> tagIds = new HashSet<>();
//
//        for (TagItemDo d : tagItemDao.findAllByItemsAndType(itemIds, type)) {
//            tagIds.add(d.getTagId());
//            List<Long> l = rItemTag.get(d.getTargetId());
//            if (l == null) {
//                l = new ArrayList<>();
//                rItemTag.put(d.getTargetId(), l);
//            }
//            l.add(d.getTagId());
//        }
//
//        if (tagIds.size() == 0) return new HashMap<>();
//
//        Map<Long, String> rIdName = new HashMap<>();
//        for (TagDo d : tagDao.findAllByIds(tagIds.toArray(new Long[tagIds.size()]))) {
//            rIdName.put(d.getId(), d.getName());
//        }
//
//        Map<Long, List<String>> result = new HashMap<>();
//        for (Map.Entry<Long, List<Long>> e : rItemTag.entrySet()) {
//            List<String> l = new ArrayList<>();
//            result.put(e.getKey(), l);
//            for (Long i : e.getValue()) {
//                String r = rIdName.get(i);
//                if (r != null) l.add(r);
//            }
//        }
//
//        return result;
//    }
//
//    private class Counter {
//        int count = 1;
//
//        public void incr() {
//            count++;
//        }
//
//        public int get() {
//            return count;
//        }
//    }
//}
//
//
