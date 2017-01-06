package cn.itcast.core.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.ColorQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.Type;
import cn.itcast.core.bean.product.TypeQuery;
import cn.itcast.core.bean.product.ProductQuery.Criteria;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.core.service.product.ColorService;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.service.product.TypeService;

/**
 * 商品后台
 * @author liliang
 *
 */
@Controller
public class ProductController {

	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private ColorService colorService;
	/**
	 * 商品的查询
	 * @param pageNo
	 * @param name
	 * @param brandId
	 * @param isShow
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/product/list.do")
	public String list(Integer pageNo ,String name,Long brandId,Boolean isShow,Model model){
		BrandQuery bquey = new BrandQuery();
		bquey.setIsDisplay(1);
		List<Brand> brands = brandService.selectBrands(bquey);
		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(3);
		productQuery.setOrderByClause("id desc");
		Criteria createCriteria = productQuery.createCriteria();
		StringBuilder params = new StringBuilder();
		//判断名称
		if(null != name){
			createCriteria.andNameLike("%"+name+"%");
			model.addAttribute("name", name);
			params.append("name=").append(name);
		}
		if(null != brandId){
			createCriteria.andBrandIdEqualTo(brandId);
			model.addAttribute("brandId", brandId);
			params.append("&brandId=").append(brandId);
		}
		if(null != isShow){
			createCriteria.andIsShowEqualTo(isShow);
			model.addAttribute("isShow", isShow);
			params.append("&isShow=").append(isShow);
		}else{
			createCriteria.andIsShowEqualTo(false);
			model.addAttribute("isShow", false);
			params.append("&isShow=").append(false);
		}
		Pagination pagination = productService.selectProductWithPage(productQuery);
		String url = "/product/list.do";
		pagination.pageView(url, params.toString());
		model.addAttribute("brands", brands);
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", productQuery.getPageNo());
		
		return "product/list";
	}
	@RequestMapping(value="/product/toAdd.do")
	public String toAdd(Model model){
		BrandQuery bquey = new BrandQuery();
		bquey.setIsDisplay(1);
		List<Brand> brands = brandService.selectBrands(bquey);
		model.addAttribute("brands", brands);
		//类型
		TypeQuery typeQuery = new TypeQuery();
		typeQuery.createCriteria().andParentIdNotEqualTo(0L);
		List<Type> types = typeService.selectTypeList(typeQuery);
		model.addAttribute("types", types);
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0L);
		List<Color> colors = colorService.selectColorList(colorQuery);
		model.addAttribute("colors", colors);
		return "product/add";
	}
	@RequestMapping(value="/product/add.do")
	public String add(Product product , Model model){
		productService.addProduct(product);;
		return "redirect:/product/list.do";
	}
}
