package cn.itcast.core.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.user.Addr;
import cn.itcast.core.bean.user.AddrQuery;
import cn.itcast.core.dao.user.AddrDao;

@Service
@Transactional
public class AddrServiceImpl implements AddrService {

	@Autowired
	private AddrDao addrDao; 
	public Addr selectAddrByUserNmae(String userName){
		AddrQuery example = new AddrQuery();
		example.createCriteria().andBuyerIdEqualTo(userName).andIsDefEqualTo(true);
		List<Addr> addrs = addrDao.selectByExample(example);
		return addrs.get(0);
	}
}
