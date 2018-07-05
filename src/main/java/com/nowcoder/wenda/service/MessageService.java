package com.nowcoder.wenda.service;

import com.nowcoder.wenda.dao.MessageDao;
import com.nowcoder.wenda.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;
    @Autowired
    SensitiveService sensitiveService;
    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message) > 0 ? message.getId() : 0;
    }
    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDao.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        List<Message> messageList = messageDao.getConversationList(userId, offset, limit);
        return messageList;
    }
    public int getConversationUnreadCount(int userId, String conversationId){
        return messageDao.getConversationUnreadCount(userId, conversationId);
    }

    public int updateMessageStatus(@Param("toId") int toId, @Param("conversationId") String conversationId){
        return messageDao.updateMessageStatus(toId, conversationId);
    }
}
