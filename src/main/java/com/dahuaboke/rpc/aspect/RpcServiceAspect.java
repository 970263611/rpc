package com.dahuaboke.rpc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RpcServiceAspect {

    @Pointcut("within(@com.dahuaboke.rpc.annotation.RpcService *)")
    public void annotationPoint() {};

    @Around("annotationPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object[] args = joinPoint.getArgs();
            return joinPoint.proceed(args);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
