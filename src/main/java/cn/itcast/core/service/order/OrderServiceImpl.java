package cn.itcast.core.service.order;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.BuyerItem;
import cn.itcast.core.bean.order.Detail;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.dao.order.DetailDao;
import cn.itcast.core.dao.order.OrderDao;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private DetailDao detailDao;
	@Autowired
	private Jedis jedis;
	public void addOrder(Order order,BuyerCart buyerCart){
		Long incr = jedis.incr("oid");
		order.setId(incr);
		//运
		order.setDeliverFee(buyerCart.getFee());
		//金额
		order.setOrderPrice(buyerCart.getProductPrice());
		//应付金额
		order.setTotalFee(buyerCart.getTotalPrice());
		//支付状态
		if(order.getPaymentWay() == 0){
			order.setIsPaiy(0);
		}else{
			order.setIsPaiy(1);
		}
		//订单状态
		order.setOrderState(0);
		
		order.setCreateDate(new Date());
		
		//保存订单
		orderDao.insert(order);
		
		//订单字表
		Detail detail = null;
		List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
		for (BuyerItem buyerItem : buyerItems) {
			detail = new Detail();
			detail.setOrderId(incr);
			detail.setProductId(""+buyerItem.getSku().getProduct().getId());
			detail.setProductName(buyerItem.getSku().getProduct().getName());
			detail.setColor(buyerItem.getSku().getColor().getName());
			detail.setPrice(buyerItem.getSku().getPrice());
			detail.setSize(buyerItem.getSku().getSize());
			detail.setAmount(buyerItem.getAmoutnt());
			detailDao.insert(detail);
		}
	}
}
