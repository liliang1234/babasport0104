package cn.itcast.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Img;
import cn.itcast.core.bean.product.ImgQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ImgDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;

@Service
@Transactional
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ImgDao imgDao;
	@Override
	public List<Sku> selectSkuListByProductId(Long productId) {
		SkuQuery example = new SkuQuery();
		example.createCriteria().andProductIdEqualTo(productId);
		List<Sku> skus = skuDao.selectByExample(example);
		for (Sku sku : skus) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
	public List<Sku> selectSkuListByProductIdAndStock(Long productId) {
		SkuQuery example = new SkuQuery();
		example.createCriteria().andProductIdEqualTo(productId).andStockGreaterThan(0);
		List<Sku> skus = skuDao.selectByExample(example);
		for (Sku sku : skus) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
	@Override
	public void update(Sku sku) {
		skuDao.updateByPrimaryKeySelective(sku);
	}
	
	public Sku selectSkuById(Long skuId){
		Sku sku = skuDao.selectByPrimaryKey(skuId);
		Product product = productDao.selectByPrimaryKey(sku.getProductId());
		ImgQuery iq = new ImgQuery();
		iq.createCriteria().andProductIdEqualTo(product.getId()).andIsDefEqualTo(true);
		List<Img> imgs = imgDao.selectByExample(iq);
		product.setImg(imgs.get(0));
		//颜色
		sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		sku.setProduct(product);
		return sku;
	}
	public Sku selectById(Long skuId){
		return skuDao.selectByPrimaryKey(skuId);
	}

}
