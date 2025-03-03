package com.example.wms.infrastructure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class PerformanceAspect {

    @Around("com.example.wms.infrastructure.aspect.CommonPointcutConfig.controllerPackage()")
    public Object measureControllerPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        log.info("⏱️ {} ms 소요됨 - Controller {} ", stopWatch.getTotalTimeMillis(), joinPoint);
        return result;
    }

    @Around("com.example.wms.infrastructure.aspect.CommonPointcutConfig.servicePackage()")
    public Object measureServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        log.info("⏱️ {} ms 소요됨 - Service {} ", stopWatch.getTotalTimeMillis(), joinPoint);
        return result;
    }

    @Around("com.example.wms.infrastructure.aspect.CommonPointcutConfig.adapterPackage()")
    public Object measureAdapterPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        log.info("⏱️ {} ms 소요됨 - Adapter {} ", stopWatch.getTotalTimeMillis(), joinPoint);
        return result;
    }
}
