package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;

public interface BrandService {

	// 查询品牌集合
	public List<Brand> selectBrands(BrandQuery bquey);

	// 分页
	public Pagination selectBrandPages(BrandQuery bquey);

	// 批量删除
	public void deletes(Integer[] ids);

	// 保存品牌
	public void addBrand(Brand brand);

	// 通过id查询品牌
	public Brand selectBrandById(Integer id);

	// 更新
	public void updateBrand(Brand brand);
}
