package com.personal.springboot.gataway;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.personal.springboot.gataway.dao.entity.RedisFastDto;

@ContextConfiguration(locations = { "classpath:test/spring-context-redis_test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class redisServiceTest {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	public void testRedisTemplate() {
		//Set<String> sets = redisTemplate.keys("*");
		
	
//		Set sets = redisTemplate.opsForHash().keys(ApiSubInterface.class.getName());
		Set sets = redisTemplate.opsForHash().keys(RedisFastDto.class.getName());
		//redisTemplate.delete(sets);
		System.out.println(sets);
		

	}

	
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
