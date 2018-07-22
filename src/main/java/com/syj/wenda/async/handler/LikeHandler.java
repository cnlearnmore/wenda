package com.syj.wenda.async.handler;

import com.syj.wenda.async.EventHandler;
import com.syj.wenda.async.EventModel;
import com.syj.wenda.async.EventType;
import com.syj.wenda.model.Message;
import com.syj.wenda.model.User;
import com.syj.wenda.service.MessageService;
import com.syj.wenda.service.UserService;
import com.syj.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的评论,http://localhost:8080/question/" + model.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
