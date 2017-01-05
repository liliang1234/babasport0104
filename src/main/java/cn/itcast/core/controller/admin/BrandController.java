package cn.itcast.core.controller.admin;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.service.product.BrandService;

/**
 * 品牌的控制层
 * @author liliang
 *
 */
@Controller
public class BrandController {

	@Autowired
	BrandService brandService;
	@RequestMapping(value="/brand/list.do")
	public String list(Integer pageNo , String name,Integer isDisPlay,Model model){
		BrandQuery bq = new BrandQuery();
		//参数的容器
		StringBuilder params = new StringBuilder();
		if(name != null ){
			bq.setName(name);
			params.append("name=").append(name);
		}
		if(isDisPlay!=null){
			bq.setIsDisplay(isDisPlay);
			params.append("&").append("isDisPlay=").append(isDisPlay);
		}
		
		//null 或者小于1  设置为1
		bq.setPageNo(Pagination.cpn(pageNo));
		//每页显示3条
		bq.setPageSize(3);
		Pagination brandPage = brandService.selectBrandPages(bq);
		//分页的显示
		String url = "/brand/list.do";
		brandPage.pageView(url, params.toString());
		model.addAttribute("brandPage", brandPage);
		model.addAttribute("name", name);
		model.addAttribute("isDisPlay", isDisPlay);
		return "brand/list";
	}
	@RequestMapping(value="/brand/deletes.do")
	public String deletes(Integer[] ids,Model model){
		brandService.deletes(ids);
		return "redirect:/brand/list.do";
	}
	//去添加的方法
	@RequestMapping(value="/brand/toAdd.do")
	public String toAdd(){
		return "brand/add";
	}
	//保存
	@RequestMapping(value="/brand/add.do")
	public String add(Brand brand , Model model){
		//保存数据
		brandService.addBrand(brand);
		
		return "redirect:/brand/list.do";
	}
	//去修改页面
	@RequestMapping(value="/brand/toEdit.do")
	public String toEdit(Integer id ,Model model){
		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);
		return "brand/edit";
	}
	//去修改页面
		@RequestMapping(value="/brand/edit.do")
		public String edit(Brand brand ,Model model){
			brandService.updateBrand(brand);
			return "redirect:/brand/list.do";
		}
}
