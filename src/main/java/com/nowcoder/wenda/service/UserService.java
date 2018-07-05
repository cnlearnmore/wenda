package com.nowcoder.wenda.service;

import com.nowcoder.wenda.dao.LoginTicketDao;
import com.nowcoder.wenda.dao.UserDao;
import com.nowcoder.wenda.model.LoginTicket;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {

    @Autowired
    UserDao userDao;
    public User getUser(int id){
//        System.out.println(userDao.selectById(id).getName());
        return userDao.selectById(id);
    }

    @Autowired
    LoginTicketDao loginTicketDao;

    public Map<String,String> register(String username,String password){
        Map<String, String> map = new HashMap<String, String>();
        if(StringUtils.isEmpty(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDao.selectByName(username);
        if(user != null){
            map.put("msg", "用户名已经存在");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDao.addUser(user);
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String,Object> login(String username,String password){
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isEmpty(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDao.selectByName(username);
        if(user == null){
            map.put("msg", "用户名不存在");
            return map;
        }
        if(!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg", "密码错误");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        map.put("userId", user.getId());
        return map;
    }

    public User selectByName(String name){
        return userDao.selectByName(name);
    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(now.getTime() + 3600*24*100);
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDao.updateStatus(ticket,1);
    }
}
