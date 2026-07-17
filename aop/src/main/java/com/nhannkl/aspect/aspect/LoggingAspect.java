package com.nhannkl.aspect.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    
    @Before("execution(* com.nhannkl.aspect.service.*.*(..))")
    public void logBefore() {
        log.info("logging before");
    }

    @After("execution(* com.nhannkl.aspect.service.*.*(..))")
    public void logAfter() {
        log.info("logging after");
    }

}
