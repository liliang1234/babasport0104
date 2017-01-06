package cn.itcast.core.controller.admin;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fckeditor.response.UploadResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

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
	@RequestMapping(value="/upload/uploadFck.do")
	public void uploadFck(HttpServletRequest request,HttpServletResponse response){
		//虽然  图片肯定在Request当中
		//Spring 提供一个 MultipartReqeust一个接口  将Request强转成MultipartReqeust 
		//只有图片  
		MultipartRequest mr = (MultipartRequest)request;
		//从此对象中获取文件  长度是1  
		//带名称
		//MultipartFile 图片
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		//遍历Map
		Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			//K 是 名称
			//V  是图片
			MultipartFile pic = entry.getValue();
			
			System.out.println(pic.getOriginalFilename());
			//名字的生成策略
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			//图片名
			String name = df.format(new Date());
			//加三位随机数
			Random r = new Random();
			for (int i = 0; i < 3; i++) {
				name +=r.nextInt(10);
			}
			//扩展名
			String ext = FilenameUtils.getExtension(pic.getOriginalFilename());
			
			//图片不在本服务器下的  发送另一台服务器
			//实例化客户端
			Client client = new Client();
			
			//保存数据库
			String path =  "upload/" + name + "." + ext;
			
			//发送到哪里  请求路径
			String url = Constants.IMG_WEB + path;
			
			//
			WebResource resource = client.resource(url);
			//推送  GET POST 
			try {
				resource.put(String.class, pic.getBytes());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//url
			//Fck的Jar包
			//把URL通过Response返回到页面、UploadResponse.class
			UploadResponse ok = UploadResponse.getOK(url);
			//将上面的对象仍回页面  页面的Fck就能接收了
			try {
				response.getWriter().print(ok);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}
}
