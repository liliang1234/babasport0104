package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;

public interface ProductService {

	public Pagination selectProductWithPage(ProductQuery productQuery);

	public void addProduct(Product product);
	
	public void isShow(Long[] ids) throws Exception;
	
	public Product selectProductById(Long id);
}
