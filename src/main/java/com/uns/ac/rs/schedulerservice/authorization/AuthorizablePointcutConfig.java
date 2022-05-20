package com.uns.ac.rs.schedulerservice.authorization;

import org.aspectj.lang.annotation.Pointcut;

public class AuthorizablePointcutConfig {

    @Pointcut("@annotation(com.uns.ac.rs.schedulerservice.authorization.Authorizable)")
    public void authorizable() {}
}
