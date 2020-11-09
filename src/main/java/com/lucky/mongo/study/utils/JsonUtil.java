package com.lucky.mongo.study.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: liangtf
 * @description: json工具
 * @date: 2020/11/5 17:33
 */
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtil() {
    }

    public static String object2Str(Object param) {
        String result = "";
        try {
            result = objectMapper.writeValueAsString(param);
        } catch (Exception e) {
            logger.error("JsonUtil.str2Object.error:", e);
        }
        return result;
    }

    public static <T> T str2Object(String str, TypeReference<T> typeReference) {
        T t = null;
        try {
            t = objectMapper.readValue(str, typeReference);
        } catch (JsonProcessingException e) {
            logger.error("JsonUtil.str2Object.error:", e);
        }
        return t;
    }

    public static <T> T str2Class(String str, Class<T> tClass) {
        T t = null;
        try {
            t = objectMapper.readValue(str, tClass);
        } catch (JsonProcessingException e) {
            logger.error("JsonUtil.str2Class.error:", e);
        }
        return t;
    }

}
