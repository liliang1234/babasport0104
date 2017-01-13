package cn.itcast.core.service.product;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Img;
import cn.itcast.core.bean.product.ImgQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.bean.product.SkuQuery.Criteria;
import cn.itcast.core.dao.product.ImgDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import cn.itcast.core.service.staticpage.StaticPageService;

/**
 * 商品管理
 * @author liliang
 *
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private Jedis jedis;
	@Autowired
	private ImgDao imgDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private SolrServer solrServer;
	@Autowired
	private StaticPageService staticPageService;
	@Autowired
	private SkuService skuService;
	//返回分页对象
	public Pagination selectProductWithPage(ProductQuery productQuery){
		List<Product> products = productDao.selectByExample(productQuery);
		for (Product product : products) {
			
			ImgQuery imgQuery = new ImgQuery();
			imgQuery.createCriteria().andProductIdEqualTo(product.getId()).andIsDefEqualTo(true);
			List<Img> imgs = imgDao.selectByExample(imgQuery);
			product.setImg(imgs.get(0));
		}
		Pagination pagination = new Pagination(productQuery.getPageNo(),
				productQuery.getPageSize(),
				productDao.countByExample(productQuery),
				products);
		
		
		return pagination;
	}
	@Override
	public void addProduct(Product product) {
		//生成id
		Long id = jedis.incr("pno");
		product.setId(id);
		//上下架
		product.setIsShow(false);
		//设置是否删除
		product.setIsDel(true);
		product.setCreateTime(new Date());
		
		productDao.insert(product);
		
		//保存图片信息
		Img img = product.getImg();
		img.setProductId(id);
		img.setIsDef(true);
		imgDao.insert(img);
		//保存sku信息
		Sku sku = null;
		for (String color : product.getColors().split(",")) {
			sku = new Sku();
			sku.setProductId(id);
			sku.setColorId(Long.parseLong(color));
			for (String size : product.getSizes().split(",")) {
				sku.setSize(size);
				sku.setDeliveFee(10f);
				sku.setPrice(0f);
				sku.setStock(0);
				sku.setUpperLimit(200);
				sku.setMarketPrice(0f);
				sku.setCreateTime(new Date());
				sku.setSkuType(true);
				skuDao.insert(sku);
			}
			
		}
		//保存尺码
		//保存颜色
		
	}
	/**
	 * 商品上架
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void isShow(Long [] ids) throws Exception {
		Product product = null;
		for (Long id : ids) {
			product = new Product();
			product.setId(id);
			//设置上架
			product.setIsShow(true);
			productDao.updateByPrimaryKeySelective(product);
			Product pro = productDao.selectByPrimaryKey(id);
			SolrInputDocument inputDocument = new SolrInputDocument();
			inputDocument.setField("id", id);
			inputDocument.setField("name_ik", pro.getName());
			//品牌id
			inputDocument.setField("brandId", pro.getBrandId());
			//商品价格
			SkuQuery example = new SkuQuery();
			example.createCriteria().andProductIdEqualTo(id);
			List<Sku> skus = skuDao.selectByExample(example);
			inputDocument.setField("price", skus.get(0).getPrice());
			//图片的url
			ImgQuery iq = new ImgQuery();
			iq.createCriteria().andProductIdEqualTo(id).andIsDefEqualTo(true);
			List<Img> imgs = imgDao.selectByExample(iq);
			inputDocument.setField("url", imgs.get(0).getUrl());
			
			
			//添加时间
			inputDocument.setField("last_modified", new Date());
			
			solrServer.add(inputDocument);
			solrServer.commit();
			
			
			Map<String, Object> rootMap = new HashMap<String, Object>();
			//查询商品
			Product pp = selectProductById(id);
			rootMap.put("product", pp);
			//查询有库存的sku
			List<Sku> ss = skuService.selectSkuListByProductIdAndStock(id);
			rootMap.put("skus", ss);
			Set<Color> colors = new HashSet<Color>();
			for (Sku sku : ss) {
				colors.add(sku.getColor());
			}
			rootMap.put("colors", colors);
			//静态化页面
			staticPageService.index(rootMap, id);
		}
	}
	@Override
	public Product selectProductById(Long id) {
		Product product = productDao.selectByPrimaryKey(id);
		ImgQuery example = new ImgQuery();
		example.createCriteria().andProductIdEqualTo(id).andIsDefEqualTo(true);
		List<Img> imgs = imgDao.selectByExample(example);
		product.setImg(imgs.get(0));
		return product;
	}
}
