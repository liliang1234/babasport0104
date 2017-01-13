package cn.itcast.core.service.staticpage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 静态化
 * @author liliang
 *
 */
public class StaticPageServiceImpl implements StaticPageService ,ServletContextAware{

	private Configuration config;
	
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		config = freeMarkerConfigurer.getConfiguration();
	}
	
	public void index(Map<String,Object> rootMap,Long id){
		String path = getPath("/html/product/"+id+".html");
		File f = new File(path);
		File parentFile = f.getParentFile();
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		Writer out = null;
		try {
			//编译对象
			Template template = config.getTemplate("productDetail.html");
			//输出最终文件位置
			out = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
			template.process(rootMap, out);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private ServletContext servletContext;
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext=servletContext;
	}
	public String getPath(String name){
		return servletContext.getRealPath(name);
	}
	
}
