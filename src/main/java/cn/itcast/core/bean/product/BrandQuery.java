package cn.itcast.core.bean.product;

import java.io.Serializable;

/**
 * 品牌类型
 * @author liliang
 *
 */
public class BrandQuery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private String imgUrl;
	private String web_site;
	private Integer sort;
	private Integer isDisplay;
	private Integer pageNo = 1;
	private Integer pageSize = 10;
	//开始行
	private Integer startRow;
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
	public String getWeb_site() {
		return web_site;
	}
	public void setWeb_site(String web_site) {
		this.web_site = web_site;
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
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.startRow = (pageNo-1)*pageSize;
		this.pageNo = pageNo;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.startRow = (pageNo-1)*pageSize;
		this.pageSize = pageSize;
	}
	
	public Integer getStartRow() {
		return startRow;
	}
	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}
	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", description="
				+ description + ", imgUrl=" + imgUrl + ", web_site=" + web_site
				+ ", sort=" + sort + ", isDisplay=" + isDisplay + "]";
	}
	
}
