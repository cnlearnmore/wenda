package com.syj.wenda.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.syj.wenda.async.EventHandler;
import com.syj.wenda.async.EventModel;
import com.syj.wenda.async.EventType;
import com.syj.wenda.model.User;
import com.syj.wenda.util.JedisAdapter;
import com.syj.wenda.util.RedisKeyUtil;
import com.syj.wenda.model.EntityType;
import com.syj.wenda.model.Feed;
import com.syj.wenda.model.Question;
import com.syj.wenda.service.FeedService;
import com.syj.wenda.service.FollowService;
import com.syj.wenda.service.QuestionService;
import com.syj.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler {

    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    FeedService feedService;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<String, String>();
        User actor = userService.getUser(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());
        if (model.getType() == EventType.COMMENT || (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionService.selectById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }


    @Override
    public void doHandle(EventModel model) {
        Feed feed = new Feed();
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setCreatedDate(new Date());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            return;
        }
        feedService.addFeed(feed);

        //下面的是推的。
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        followers.add(0);
        for(int follower : followers){
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
