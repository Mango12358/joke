package com.fun.yzss.aop;

import com.alibaba.fastjson.JSON;
import com.fun.yzss.exception.AuthException;
import com.fun.yzss.exception.NotFoundException;
import com.fun.yzss.exception.ValidateException;
import com.fun.yzss.model.protocol.BaseResponse;
import com.fun.yzss.model.protocol.ErrorResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by fanqq on 2016/7/15.
 */
@Aspect
@Component("exceptionAop")
public class ExceptionAop implements Ordered {
    @Override
    public int getOrder() {
        return AopOrder.Exception;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.fun.yzss.resources.*Resource.*(..))")
    public Object exception(ProceedingJoinPoint point) throws Throwable {
        String objectName = point.getSignature().getDeclaringTypeName();
        String methodName = point.getSignature().getName();
        try {
            return point.proceed();
        } catch (Throwable throwable) {
            ErrorResponse response = new ErrorResponse();
            response.setResponseStatus(new BaseResponse("Fail"));
            logger.error(objectName + " throws an error when calling " + methodName + ".", throwable);
            if (throwable instanceof ValidateException) {
                response.setMsg("Bad Request! Data is illegal.");
                return Response.status(400).entity(JSON.toJSONString(response)).build();
            }
            if (throwable instanceof NotFoundException) {
                response.setMsg("Not Found Error.");
                return Response.status(404).entity(JSON.toJSONString(response)).build();
            }
            if (throwable instanceof NullPointerException) {
                response.setMsg("Not Found Error.");
                return Response.status(404).entity(JSON.toJSONString(response)).build();
            }
            if (throwable instanceof AuthException) {
                response.setMsg("Auth Failed.");
                return Response.status(431).entity(JSON.toJSONString(response)).build();
            }
            response.setMsg("Server Error.");
            return Response.status(500).entity(JSON.toJSONString(response)).build();
        }
    }
}
