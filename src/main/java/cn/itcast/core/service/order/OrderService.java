package cn.itcast.core.service.order;

import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.order.Order;

public interface OrderService {

	public void addOrder(Order order,BuyerCart buyerCart);
}
