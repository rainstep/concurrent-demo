package com.example.concurrentdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.concurrentdemo.dao")
public class ConcurrentDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcurrentDemoApplication.class, args);
    }

}
