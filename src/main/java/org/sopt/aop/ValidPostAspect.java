package org.sopt.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.sopt.dto.request.post.PostCreateRequest;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.validator.PostValidator;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidPostAspect {

    /**
     * @param joinPoint
     * @Before("args(org.sopt.dto.request.post.PostRequest)") 은 파라미터가 PostRequest인 경우 계속 적용되므로 비효율적
     * arg로 특정 파라미터만 처리하려고 할 경우 controller 메서드의 파라미터 순서에 민감할 수 있으므로 for 문을 순회하도록 함
     * 현재는 메서드의 요청 타입, 반환타입에 제한이 없고, 파라미터의 앞, 뒤 요소들의 개수에도 제한이 없으며, @ValidPost 가 붙은 PostRequest 가 존재하는 경우를 타겟팅으로 함.
     * 그러나 이런 형태는 확장에 한계가 있음 추후 다른 도메인들이 추가된다면 매번 이렇게 확인하는 부분을 만들 수는 없음. 추후 수정 필요. 직접 구현하려고 고민해보니 validation 라이브러리의 소중함이 더 커짐
     */
    @Before("execution(* *(.., @org.sopt.annotation.ValidPostCreateRequest (org.sopt.dto.request.post.PostCreateRequest), ..))")
    public void validPostRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof PostCreateRequest request) {
                PostValidator.validTitle(request.title());
            }
        }
    }

    @Before("execution(* *(.., @org.sopt.annotation.ValidPostUpdateRequest (org.sopt.dto.request.post.PostUpdateRequest), ..))")
    public void validPostUpdateRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof PostUpdateRequest request
                    && request.title().isPresent()) {
                PostValidator.validTitle(request.title().get());
            }
        }
    }
}
