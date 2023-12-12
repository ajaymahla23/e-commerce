package com.fastshop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "my_order")
public class MyOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long myOrderId;

	@Column(name = "order_date")
	private Date date;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "amount")
	private String amount;

	@Column(name = "receipt")
	private String receipt;

	@Column(name = "status")
	private String status;

	@Column(name = "user_paymnt_status")
	private String userPayemetnStatus;

	@Column(name = "admin_delivery_status")
	private String adminDeliveryStatus;

	@Column(name = "approval_date")
	private Date approvalDate;

	@ManyToOne
	private User user;

	@Column(name = "payment_id")
	private String paymentId;

	@Column(name = "admin_TotalIncomeAmt")
	private double adminIncomeAmt;

	@ManyToOne
	@JoinColumn(name = "shippingOrder_id")
	private ShippingOrder shippingOrder;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "myOrder")
	private List<MyOrderDetail> myOrderDetailList = new ArrayList<>();

	public String getUserPayemetnStatus() {
		return userPayemetnStatus;
	}

	public void setUserPayemetnStatus(String userPayemetnStatus) {
		this.userPayemetnStatus = userPayemetnStatus;
	}

	public double getAdminIncomeAmt() {
		return adminIncomeAmt;
	}

	public void setAdminIncomeAmt(double adminIncomeAmt) {
		this.adminIncomeAmt = adminIncomeAmt;
	}

	public String getAdminDeliveryStatus() {
		return adminDeliveryStatus;
	}

	public void setAdminDeliveryStatus(String adminDeliveryStatus) {
		this.adminDeliveryStatus = adminDeliveryStatus;
	}

	public Date getDate() {
		return date;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public ShippingOrder getShippingOrder() {
		return shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<MyOrderDetail> getMyOrderDetailList() {
		return myOrderDetailList;
	}

	public void setMyOrderDetailList(List<MyOrderDetail> myOrderDetailList) {
		this.myOrderDetailList = myOrderDetailList;
	}

	public Long getMyOrderId() {
		return myOrderId;
	}

	public void setMyOrderId(Long myOrderId) {
		this.myOrderId = myOrderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

}
