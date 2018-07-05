package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.model.Message;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.model.ViewObject;
import com.nowcoder.wenda.service.MessageService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.util.WendaUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
        try{
            if(hostHolder.getUser() == null){
                return WendaUtil.getJSONString(999,"未登录");
            }
            User user = userService.selectByName(toName);
            if(user == null){
                return WendaUtil.getJSONString(1, "用户不存在");
            }
            Message message = new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("发送消息失败", e.getMessage());
            return WendaUtil.getJSONString(1, "发信失败");
        }
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @Param("conversationId") String conversationId){

        try {
            List<Message> messagesList = messageService.getConversationDetail(conversationId, 0, 10);
//            logger.info("消息的条数是"+messagesList.size());
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for(Message message : messagesList){
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            messageService.updateMessageStatus(hostHolder.getUser().getId(),conversationId);

        } catch (Exception e) {
            logger.error("获取详情失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String getConversationList(Model model){
        if(hostHolder == null){
            return "redirect:/reglogin";
        }
        int localUserId = hostHolder.getUser().getId();
        List<Message> messagesList = messageService.getConversationList(localUserId, 0, 10);
        List<ViewObject> conversations = new ArrayList<ViewObject>();
        for(Message message : messagesList){
            ViewObject vo = new ViewObject();
            vo.set("message", message);
            int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
            vo.set("user", userService.getUser(targetId));
            vo.set("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations", conversations);

        return "letter";
    }
}
