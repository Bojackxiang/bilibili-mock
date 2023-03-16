package org.example;

// springboot 启动方法

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BilibiliApp {
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(BilibiliApp.class, args);
    }

}
