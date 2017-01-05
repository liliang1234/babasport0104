package cn.itcast.core.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理页面跳转
 * @author liliang
 *
 */
@Controller
@RequestMapping(value="/control")
public class CenterController {

	@RequestMapping(value="/index.do")
	public String index(){
		return "index";
	}
	@RequestMapping(value="/top.do")
	public String top(){ 
		return "top";
	}
	@RequestMapping(value="/main.do")
	public String main(){
		return "main";
	}
	@RequestMapping(value="/left.do")
	public String left(){
		return "left";
	}
	@RequestMapping(value="/right.do")
	public String right(){
		return "right";
	}
	@RequestMapping(value="/frame/product_main.do")
	public String product_main(){
		return "/frame/product_main";
	}
	@RequestMapping(value="/frame/product_left.do")
	public String product_left(){
		return "/frame/product_left";
	}
	@RequestMapping(value="/frame/order_main.do")
	public String order_main(){
		return "/frame/order_main";
	}
	@RequestMapping(value="/frame/order_left.do")
	public String order_left(){
		return "/frame/order_left";
	}
}
