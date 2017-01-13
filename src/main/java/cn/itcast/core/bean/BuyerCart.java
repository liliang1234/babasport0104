package cn.itcast.core.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import cn.itcast.core.bean.product.Sku;

/**
 * 购物车	
 * @author liliang
 *
 */
public class BuyerCart {
	
	private List<BuyerItem> buyerItems = new ArrayList<BuyerItem>();
	
	public void deleteItem(Long skuId){
		BuyerItem item = new BuyerItem();
		Sku sku = new Sku();
		sku.setId(skuId);
		item.setSku(sku);
		buyerItems.remove(item);
	}
	
	public void addItem(BuyerItem item){
		if(buyerItems.contains(item)){
			for(BuyerItem ite : buyerItems){
				if(ite.equals(item)){
					ite.setAmoutnt(ite.getAmoutnt()+item.getAmoutnt());
				}
			}
		}else{
			buyerItems.add(item);
		}
	}
	//获取数量
	@JsonIgnore
	public Integer getProductAmoutnt(){
		Integer result = 0;
		for(BuyerItem item : buyerItems){
			result+=item.getAmoutnt();
		}
		return result;
	}
	//获取金额
	@JsonIgnore
	public Float getProductPrice(){
		Float result = 0f;
		for(BuyerItem item : buyerItems){
			result+=item.getAmoutnt()*item.getSku().getPrice();
		}
		return result;
	}
	//获取运费
	@JsonIgnore
	public Float getFee(){
		Float result = 0f;
		if(getProductPrice() <= 59){
			result =10f;
		}
		return result;
	}
	//总金额
	@JsonIgnore
	public Float getTotalPrice(){
		return getProductPrice()+getFee();
	}
	public List<BuyerItem> getBuyerItems() {
		return buyerItems;
	}

	public void setBuyerItems(List<BuyerItem> buyerItems) {
		this.buyerItems = buyerItems;
	}
	
	
}
