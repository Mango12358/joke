package com.fun.yzss.model.protocol;

/**
 * Created by fanqq on 2016/7/29.
 */
public class ErrorResponse {
    BaseResponse responseStatus;
    String msg;

    public BaseResponse getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(BaseResponse responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
