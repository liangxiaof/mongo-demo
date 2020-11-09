package com.lucky.mongo.study.controller;

import com.lucky.mongo.study.apirequest.NewsCommentRequest;
import com.lucky.mongo.study.apiresponse.NewsCommentResponse;
import com.lucky.mongo.study.common.PageResult;
import com.lucky.mongo.study.common.Result;
import com.lucky.mongo.study.manager.NewsCommentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/6 14:24
 */
@RestController
public class NewsCommentController {

    @Autowired
    private NewsCommentManager newsCommentManager;

    @RequestMapping(value = "/news/comment/save", method = RequestMethod.POST)
    public String saveComment(@RequestBody NewsCommentRequest commentRequest) {
        return newsCommentManager.saveComment(commentRequest);
    }

    @RequestMapping(value = "/news/comment/list", method = RequestMethod.GET)
    public PageResult<List<NewsCommentResponse>> getComment(String newsId) {
        return newsCommentManager.getComment(newsId);
    }

}
