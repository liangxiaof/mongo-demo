package com.lucky.mongo.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/5 15:13
 */
@SpringBootApplication
@EnableAsync
public class MongoDbApplicationStart {

    public static void main(String[] args) {
        SpringApplication.run(MongoDbApplicationStart.class, args);
    }

}
