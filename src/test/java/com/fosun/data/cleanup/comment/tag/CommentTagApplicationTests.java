package com.fosun.data.cleanup.comment.tag;

import com.fosun.data.cleanup.comment.tag.dto.po.mongodb.MallCommentInfoPo;
import com.fosun.data.cleanup.comment.tag.service.impl.BaiduApiServiceImpl;
import com.mongodb.client.result.UpdateResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentTagApplicationTests {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void contextLoads() {
		String id = "5d12da8d3bdeb156427b2141";
		String highlighContent = "长风景畔变成长风大悦城后，感觉比以前好了不少。店多了，种类也多了，特别适合亲子。学的，玩的，吃的，什么都有，大人小孩都不会无聊，可以呆上一整天。5楼电影院也还不错，相比多数商场都有的小小的电影院，各方面更有感觉。";
		Update update = new Update();
		update.set("commentTag", Arrays.asList("空间大"));
		update.set("highlighContent",highlighContent);
		Query query = new Query(new Criteria().and("_id").is(id));
		UpdateResult updateResult = mongoTemplate.updateMulti(query, update, MallCommentInfoPo.class);
		System.out.println(updateResult.getModifiedCount());
	}

	@Autowired
	private BaiduApiServiceImpl baiduApiService;
	@Test
	public void testGetToken(){
		String token = baiduApiService.getAccessKey("sFvE4PQ1O4IhWp0QwD9yoHt8","IFoLvAT6zmE3dut6CA5Yb3slZDElSgyS");
		System.out.println(token);

	}

}
