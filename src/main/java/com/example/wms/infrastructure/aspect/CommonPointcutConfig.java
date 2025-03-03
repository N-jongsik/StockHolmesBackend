package com.example.wms.infrastructure.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcutConfig {

    @Pointcut("execution(* com.example.wms.*.adapter.in..*(..))")
    public void controllerPackage() {}

    @Pointcut("execution(* com.example.wms.*.application.service..*(..))")
    public void servicePackage() {}

    @Pointcut("execution(* com.example.wms.*.adapter.out..*(..))")
    public void adapterPackage() {}
}
