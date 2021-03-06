package com.syj.wenda.controller;

import com.syj.wenda.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class SettingController {
    @Autowired
    WendaService wendaService;

    @RequestMapping(value = "/setting", method = {RequestMethod.GET})
    @ResponseBody
    public String setting(HttpSession httpSession) {
        return "setting ok" + wendaService.getMessage(1);
    }
}
