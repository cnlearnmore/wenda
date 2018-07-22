package com.syj.wenda.controller;

import com.syj.wenda.model.EntityType;
import com.syj.wenda.model.Feed;
import com.syj.wenda.model.HostHolder;
import com.syj.wenda.service.FeedService;
import com.syj.wenda.service.FollowService;
import com.syj.wenda.util.JedisAdapter;
import com.syj.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    @Autowired
    FeedService feedService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET})
    public String getPullFeeds(Model model){
//        拉的方式
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        List<Integer> followees = new ArrayList<>();
        if(localUserId != 0){
            System.out.println("the localuserId is : " + localUserId);
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER,Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET})
    public String getPushFeeds(Model model){
//        推的方式
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0 , 10);
        List<Feed> feeds = new ArrayList<Feed>();
        for(String feedId : feedIds){
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if(feed != null){
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
