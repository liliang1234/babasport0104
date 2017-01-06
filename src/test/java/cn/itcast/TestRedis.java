package cn.itcast;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestRedis {

	@Test
	public void testRedis(){
		Jedis jedis = new Jedis("192.168.40.128");
		jedis.set("abad", "bushizheyang");
		Long incr = jedis.incr("pno");
		System.out.println(incr);
	}
}
