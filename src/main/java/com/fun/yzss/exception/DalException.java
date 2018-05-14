package com.fun.yzss.exception;

/**
 * Created by fanqq on 2016/9/23.
 */
public class DalException extends RuntimeException{
    public DalException(String msg, Exception e){
        super(msg);
    }
}
