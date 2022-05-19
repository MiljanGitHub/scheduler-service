package com.uns.ac.rs.schedulerservice.repository;

import com.uns.ac.rs.schedulerservice.dto.response.UserNameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${feign-services.user-service.name}", url = "${feign-services.user-service.url}")
public interface FeignClientUserService {

    @GetMapping(value = "${feign-services.user-service.path}" + "/{userId}", consumes = "application/json")
    UserNameDto fetchUserData(@PathVariable("userId") Integer userId);
}