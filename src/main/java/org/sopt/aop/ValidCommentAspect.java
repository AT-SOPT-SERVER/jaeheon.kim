package org.sopt.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.sopt.common.util.validator.CommentValidator;
import org.sopt.dto.request.comment.CommentCreateRequest;
import org.sopt.dto.request.comment.CommentUpdateRequest;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidCommentAspect {

	@Before("execution(* * (.., @org.sopt.annotation.ValidCommentCreateRequest (org.sopt.dto.request.comment.CommentCreateRequest), ..))")
	public void validCommentCreateRequest(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();

		for (Object arg : args) {
			if (arg instanceof CommentCreateRequest request) {
				CommentValidator.validContent(request.content());
			}
		}
	}

	@Before("execution(* * (.., @org.sopt.annotation.ValidCommentUpdateRequest (org.sopt.dto.request.comment.CommentUpdateRequest), ..))")
	public void validCommentUpdateRequest(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();

		for (Object arg : args) {
			if (arg instanceof CommentUpdateRequest request) {
				if (request.content().isPresent()) {
					CommentValidator.validContent(request.content().get());
				}
			}
		}
	}
}
