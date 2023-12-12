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
@Table(name = "return_order")
public class ReturnOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date date;

	@Column(name = "return_order_reason")
	private String reason;

	@Column(name = "return_order_status")
	private String reasonStatus;

	@Column(name = "order_number")
	private String orderNumner;

	@Column(name = "product_totalAmt")
	private String prodouctTotalAmt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "myOrderDetail_id")
	private MyOrderDetail myOrderDetail;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "productDet_id")
	private ProductsDetail productsDetail;

	@ManyToOne
	@JoinColumn(name = "SHOP_ID")
	private ShopDetail shopDetail;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getReasonStatus() {
		return reasonStatus;
	}

	public void setReasonStatus(String reasonStatus) {
		this.reasonStatus = reasonStatus;
	}

	public String getOrderNumner() {
		return orderNumner;
	}

	public void setOrderNumner(String orderNumner) {
		this.orderNumner = orderNumner;
	}

	public ShopDetail getShopDetail() {
		return shopDetail;
	}

	public void setShopDetail(ShopDetail shopDetail) {
		this.shopDetail = shopDetail;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getProdouctTotalAmt() {
		return prodouctTotalAmt;
	}

	public void setProdouctTotalAmt(String prodouctTotalAmt) {
		this.prodouctTotalAmt = prodouctTotalAmt;
	}

	public ProductsDetail getProductsDetail() {
		return productsDetail;
	}

	public void setProductsDetail(ProductsDetail productsDetail) {
		this.productsDetail = productsDetail;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public MyOrderDetail getMyOrderDetail() {
		return myOrderDetail;
	}

	public void setMyOrderDetail(MyOrderDetail myOrderDetail) {
		this.myOrderDetail = myOrderDetail;
	}

}
