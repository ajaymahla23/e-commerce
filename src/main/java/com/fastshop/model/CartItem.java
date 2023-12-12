package com.fastshop.model;

import java.util.Date;

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
@Table(name = "cart_item")
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int quantity = 1;
	private Date date;

	@Column(name = "cart_wishlist")
	private String wishlist = "C";

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SHOP_ID")
	private ShopDetail shopDetail;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "productDet_id")
	private ProductsDetail productsDetail;

	private double totalAmount;

	@Column(name = "product_price")
	private double productPrice;

	@ManyToOne
	@JoinColumn(name = "shippingOrder_id")
	private ShippingOrder shippingOrder;

	public ShippingOrder getShippingOrder() {
		return shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public ProductsDetail getProductsDetail() {
		return productsDetail;
	}

	public void setProductsDetail(ProductsDetail productsDetail) {
		this.productsDetail = productsDetail;
	}

	public String getWishlist() {
		return wishlist;
	}

	public void setWishlist(String wishlist) {
		this.wishlist = wishlist;
	}

	public ShopDetail getShopDetail() {
		return shopDetail;
	}

	public void setShopDetail(ShopDetail shopDetail) {
		this.shopDetail = shopDetail;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
