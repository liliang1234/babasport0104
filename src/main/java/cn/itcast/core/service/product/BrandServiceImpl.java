package cn.itcast.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.dao.product.BrandDao;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

	@Autowired 
	BrandDao brandDao;
	@Override
	public List<Brand> selectBrands(BrandQuery bquey) {
		return brandDao.selectBrands(bquey);
	}
	@Override
	public Pagination selectBrandPages(BrandQuery bquey) {
		Pagination pagination = new Pagination(bquey.getPageNo(),bquey.getPageSize(),brandDao.selectCount(bquey));
		//设置结果集
		pagination.setList(brandDao.selectBrands(bquey));
		return pagination;
	}
	@Override
	public void deletes(Integer[] ids) {
		brandDao.deletes(ids);
	}
	@Override
	public void addBrand(Brand brand) {
		brandDao.addBrand(brand);
		
	}
	@Override
	public Brand selectBrandById(Integer id) {
		return brandDao.selectBrandById(id);
	}
	@Override
	public void updateBrand(Brand brand) {
		brandDao.updateBrand(brand);
	}

}
