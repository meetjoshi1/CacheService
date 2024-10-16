package com.cache;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.cache")
@EnableSwagger2
public class CachingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CachingServiceApplication.class, args);
    }
}
