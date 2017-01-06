package cn.itcast.core.service.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Img;
import cn.itcast.core.bean.product.ImgQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.dao.product.ImgDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;

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
}
