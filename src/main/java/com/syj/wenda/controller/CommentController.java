package com.syj.wenda.controller;

import com.syj.wenda.async.EventModel;
import com.syj.wenda.async.EventProducer;
import com.syj.wenda.async.EventType;
import com.syj.wenda.model.Comment;
import com.syj.wenda.model.EntityType;
import com.syj.wenda.model.HostHolder;
import com.syj.wenda.service.CommentService;
import com.syj.wenda.service.QuestionService;
import com.syj.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());

            } else {
                return "redirect:/reglogin?next=/question/" + questionId;
//                为了功能的完善，暂时注销
                //comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            if(content == null || content.length() == 0){
                return "redirect:/question/" + questionId;
            }
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            commentService.addComment(comment);
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);
            //添加消息,此处对应的handler还没写，写完关注部分再写这里
//            eventProducer.fireEvent(new EventModel().setActorId(comment.getUserId()).setEntityType(EntityType.ENTITY_COMMENT).setEntityId(questionId));
            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId()).setEntityType(EntityType.ENTITY_COMMENT).setEntityId(questionId).setEntityOwnerId(questionService.selectById(questionId).getUserId()));
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/question/" + questionId;
    }
}
