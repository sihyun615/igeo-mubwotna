package com.sparta.igeomubwotna.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "LoggingAop")
@Aspect  //클래스쪽에 달아야하며 Bean클래스에만 적용가능
@Component
public class LoggingAop {

	@Before("execution(* com.sparta.igeomubwotna.controller.*.*(..))")
	public void printLog(JoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		log.info("Request URL: {}", request.getRequestURL());
		log.info("HTTP Method: {}", request.getMethod());
		log.info("Class Method: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
	}
}
