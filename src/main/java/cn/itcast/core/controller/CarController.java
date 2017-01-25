package cn.itcast.core.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.ognl.ObjectMethodAccessor;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.Constants;
import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.BuyerItem;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.user.Addr;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.dao.product.SkuDao;
import cn.itcast.core.service.order.OrderService;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.service.user.AddrService;

/**
 * 购物车
 * 
 * @author liliang
 * 
 */
@Controller
public class CarController {

	@Autowired
	private SkuService skuService;
	@Autowired
	private AddrService addrService;
	@Autowired
	private OrderService orderService;

	/**
	 * 新增或者追加购物车
	 * 
	 * @param skuId
	 * @param amount
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shopping/buyerCart.shtml")
	public String buyerCart(Long skuId, Integer amount, Model model,
			HttpServletResponse response, HttpServletRequest request) {
		BuyerCart buyerCart = null;
		ObjectMapper om = new ObjectMapper();
		// 不可空
		om.setSerializationInclusion(Inclusion.NON_NULL);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (Constants.BUYER_CART.equals(cookie.getName())) {
					try {
						buyerCart = om.readValue(cookie.getValue(),
								BuyerCart.class);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		// cookies没有购物车
		if (buyerCart == null) {
			buyerCart = new BuyerCart();
		}

		if (skuId != null) {
			Sku sku = new Sku();
			sku.setId(skuId);
			BuyerItem buyerItem = new BuyerItem();
			buyerItem.setSku(sku);
			buyerItem.setAmoutnt(amount);
			buyerCart.addItem(buyerItem);
			StringWriter w = new StringWriter();
			try {
				om.writeValue(w, buyerCart);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Cookie cookie = new Cookie(Constants.BUYER_CART, w.toString());
			cookie.setPath("/");
			cookie.setMaxAge(60 * 60 * 24);
			response.addCookie(cookie);
		}

		// 最后加载数据
		if (buyerCart.getBuyerItems().size() > 0) {
			for (BuyerItem item : buyerCart.getBuyerItems()) {
				Sku sku = skuService.selectSkuById(item.getSku().getId());
				item.setSku(sku);
			}
		}

		model.addAttribute("buyerCart", buyerCart);

		return "product/cart";
	}

	@RequestMapping(value = "/shopping/clearCart.shtml")
	public String clearCart(HttpServletResponse response) {
		Cookie cookie = new Cookie(Constants.BUYER_CART, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return "redirect:/shopping/buyerCart.shtml";
	}

	@RequestMapping(value = "/shooping/delProduct.shtml")
	public String delProduct(Long skuId, HttpServletResponse response,
			HttpServletRequest request) {
		BuyerCart buyerCart = null;
		ObjectMapper om = new ObjectMapper();
		// 不可空
		om.setSerializationInclusion(Inclusion.NON_NULL);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (Constants.BUYER_CART.equals(cookie.getName())) {
					try {
						buyerCart = om.readValue(cookie.getValue(),
								BuyerCart.class);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		buyerCart.deleteItem(skuId);
		StringWriter w = new StringWriter();
		try {
			om.writeValue(w, buyerCart);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Cookie cookie = new Cookie(Constants.BUYER_CART, w.toString());
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24);
		response.addCookie(cookie);
		return "redirect:/shopping/buyerCart.shtml";
	}

	/**
	 * 结算
	 */
	@RequestMapping(value = "/buyer/trueBuy.shtml")
	public String trueBuy(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		BuyerCart buyerCart = null;
		ObjectMapper om = new ObjectMapper();
		// 不可空
		om.setSerializationInclusion(Inclusion.NON_NULL);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (Constants.BUYER_CART.equals(cookie.getName())) {
					try {
						buyerCart = om.readValue(cookie.getValue(),
								BuyerCart.class);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		if (buyerCart != null) {
			// 判断库存
			List<BuyerItem> items = buyerCart.getBuyerItems();
			Boolean flag = false;
			if (items.size() > 0) {
				for (BuyerItem buyerItem : items) {
					Sku sku = skuService.selectById(buyerItem.getSku().getId());
					if (sku.getStock() < buyerItem.getAmoutnt()) {
						buyerItem.setIsStock(false);
						flag = true;
					}

				}
			}
			if (flag) {
				StringWriter w = new StringWriter();
				try {
					om.writeValue(w, buyerCart);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Cookie cookie = new Cookie(Constants.BUYER_CART, w.toString());
				cookie.setPath("/");
				cookie.setMaxAge(60 * 60 * 24);
				response.addCookie(cookie);
				return "redirect:/shopping/buyerCart.shtml";
			}
		} else {
			return "redirect:/shopping/buyerCart.shtml";
		}
		Buyer buyer = (Buyer) request.getSession().getAttribute(
				Constants.BUYER_NAME);
		Addr addr = addrService.selectAddrByUserNmae(buyer.getUsername());
		model.addAttribute("addr", addr);

		return "product/productOrder";
	}

	@RequestMapping(value = "/buyer/confirmOrder.shtml")
	public String confirmOrder(Model model, Order order,
			HttpServletRequest request) {
		// 订单保存
		// 订单主表
		// 订单字表

		// 清空购物车
		BuyerCart buyerCart = null;
		ObjectMapper om = new ObjectMapper();
		// 不可空
		om.setSerializationInclusion(Inclusion.NON_NULL);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (Constants.BUYER_CART.equals(cookie.getName())) {
					try {
						buyerCart = om.readValue(cookie.getValue(),
								BuyerCart.class);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		// 最后加载数据
		if (buyerCart.getBuyerItems().size() > 0) {
			for (BuyerItem item : buyerCart.getBuyerItems()) {
				Sku sku = skuService.selectSkuById(item.getSku().getId());
				item.setSku(sku);
			}
		}
		Buyer buyer = (Buyer) request.getSession().getAttribute(
				Constants.BUYER_NAME);
		order.setBuyerId(buyer.getUsername());
		orderService.addOrder(order, buyerCart);

		return "product/confirmOrder";

	}
}
