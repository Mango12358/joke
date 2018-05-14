package com.fun.yzss.aop;

import com.fun.yzss.util.DesEncrypt;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;

/**
 * Created by fanqq on 2016/7/14.
 */
@Aspect
@Component("desAop")
public class DesAop implements Ordered {
    @Resource
    DesEncrypt desEncrypt;
    DynamicBooleanProperty desaopEnable = DynamicPropertyFactory.getInstance().getBooleanProperty("desaop.enalbe", false);


    @Around("execution(* com.fun.yzss.resources.*Resource.*(..))")
    public Object desPostBody(ProceedingJoinPoint point) throws Throwable {
        if (!desaopEnable.get()){
            return point.proceed();
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        POST post = method.getAnnotation(POST.class);
        if (post != null) {
            Object[] args = point.getArgs();
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof String) {
                    String body = (String) args[i];
                    String desBody = desEncrypt.decrypt(body);
                    args[i] = desBody;
                    if (desBody == null) {
                        return Response.status(400).entity("Bad Request! Data is illegal.").build();
                    }
                    return point.proceed(args);
                }
            }
        }
        return point.proceed();
    }

    @Override
    public int getOrder() {
        return AopOrder.DesAop;
    }
}
