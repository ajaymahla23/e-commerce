package com.fastshop.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recharge_wallet")
public class RechargeWallet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date date;

	private double amountByUser;
	private String imageScreenshot;

	private String status = "U";

	private String roleType;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAmountByUser() {
		return amountByUser;
	}

	public void setAmountByUser(double amountByUser) {
		this.amountByUser = amountByUser;
	}

	public String getImageScreenshot() {
		return imageScreenshot;
	}

	public void setImageScreenshot(String imageScreenshot) {
		this.imageScreenshot = imageScreenshot;
	}

}
