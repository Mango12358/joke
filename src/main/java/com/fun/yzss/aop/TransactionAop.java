package com.fun.yzss.aop;

import com.fun.yzss.db.engine.InTransaction;
import com.fun.yzss.db.engine.TransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * Created by fanqq on 2016/10/11.
 */
@Aspect
@Component("transactionAop")
public class TransactionAop implements Ordered {

    @Resource
    private TransactionManager transactionManager;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.fun.yzss.service.*Service.*(..))")
    public Object transaction(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        InTransaction inTransaction = method.getAnnotation(InTransaction.class);
        if (inTransaction != null) {
            String objectName = point.getSignature().getDeclaringTypeName();
            String methodName = point.getSignature().getName();
            boolean isInTransaction = transactionManager.isInTransaction();
            if (isInTransaction) {
                logger.info("In transaction,  [{}.{}] is called.", objectName, methodName);
            } else {
                logger.info("Start transaction. [{}.{}] is called.", objectName, methodName);
                transactionManager.startTransaction();
            }
            try {
                Object result = point.proceed();
                if (!isInTransaction) {
                    logger.info("Commit transaction. [{}.{}] is called.", objectName, methodName);
                    transactionManager.commitTransaction();
                }
                return result;
            } catch (Throwable throwable) {
                if (!isInTransaction) {
                    logger.warn(String.format("Rollback transaction. [%s.%s] is called.", objectName, methodName));
                    transactionManager.rollbackTransaction();
                }
                throw throwable;
            }
        }else {
            return point.proceed();
        }
    }

    @Override
    public int getOrder() {
        return AopOrder.TransactionAop;
    }

}
