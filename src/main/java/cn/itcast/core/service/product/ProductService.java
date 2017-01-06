package cn.itcast.core.service.product;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;

public interface ProductService {

	public Pagination selectProductWithPage(ProductQuery productQuery);

	public void addProduct(Product product);
}
