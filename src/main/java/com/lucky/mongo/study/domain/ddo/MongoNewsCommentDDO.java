package com.lucky.mongo.study.domain.ddo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/6 16:12
 */
@Data
public class MongoNewsCommentDDO implements Serializable {

    /** mongodb Id */
    private String _id;

    /** newsId */
    private String newsId;

    /** 用户id */
    private String userId;

    /** 用户昵称 */
    private String nickName;

    /** 评论 */
    private String comment;

    /** 评论时间 */
    private String commentTime;

    /** 是否显示 */
    private String display;

    /** 父级Id 默认顶级为 -1 */
    private String parentId;

    /** 是否是二级回复 */
    private String secondReply;

    /** 二级回复用户id */
    private String replyUserId;

    /** 二级回复用户昵称 */
    private String replyNickName;

    /** 是否删除： Y -> 删除 N ：未删除 */
    private String deleted;

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private String createdAt;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private String updatedAt;

    /** 备注 */
    private String remark;


}
