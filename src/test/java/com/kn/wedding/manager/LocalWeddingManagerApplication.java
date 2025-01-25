package com.kn.wedding.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class LocalWeddingManagerApplication {

    public static void main(String[] args) {
        SpringApplication.from(WeddingManagerApplication::main)
                .with(LocalWeddingManagerApplication.class)
                .run(args);
    }
}
