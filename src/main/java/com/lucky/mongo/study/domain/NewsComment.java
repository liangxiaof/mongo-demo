package com.lucky.mongo.study.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/6 13:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class NewsComment {

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
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentTime;

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
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 备注 */
    private String remark;





}
