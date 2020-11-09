package com.lucky.mongo.study.apirequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/6 14:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class NewsCommentRequest {

    private String newsId;

    private String parentId;

    private String userId;

    private String nickName;

    private String comment;

    private String replyUserId;

    private String replyNickName;

}
