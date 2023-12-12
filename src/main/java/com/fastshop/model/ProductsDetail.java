package com.fastshop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_detail")
public class ProductsDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productDetailId;

	@Column(name = "status")
	private String active = "N";

	@Column(name = "markup_percentage")
	private Long markupPercentage;

	@Column(name = "estimated_profit")
	private Long estimatedProfit;

	@Column(name = "unit_price")
	private Long unitPrice;

	@Column(name = "discount_price")
	private Long discountPrice;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "publish_status")
	public int publish;

	@Column(name = "after_disc_productprice")
	private Long afterDiscProductPrice;

	@ManyToOne
	@JoinColumn(name = "id")
	private Products products;

	@ManyToOne
	@JoinColumn(name = "SHOP_ID")
	private ShopDetail shopDetail;

	public ProductsDetail() {
		super();
	}

	public Long getAfterDiscProductPrice() {
		return afterDiscProductPrice;
	}

	public void setAfterDiscProductPrice(Long afterDiscProductPrice) {
		this.afterDiscProductPrice = afterDiscProductPrice;
	}

	public ShopDetail getShopDetail() {
		return shopDetail;
	}

	public void setShopDetail(ShopDetail shopDetail) {
		this.shopDetail = shopDetail;
	}

	public int getPublish() {
		return publish;
	}

	public void setPublish(int publish) {
		this.publish = publish;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public Long getProductDetailId() {
		return productDetailId;
	}

	public void setProductDetailId(Long productDetailId) {
		this.productDetailId = productDetailId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public Long getMarkupPercentage() {
		return markupPercentage;
	}

	public void setMarkupPercentage(Long markupPercentage) {
		this.markupPercentage = markupPercentage;
	}

	public Long getEstimatedProfit() {
		return estimatedProfit;
	}

	public void setEstimatedProfit(Long estimatedProfit) {
		this.estimatedProfit = estimatedProfit;
	}

	public Long getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Long getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Long discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
