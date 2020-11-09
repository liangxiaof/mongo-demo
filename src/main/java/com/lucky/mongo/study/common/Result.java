package com.lucky.mongo.study.common;

import lombok.Data;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/6 16:59
 */
@Data
public class Result<T> {

    private String code;

    private String message;

    private T data;

}
