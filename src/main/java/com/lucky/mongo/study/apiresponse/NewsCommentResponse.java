package com.lucky.mongo.study.apiresponse;

import lombok.Data;

import java.util.List;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/6 16:51
 */
@Data
public class NewsCommentResponse {

    private String id;

    private String userId;

    private String nickName;

    private String createdAt;

    private String parentId;

    private String comment;

    private List<NewsChildCommentResponse> children;

}
