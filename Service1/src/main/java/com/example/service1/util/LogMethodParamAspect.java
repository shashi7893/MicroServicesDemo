package com.example.service1.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogMethodParamAspect {
    @Around("@annotation(LogMethodParam)")
    public Object logMethodParam(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            String paramName = parameterNames[i];
            Object paramValue = args[i];
            System.out.println(paramName + " = " + paramValue);
        }
        return joinPoint.proceed();
    }
}
