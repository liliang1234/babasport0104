package cn.itcast.core.bean.product;

import java.io.Serializable;

import cn.itcast.core.Constants;

/**
 * 品牌类型
 * @author liliang
 *
 */
public class Brand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private String imgUrl;
	private String webSite;
	private Integer sort;
	private Integer isDisplay;
	public String getAllUrl(){
		return Constants.IMG_WEB+imgUrl;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", description="
				+ description + ", imgUrl=" + imgUrl + ", webSite=" + webSite
				+ ", sort=" + sort + ", isDisplay=" + isDisplay + "]";
	}
	
}
