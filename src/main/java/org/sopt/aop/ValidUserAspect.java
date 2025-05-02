package org.sopt.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.sopt.dto.request.user.UserCreateRequest;
import org.sopt.util.validator.UserValidator;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidUserAspect {

    @Before("execution(* * (.., @org.sopt.annotation.ValidUserCreateRequest (org.sopt.dto.request.user.UserCreateRequest), ..)) ")
    public void validUserCreateRequest(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof UserCreateRequest request) {
                UserValidator.validName(request.name());
            }
        }
    }

}
