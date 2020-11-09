package com.lucky.mongo.study.manager;

import com.google.common.collect.Lists;
import com.lucky.mongo.study.apirequest.NewsCommentRequest;
import com.lucky.mongo.study.apiresponse.NewsChildCommentResponse;
import com.lucky.mongo.study.apiresponse.NewsCommentResponse;
import com.lucky.mongo.study.common.PageResult;
import com.lucky.mongo.study.common.Result;
import com.lucky.mongo.study.domain.NewsComment;
import com.lucky.mongo.study.domain.ddo.MongoNewsCommentDDO;
import com.lucky.mongo.study.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/6 14:18
 */
@Component
public class NewsCommentManager {

    private static final Logger logger = LoggerFactory.getLogger(NewsCommentManager.class);

    private static final String COLLECTION_NAME = "comment";

    @Autowired
    private MongoManager mongoManager;

    @Autowired
    private MongoTemplate mongoTemplate;

    public String saveComment(NewsCommentRequest commentRequest) {
        logger.info("comment request {}", commentRequest);
        NewsComment comment = convertComment(commentRequest);
        MongoNewsCommentDDO newsCommentDDO = convertDdo(comment);
        logger.info("ddo {}", newsCommentDDO);
        mongoManager.saveMongo(newsCommentDDO, COLLECTION_NAME);
        return comment.get_id();
    }


    private NewsComment convertComment(NewsCommentRequest commentRequest) {
        NewsComment comment = new NewsComment();
        Date currentTime = new Date();
        comment.set_id(UUID.randomUUID().toString().replace("-", ""))
               .setNewsId(commentRequest.getNewsId())
               .setParentId(StringUtils.isEmpty(commentRequest.getParentId()) ? "-1" : commentRequest.getParentId())
               .setUserId(commentRequest.getUserId())
               .setNickName(commentRequest.getNickName())
               .setComment(commentRequest.getComment())
               .setCommentTime(currentTime)
               .setDisplay("Y")
               .setDeleted("N")
               .setReplyUserId(commentRequest.getReplyUserId())
               .setReplyNickName(commentRequest.getReplyNickName())
               .setSecondReply("N")
               .setCreatedAt(currentTime)
               .setCreatedBy(commentRequest.getUserId())
               .setUpdatedAt(currentTime)
               .setUpdatedBy(commentRequest.getUserId());
        if (!StringUtils.isEmpty(commentRequest.getReplyUserId())
                && !StringUtils.isEmpty(commentRequest.getReplyNickName()))
            comment.setSecondReply("Y");

        return comment;
    }

    private MongoNewsCommentDDO convertDdo(NewsComment newsComment) {
        MongoNewsCommentDDO newsCommentDDO = new MongoNewsCommentDDO();
        String commentStr = JsonUtil.object2Str(newsComment);
        newsCommentDDO = JsonUtil.str2Class(commentStr, MongoNewsCommentDDO.class);
        if (StringUtils.isEmpty(newsCommentDDO.getRemark())) newsCommentDDO.setRemark("");
        if (StringUtils.isEmpty(newsCommentDDO.getReplyUserId())) newsCommentDDO.setReplyUserId("");
        if (StringUtils.isEmpty(newsCommentDDO.getReplyNickName())) newsCommentDDO.setReplyNickName("");
        return newsCommentDDO;
    }

    public void queryComment() {

        Long currentPage = 1L;

        Long pageSize = 20L;
        // 构建查询

        // and
        Criteria criteria = Criteria.where("newsId").is("").and("deleted").is("N");
        // 模糊
        criteria.and("").regex("");
        // 或
        Criteria criteriaNickName = new Criteria("").regex("");
        Criteria criteriaUserId = new Criteria("").is("");
        criteria.orOperator(criteriaNickName, criteriaUserId);

        // 单字段排序
        SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.ASC, ""));
        // 多字段排序
        SortOperation sortMulti = Aggregation.sort(Sort.by(new Sort.Order(Sort.Direction.ASC, ""),
                                                           new Sort.Order(Sort.Direction.DESC, "")));

        // 分组
        GroupOperation groupOperation = Aggregation.group("")
                                                   // 为字段起别名
                                                   .first("").as("")
                                                   // 将符合的字段值添加到一个集合中
                                                   .addToSet("").as("");

        // 分页
        long skip = (currentPage - 1) * pageSize;

        SkipOperation skipOperation = Aggregation.skip(skip);
        LimitOperation limitOperation = Aggregation.limit(pageSize);

        // 别名
        ProjectionOperation projectionOperation = Aggregation.project("要返回的字段").and("原始字段").as("别名");
        // 查询所有 起别名
        Aggregation.count().as("");

        // 上面所有条件整合
        Aggregation aggregation = Aggregation.newAggregation(projectionOperation,
                                                             Aggregation.match(criteria),
                                                             groupOperation,
                                                             sort,
                                                             skipOperation,
                                                             limitOperation);

        // 查询
        AggregationResults<MongoNewsCommentDDO> aggregateResult = mongoTemplate.aggregate(aggregation, "", MongoNewsCommentDDO.class);

        List<MongoNewsCommentDDO> mappedResults = aggregateResult.getMappedResults();

    }


    public PageResult<List<NewsCommentResponse>> getComment(String newsId) {

        Integer currentPage = 1;

        Integer pageSize = 20;

        long skip = (currentPage - 1) * pageSize;

        PageResult<List<NewsCommentResponse>> result = new PageResult<>();
        result.setCode("00000");
        result.setMessage("success");

        // 查询条件
        Criteria criteria = Criteria.where("newsId").is(newsId).and("deleted").is("N").and("parentId").is("-1");

        // 数据
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                                                             Aggregation.sort(Sort.by(Sort.Direction.ASC, "createdAt")),
                                                             Aggregation.skip(skip),
                                                             Aggregation.limit(pageSize));
        AggregationResults<MongoNewsCommentDDO> aggregate = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, MongoNewsCommentDDO.class);
        List<MongoNewsCommentDDO> mappedResults = aggregate.getMappedResults();
        if (mappedResults.isEmpty()) return result;
        List<NewsCommentResponse> responses = Lists.newLinkedList();
        for (MongoNewsCommentDDO commentDDO : mappedResults) {
            NewsCommentResponse newsCommentResponse = convertCommentResponse(commentDDO);
            List<MongoNewsCommentDDO> childrenComment = getChildrenComment(commentDDO.get_id());
            newsCommentResponse.setChildren(convertChildrenComment(childrenComment));
            responses.add(newsCommentResponse);
        }
        result.setData(responses);
        // 总数
        Aggregation countAggregation = Aggregation.newAggregation(Aggregation.match(criteria), Aggregation.count().as("count"));
        AggregationResults<String> aggregateCount = mongoTemplate.aggregate(countAggregation, COLLECTION_NAME, String.class);
        List<String> stringList = aggregateCount.getMappedResults();
        Integer count = 0;
        Map map = JsonUtil.str2Class(stringList.get(0), Map.class);
        count = (Integer) map.get("count");

        // 总页数
        Integer totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        result.setTotalPage(totalPage);

        return result;
    }


    private NewsCommentResponse convertCommentResponse(MongoNewsCommentDDO commentDDO) {
        NewsCommentResponse newsCommentResponse = new NewsCommentResponse();
        BeanUtils.copyProperties(commentDDO, newsCommentResponse);
        newsCommentResponse.setId(commentDDO.get_id());
        return newsCommentResponse;
    }


    private List<MongoNewsCommentDDO> getChildrenComment(String parentId) {
        List<MongoNewsCommentDDO> commentDDOS = Lists.newLinkedList();
        Criteria criteria = Criteria.where("parentId").is(parentId).and("deleted").is("N");
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                                                             Aggregation.sort(Sort.by(Sort.Direction.ASC, "createdAt")));
        AggregationResults<MongoNewsCommentDDO> aggregate = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, MongoNewsCommentDDO.class);
        List<MongoNewsCommentDDO> mappedResults = aggregate.getMappedResults();
        if (CollectionUtils.isEmpty(mappedResults)) return commentDDOS;
        return mappedResults;
    }

    private List<NewsChildCommentResponse> convertChildrenComment(List<MongoNewsCommentDDO> childrenComment) {
        List<NewsChildCommentResponse> children = Lists.newLinkedList();
        if (CollectionUtils.isEmpty(childrenComment)) return children;
        for (MongoNewsCommentDDO commentDDO : childrenComment) {
            NewsChildCommentResponse newsChildCommentResponse = new NewsChildCommentResponse();
            BeanUtils.copyProperties(commentDDO, newsChildCommentResponse);
            newsChildCommentResponse.setId(commentDDO.get_id());
            children.add(newsChildCommentResponse);
        }
        return children;
    }

}
