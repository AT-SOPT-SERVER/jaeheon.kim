package org.sopt.common.advice;

import org.sopt.dto.ErrorResponseDto;
import org.sopt.dto.ResponseDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(
        basePackages = "org.sopt.controller"
)
public class ResponseDtoAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return !(returnType.getParameterType() == ResponseEntity.class)
                && MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        if (body instanceof ResponseDto<?> responseDto) {
            response.setStatusCode(HttpStatusCode.valueOf(Integer.parseInt(responseDto.getStatusCode())));
            return responseDto;
        }
        if (body instanceof ErrorResponseDto responseDto) {
            response.setStatusCode(HttpStatusCode.valueOf(Integer.parseInt(responseDto.getStatusCode())));
            return responseDto;
        }
        return ResponseDto.of(HttpStatus.OK, "Success", body);
    }
}