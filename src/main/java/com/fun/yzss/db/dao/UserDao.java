package com.fun.yzss.db.dao;

import com.fun.yzss.db.engine.QueryEngine;
import com.fun.yzss.db.entity.UserDo;
import com.fun.yzss.db.query.UserQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by fanqq on 2016/9/23.
 */
@Service("userDao")
public class UserDao {
    @Resource
    QueryEngine queryEngine;

    public UserDo findByName(String name) {
        return queryEngine.querySingle(UserQuery.GET_USER_BY_NAME,new UserDo().setUserName(name),UserDo.class);
    }
}
