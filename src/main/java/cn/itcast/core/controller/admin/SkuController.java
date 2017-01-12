package cn.itcast.core.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.product.SkuService;

/**
 * 库存的管理
 * @author liliang
 *
 */
@Controller
public class SkuController {

	@Autowired
	private SkuService skuService;
	@RequestMapping(value="/sku/list.do")
	public String list(Long productId,Model model){
		List<Sku> skus = skuService.selectSkuListByProductId(productId);
		model.addAttribute("skus", skus);
		return "/sku/list";
	}
	@RequestMapping(value="/sku/add.do")
	public void add(Sku sku,HttpServletResponse response){
		skuService.update(sku);
		JSONObject jo = new JSONObject();
		jo.put("message", "保存成功!");
		response.setContentType("application/json;charset=UTF-8");
		try {
			response.getWriter().write(jo.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
