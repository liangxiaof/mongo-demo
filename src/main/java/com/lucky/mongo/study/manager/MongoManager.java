package com.lucky.mongo.study.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/5 15:27
 */
@Component
public class MongoManager {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Async
    public void saveMongo(Object objectToSave, String collectionName) {

        mongoTemplate.save(objectToSave, collectionName);
    }

}
