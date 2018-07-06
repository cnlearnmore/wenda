package com.nowcoder.wenda;

import com.nowcoder.wenda.dao.QuestionDao;
import com.nowcoder.wenda.dao.UserDao;
import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.service.FollowService;
import com.nowcoder.wenda.service.SensitiveService;
import com.nowcoder.wenda.util.JedisAdapter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest

public class SensitiveTest {

    @Autowired
    SensitiveService sensitiveService;

    @Test
    public void testSensitive() {
        System.out.println(sensitiveService.filter("这是小明发送的测试数据"));
    }
}
