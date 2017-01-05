package cn.itcast.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品管理
 *
 */
@Controller
public class FrontProductController {

	//第一个Springmvc请求
	@RequestMapping("/test/index.do")
	public String index(){
		System.out.println("");
		return "";
	}
}
