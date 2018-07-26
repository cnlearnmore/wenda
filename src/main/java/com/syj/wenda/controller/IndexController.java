package com.syj.wenda.controller;

import com.syj.wenda.service.WendaService;
import com.syj.wenda.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.*;

//这个类做测试用
//@Controller
public class IndexController {
    @Autowired
    WendaService wendaService;
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(path = {"/loggerTest"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession) {

        logger.info("VISIT HOME" + new Date());
        return wendaService.getMessage(2) + "Hello NowCoder " + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1", required = true) int type,
                          @RequestParam(value = "key", defaultValue = "zz", required = false) String key
    ) {
        return String.format("profile value of %s %d,t:%d,k:%s", groupId, userId, type, key);
    }

    @RequestMapping(path = {"/vm"}, method = {RequestMethod.GET})
    public String template(Model model) {
        model.addAttribute("value1", "vvvvv1");
        List<String> colors = Arrays.asList(new String[]{"red", "green", "blue"});
        model.addAttribute("colors", colors);
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
            model.addAttribute("map", map);
        }
        model.addAttribute("user", new User("xiaoming"));

        return "home";
    }


    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    //@ResponseBody
    public RedirectView redirect(@PathVariable("code") int code, HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect");
        RedirectView red = new RedirectView("/", true);
        if (code == 301)
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return red;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("参数错误");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }
}
