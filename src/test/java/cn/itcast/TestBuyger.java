package cn.itcast;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.ColorQuery;
import cn.itcast.core.dao.product.ColorDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-context.xml"})
public class TestBuyger {

	@Autowired
	ColorDao colorDao;
	@Test
	public void testBuyger(){
		/*Color c = colorDao.selectByPrimaryKey(1L);
		System.out.println(c);*/
		ColorQuery example = new ColorQuery();
		example.createCriteria().andParentIdEqualTo(0L);
		example.setOrderByClause("id desc");
		example.setFields("id,name");
		List<Color> colors = colorDao.selectByExample(example);
		for (Color color : colors) {
			System.out.println(color);
		}
	}
	
}
