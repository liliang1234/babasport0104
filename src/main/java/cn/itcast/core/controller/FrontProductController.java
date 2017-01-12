package cn.itcast.core.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Img;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.core.service.product.ProductService;
import cn.itcast.core.service.product.SkuService;

/**
 * 商品管理
 *
 */
@Controller
public class FrontProductController {

	@Autowired
	private SolrServer solrServer; 
	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;
	@Autowired
	private SkuService skuService;
	//第一个Springmvc请求
	/*@RequestMapping("/test/index.do")
	public String index(){
		System.out.println("");
		return "";
	}*/
	@RequestMapping("/product/display/list.shtml")
	public String list(Integer pageNo,String keyword,Integer brandId,String price,Model model){
		//加载品牌
		BrandQuery bquey = new BrandQuery();
		bquey.setIsDisplay(1);
		List<Brand> brands = brandService.selectBrands(bquey);
		model.addAttribute("brands", brands);
		
		StringBuilder params = new StringBuilder();
		ProductQuery productQuery = new ProductQuery();
		//solr关键词
		SolrQuery solrQuery = new SolrQuery();
		if(keyword != null){
			solrQuery.set("q","name_ik:"+keyword);
			params.append("keyword="+keyword);
		}
		//设置当前页数
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(3);
		//开始行
		solrQuery.setStart(productQuery.getStartRow());
		//每页数
		solrQuery.setRows(productQuery.getPageSize());
		//高亮  开关
		solrQuery.setHighlight(true);
		//设置高亮字段  
		solrQuery.addHighlightField("name_ik");
		//设置高亮样式
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		QueryResponse queryResponse = null;
		try {
			queryResponse = solrServer.query(solrQuery);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Product product = null;
		List<Product> productArray = new ArrayList<Product>();
		SolrDocumentList results = queryResponse.getResults();
		long numFound = results.getNumFound();
		Pagination pagination = new Pagination(productQuery.getPageNo(),
				productQuery.getPageSize(),(int)numFound);
		for (SolrDocument solrDocument : results) {
			product = new Product();
			String id = (String) solrDocument.get("id");
			product.setId(Long.parseLong(id));
//			String name = (String) solrDocument.get("name_ik");
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
			product.setName(list.get(0));
			Float pri = (Float) solrDocument.get("price");
			product.setPrice(pri);
			String url = (String) solrDocument.get("url");
			Img img = new Img();
			img.setUrl(url);
			product.setImg(img);
			productArray.add(product);
		}
		pagination.setList(productArray);
		String url = "/product/display/list.shtml";
		pagination.pageView(url, params.toString());
		model.addAttribute("keyword", keyword);
		model.addAttribute("pagination", pagination);
		return "product/product";
	}
	@RequestMapping(value="/product/display/detail.shtml")
	public String detail(Model model,Long id){
		//查询商品
		Product product = productService.selectProductById(id);
		model.addAttribute("product", product);
		//查询有库存的sku
		List<Sku> skus = skuService.selectSkuListByProductIdAndStock(id);
		model.addAttribute("skus", skus);
		Set<Color> colors = new HashSet<Color>();
		for (Sku sku : skus) {
			colors.add(sku.getColor());
		}
		model.addAttribute("colors", colors);
		return "product/productDetail";
	}
	
}
