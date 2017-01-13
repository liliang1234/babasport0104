package cn.itcast.core.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.dao.user.BuyerDao;

@Service
@Transactional
public class BuyerServiceImpl implements BuyerService {

	@Autowired
	private BuyerDao buyerDao;
	public Buyer selectBuyerById(String name){
		
		return buyerDao.selectByPrimaryKey(name);
	}
}
