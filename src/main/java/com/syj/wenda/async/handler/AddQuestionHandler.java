package com.syj.wenda.async.handler;

import com.syj.wenda.async.EventHandler;
import com.syj.wenda.async.EventModel;
import com.syj.wenda.async.EventType;
import com.syj.wenda.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AddQuestionHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);

    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model) {
        try {
            searchService.indexQuestion(model.getEntityId(), model.getExt("title"), model.getExt("content"));
        } catch (Exception e) {
            logger.info("增加题目索引失败");
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
