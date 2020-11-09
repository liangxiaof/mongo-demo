package com.lucky.mongo.study.apiresponse;

import lombok.Data;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/6 16:52
 */
@Data
public class NewsChildCommentResponse {

    private String id;

    private String userId;

    private String nickName;

    private String createdAt;

    private String secondReply;

    private String replyUserId;

    private String replyNickName;

    private String comment;

}
