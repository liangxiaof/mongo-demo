package com.lucky.mongo.study.domain;

import com.lucky.mongo.study.annotations.FieldCommentAnnotation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/5 17:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SysUser {

    @FieldCommentAnnotation("用户Id")
    private String id;

    private String nickName;

    @FieldCommentAnnotation("删除状态")
    private String deleted;

}
