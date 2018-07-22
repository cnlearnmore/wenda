package com.syj.wenda.async.handler;

import com.syj.wenda.async.EventHandler;
import com.syj.wenda.async.EventModel;
import com.syj.wenda.async.EventType;
import com.syj.wenda.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
//    xxx判断发现这个用户登录异常
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登录IP异常", "mails/login_exception.ftl", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
