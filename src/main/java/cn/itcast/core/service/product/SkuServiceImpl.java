package cn.itcast.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.SkuDao;

@Service
@Transactional
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
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

}
