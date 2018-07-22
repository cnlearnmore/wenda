package com.syj.wenda;

import com.syj.wenda.service.SensitiveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
