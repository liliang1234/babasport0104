package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.core.bean.product.Sku;

public interface SkuService {

	public List<Sku> selectSkuListByProductId(Long productId);
	
	public void update(Sku sku);
	
	public List<Sku> selectSkuListByProductIdAndStock(Long productId);
	
	public Sku selectSkuById(Long id);
	
	public Sku selectById(Long skuId);
}
