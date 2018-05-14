package com.fun.yzss.db.entity;

/**
 * Created by fanqq on 2016/9/23.
 */
public class UserDo extends DataObject {
    String name;

    public String getUserName() {
        return name;
    }

    public UserDo setUserName(String name) {
        this.name = name;
        return this;
    }


}
