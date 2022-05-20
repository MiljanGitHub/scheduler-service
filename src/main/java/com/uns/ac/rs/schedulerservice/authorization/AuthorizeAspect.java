package com.uns.ac.rs.schedulerservice.authorization;

import com.uns.ac.rs.schedulerservice.dto.response.AuthenticationResponse;
import com.uns.ac.rs.schedulerservice.repository.FeignClientUserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@Aspect
@AllArgsConstructor
public class AuthorizeAspect {

    private FeignClientUserService feignClientUserService;

    @Around("com.uns.ac.rs.schedulerservice.authorization.AuthorizablePointcutConfig.authorizable()")
    public Object aroundAuthorizableMethod(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Authorizable annotation = method.getAnnotation(Authorizable.class);
        if (annotation.roles().length == 0) pjp.proceed();
        Object[] args = pjp.getArgs();
        HttpServletRequest request  = (HttpServletRequest) args[0];
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String jwtToken = authorization.substring(7);
        AuthenticationResponse authenticationResponse = feignClientUserService.fetchUserRole(jwtToken);

        if (authenticationResponse == null ||!authenticationResponse.getAuthenticated()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (!Arrays.asList(annotation.roles()).contains(authenticationResponse.getRole())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return pjp.proceed();

    }
}
