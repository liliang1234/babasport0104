package cn.itcast.core.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.itcast.core.Constants;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.service.user.BuyerService;

/**
 * 登录
 * 
 * @author liliang
 * 
 */
@Controller
public class LoginController {

	@Autowired
	private BuyerService buyerService;
	// 去登录页面
	@RequestMapping(value = "/shopping/login.shtml", method = RequestMethod.GET)
	public String login() {
		return "buyer/login";
	}

	// 提交登录
	@RequestMapping(value = "/shopping/login.shtml", method = RequestMethod.POST)
	public String login(String username, String password, String code,Model model,String returnUrl,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
		//验证码校验
		if(code != null){
			String c = (String) request.getSession().getAttribute(Constants.CODE_NAME);
			if(c.equalsIgnoreCase(code)){
				if(username != null){
					if(password != null){
						Buyer buyer = buyerService.selectBuyerById(username);
						if(buyer != null){
							//检验密码
							if(buyer.getPassword().equals(encodePassword(password))){
								//全部正确之后把用户放进session中
								request.getSession().setAttribute(Constants.BUYER_NAME, buyer);
								if(returnUrl!=null){
									URL url = null;
									try {
										url = new URL(returnUrl);
										String query = url.getQuery();
										if(query != null){
											String[] q = query.split("&");
											for (String string : q) {
												String[] split = string.split("=");
												model.addAttribute(split[0], URLDecoder.decode(split[1],"UTF-8"));
											}
										}
									} catch (MalformedURLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									return "redirect:"+url.getPath();
								}else{
									return "/buyer/index.shtml";
								}
							}else{
								model.addAttribute("error", "用户密码必须正确");
								return "buyer/login";
							}
						}else{
							model.addAttribute("error", "用户名必须正确");
							return "buyer/login";
						}
					}else{
						model.addAttribute("error", "密码不能为空");
						return "buyer/login";
					}
				}else{
					model.addAttribute("error", "用户名不能为空");
					return "buyer/login";
				}
			}else{
				model.addAttribute("error", "验证码必须正确");
				return "buyer/login";
			}
		}else{
			model.addAttribute("error", "验证码不能为空");
			return "buyer/login";
		}
	}

	// 验证码生成
	@RequestMapping(value = "/shopping/getCodeImage.shtml")
	public void getCodeImage(HttpServletRequest request,
			HttpServletResponse response) {
		System.out
				.println("#######################生成数字和字母的验证码#######################");
		BufferedImage img = new BufferedImage(68, 22,
				BufferedImage.TYPE_INT_RGB);
		// 得到该图片的绘图对象
		Graphics g = img.getGraphics();

		Random r = new Random();

		Color c = new Color(200, 150, 255);

		g.setColor(c);

		// 填充整个图片的颜色

		g.fillRect(0, 0, 68, 22);

		// 向图片中输出数字和字母

		StringBuffer sb = new StringBuffer();

		char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

		int index, len = ch.length;

		for (int i = 0; i < 4; i++) {

			index = r.nextInt(len);

			g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt

			(255)));

			g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));
			// 输出的 字体和大小

			g.drawString("" + ch[index], (i * 15) + 3, 18);
			// 写什么数字，在图片 的什么位置画

			sb.append(ch[index]);

		}
		request.getSession().setAttribute(Constants.CODE_NAME, sb.toString());
		try {
			ImageIO.write(img, "JPG", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 采用md5 + 十六进制
	 * @param Password
	 * @return
	 */
	public String encodePassword(String password){
		String algorithm = "MD5";
		MessageDigest instance = null;
		try {
			instance = MessageDigest.getInstance(algorithm);
			byte[] digest = instance.digest(password.getBytes());
			char[] encodeHex = Hex.encodeHex(digest);
			password = new String(encodeHex);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return password;
	}

}
