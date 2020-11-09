package com.lucky.mongo.study.common;

import lombok.Data;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/9 14:53
 */
@Data
public class PageResult<T> extends Result<T> {

    private Integer totalPage;

}
