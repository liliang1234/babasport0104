package cn.itcast.core.controller.admin;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.itcast.core.Constants;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * 图片上传
 * @author liliang
 *
 */
@Controller
public class UploadController {

	//上传图片 异步
	//前提配置springmvc上传
	@RequestMapping(value="/upload/uploadPic.do")
	public void uploadPic(@RequestParam MultipartFile pic,HttpServletResponse resphone){
		
		Client client = new Client();
		//生成一个时间
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String name = df.format(new Date());
		//数据更多的话再加三位随机数
		Random r = new Random();
		for (int i = 0; i < 3; i++) {
			name +=r.nextInt(10);
		}
		//获取图片扩展名字
		String extension = FilenameUtils.getExtension(pic.getOriginalFilename());
		//需要保存到数据库的路径
		String path = "upload/"+name+"."+extension;
		
		String url = Constants.IMG_WEB+path;
		WebResource resource = client.resource(url);
		//推送
		try {
			resource.put(String.class, pic.getBytes());
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//返回图片路径到前台
		//拼接json
		JSONObject jo = new JSONObject();
		jo.put("url", url);
		jo.put("path", path);
		resphone.setContentType("application/json;charset=UTF-8");
		try {
			resphone.getWriter().write(jo.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
