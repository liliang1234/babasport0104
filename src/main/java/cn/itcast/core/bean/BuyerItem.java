package cn.itcast.core.bean;

import cn.itcast.core.bean.product.Sku;

public class BuyerItem {

	private Sku sku;
	
	private Integer amoutnt = 1;
	//有货无货
	private Boolean isStock = true;
	
	public Boolean getIsStock() {
		return isStock;
	}

	public void setIsStock(Boolean isStock) {
		this.isStock = isStock;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Integer getAmoutnt() {
		return amoutnt;
	}

	public void setAmoutnt(Integer amoutnt) {
		this.amoutnt = amoutnt;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuyerItem other = (BuyerItem) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.getId().equals(other.sku.getId()))
			return false;
		return true;
	}
	
}
