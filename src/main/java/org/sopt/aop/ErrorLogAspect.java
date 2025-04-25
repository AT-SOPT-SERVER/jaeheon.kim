package org.sopt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopt.exception.CustomException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErrorLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(ErrorLogAspect.class);

    @Around("execution(org.springframework.http.ResponseEntity org.sopt.exception.handler.ExceptionController.* (..))")
    public Object writeErrorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof CustomException e) {
                writeLog(e);
            } else if (arg instanceof Exception e) {
                writeLog(e);
            }
        }
        return joinPoint.proceed();
    }

    private void writeLog(CustomException e) {
        logger.error("[{}] {}: {}",
                e.getClass().getSimpleName(),
                e.getErrorCode().getMessage(),
                e.getDetail());
    }

    private void writeLog(Exception e) {
        logger.error("[{}]: {}",
                e.getClass().getSimpleName(),
                e.getMessage());
    }
}
