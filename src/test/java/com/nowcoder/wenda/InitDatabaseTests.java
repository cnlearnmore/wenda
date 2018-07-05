package com.nowcoder.wenda;

import com.nowcoder.wenda.dao.QuestionDao;
import com.nowcoder.wenda.dao.UserDao;
import com.nowcoder.wenda.model.EntityType;
import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.service.FollowService;
import com.nowcoder.wenda.util.JedisAdapter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {

	@Autowired
	UserDao userDao;
	@Autowired
	QuestionDao questionDao;
	@Autowired
	JedisAdapter jedisAdapter;
	@Autowired
	FollowService followService;

	@Test
	public void initDatabase() {
		Random random = new Random();
		for(int i = 0; i < 11; i++){
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i));
			user.setPassword("");
			user.setSalt("");
			userDao.addUser(user);

//			用于添加关注样例数据
//			for(int j = 1; j < i; ++j){
//				followService.follow(j, EntityType.ENTITY_USER, i);
//			}

			user.setPassword("xx");
			userDao.updatePassword(user);


			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
			question.setCreatedDate(date);
			question.setUserId(i + 1);
			question.setTitle(String.format("TITLE{%d}", i));
			question.setContent(String.format("Balaababalalalal Content %d", i));
			questionDao.addQuestion(question);
		}
		Assert.assertEquals("xx",userDao.selectById(1).getPassword());
		userDao.deleteById(1);
		Assert.assertNull(userDao.selectById(1));

		System.out.println(questionDao.selectLatestQuestions(0,0,10));

	}

}
