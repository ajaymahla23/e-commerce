package com.fastshop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_detail")
public class MyOrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "oreder_detail_Id")
	private Long orderDetailId;

	@Column(name = "product_price")
	private Long productPrice;

	@Column(name = "tatal_qty")
	private int totalqty;

	@Column(name = "estimated_profit")
	private Long estimatedProfit;

	@Column(name = "remarks")
	private String remarks;

	private Integer rating;

	@Column(name = "mallproduct_price")
	private double mallproductprice;

	@Column(name = "product_returnOrNot")
	private String productReturnOrNot = "N";

	@ManyToOne
	@JoinColumn(name = "SHOP_ID")
	private ShopDetail shopDetail;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "productDet_id")
	private ProductsDetail productsDetail;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	private MyOrder myOrder;

	public Long getEstimatedProfit() {
		return estimatedProfit;
	}

	public void setEstimatedProfit(Long estimatedProfit) {
		this.estimatedProfit = estimatedProfit;
	}

	public String getProductReturnOrNot() {
		return productReturnOrNot;
	}

	public void setProductReturnOrNot(String productReturnOrNot) {
		this.productReturnOrNot = productReturnOrNot;
	}

	public double getMallproductprice() {
		return mallproductprice;
	}

	public void setMallproductprice(double mallproductprice) {
		this.mallproductprice = mallproductprice;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public int getTotalqty() {
		return totalqty;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setTotalqty(int totalqty) {
		this.totalqty = totalqty;
	}

	public Long getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Long productPrice) {
		this.productPrice = productPrice;
	}

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public ShopDetail getShopDetail() {
		return shopDetail;
	}

	public void setShopDetail(ShopDetail shopDetail) {
		this.shopDetail = shopDetail;
	}

	public ProductsDetail getProductsDetail() {
		return productsDetail;
	}

	public void setProductsDetail(ProductsDetail productsDetail) {
		this.productsDetail = productsDetail;
	}

	public MyOrder getMyOrder() {
		return myOrder;
	}

	public void setMyOrder(MyOrder myOrder) {
		this.myOrder = myOrder;
	}

}
