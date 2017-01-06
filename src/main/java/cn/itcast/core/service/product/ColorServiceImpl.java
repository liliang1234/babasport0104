package cn.itcast.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.ColorQuery;
import cn.itcast.core.dao.product.ColorDao;

@Service
@Transactional
public class ColorServiceImpl implements ColorService {

	@Autowired
	private ColorDao colorDao;
	@Override
	public List<Color> selectColorList(ColorQuery colorQuery) {
		return colorDao.selectByExample(colorQuery);
	}

}
