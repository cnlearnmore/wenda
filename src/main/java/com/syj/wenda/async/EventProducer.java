package com.syj.wenda.async;

import com.alibaba.fastjson.JSONObject;
import com.syj.wenda.util.JedisAdapter;
import com.syj.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    //此类目前在LikeController、LoginController中实现了。
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
