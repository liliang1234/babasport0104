package cn.itcast.common.convertion;

import org.springframework.core.convert.converter.Converter;

/**
 * 去除空格
 * @author liliang
 *
 */
public class CustomerTrimConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
		try {
			if(source != null){
				source = source.trim();
				if(!source.equals("")){
					return source;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
