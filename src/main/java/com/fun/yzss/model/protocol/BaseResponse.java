package com.fun.yzss.model.protocol;

import java.util.Date;

/**
 * Created by fanqq on 2016/7/29.
 */
public class BaseResponse {
    String ack;
    String message;
    String errorCode;
    String errorMessage;
    Long timestamp;
    public BaseResponse(){
        ack = "";
        message = "";
        errorCode = "";
        errorMessage = "";
        timestamp = new Date().getTime();
    }
    public BaseResponse(String ack){
        this.ack = ack;
        message = "";
        errorCode = "";
        errorMessage = "";
        timestamp = new Date().getTime();
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
