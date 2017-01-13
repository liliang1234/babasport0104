package cn.itcast.core.service.user;

import cn.itcast.core.bean.user.Addr;

public interface AddrService {

	public Addr selectAddrByUserNmae(String userName);
}
