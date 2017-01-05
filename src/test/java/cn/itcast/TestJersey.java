package cn.itcast;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class TestJersey {

	//发送图片
	public static void main(String[] args) throws IOException {
		Client client = new Client();
		String url = "http://localhost:8088/img-web/upload/ppp.jpg";
		WebResource resource = client.resource(url);
		String path = "I:\\学习\\资料2\\新巴巴\\新巴巴-1222\\新巴巴运动网资料\\图片资源\\53aa5bb8N9c338744.jpg";
		byte[] readFileToByteArray = FileUtils.readFileToByteArray(new File(path));
		//推送
		resource.put(String.class, readFileToByteArray);
		System.out.println("发布完成");
		
	}
}
