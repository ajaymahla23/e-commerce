package com.fastshop.dto;

import com.fastshop.model.ProductsDetail;

public class ProductDetailDTO {

	private Long productDetailId;
	private String productName;
	private Long unitPrice;
	private String remarks;
	private String cateogryName;
	private String imageName;
	private Long afterDiscProductPrice;

	private Long minPrice;
	private Long maxPrice;

	// Constructors, getters, and setters

	public ProductDetailDTO(ProductsDetail productsDetail) {
		this.productName = productsDetail.getProducts().getName();
		this.unitPrice = productsDetail.getUnitPrice();
		this.productDetailId = productsDetail.getUnitPrice();
		this.remarks = productsDetail.getProducts().getRemarks();
		this.cateogryName = productsDetail.getProducts().getCategory().getName();
		this.imageName = productsDetail.getProducts().getImageName();
		this.afterDiscProductPrice = productsDetail.getAfterDiscProductPrice();
	}

	public ProductDetailDTO(Long minPrice, Long maxPrice) {
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;

	}

	public Long getProductDetailId() {
		return productDetailId;
	}

	public void setProductDetailId(Long productDetailId) {
		this.productDetailId = productDetailId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCateogryName() {
		return cateogryName;
	}

	public void setCateogryName(String cateogryName) {
		this.cateogryName = cateogryName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Long getAfterDiscProductPrice() {
		return afterDiscProductPrice;
	}

	public void setAfterDiscProductPrice(Long afterDiscProductPrice) {
		this.afterDiscProductPrice = afterDiscProductPrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Long getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Long minPrice) {
		this.minPrice = minPrice;
	}

	public Long getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Long maxPrice) {
		this.maxPrice = maxPrice;
	}

}
