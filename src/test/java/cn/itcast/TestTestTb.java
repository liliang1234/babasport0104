package cn.itcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.core.bean.TestTb;
import cn.itcast.core.dao.TestDao;
import cn.itcast.core.service.TestTbService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-context.xml"})
public class TestTestTb {

	@Autowired TestTbService service;
	@Test
	public void testName() throws Exception{
		TestTb tb = new TestTb();
		tb.setName("呵呵qq44qqq");
		
		 service.add(tb);
		
	}
}
