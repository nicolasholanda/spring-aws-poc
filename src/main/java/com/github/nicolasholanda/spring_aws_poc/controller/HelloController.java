package com.github.nicolasholanda.spring_aws_poc.controller;

import com.github.nicolasholanda.spring_aws_poc.service.AwsSecretsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private AwsSecretsManagerService secretsManagerService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, " + secretsManagerService.getSecret("spring-poc/my-secret-name");
    }
}
