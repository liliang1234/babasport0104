package cn.itcast.core.dao.product;

import java.util.List;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;

/**
 * 数据持久层的接口
 * @author liliang
 *
 */
public interface BrandDao {
	//查询品牌集合
	public List<Brand> selectBrands(BrandQuery bquey);
	//查询品牌数量
	public int selectCount(BrandQuery bquey);
	public void deletes(Integer[] ids);
	//保存的方法
	public void addBrand(Brand brand);
	//通过id查询品牌
	public Brand selectBrandById(Integer id);
	//更新
	public void updateBrand(Brand brand);

}
