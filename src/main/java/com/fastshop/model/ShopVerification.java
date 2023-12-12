package com.fastshop.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "shop_verification")
public class ShopVerification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "user_name")
	private String userName;

	private Date date;

	@Column(name = "valid_id_doc")
	private String validIdDoc;

	@Column(name = "passport_country")
	private String passportCountry;

	@Column(name = "address")
	private String address;

	@ManyToOne
	@JoinColumn(name = "SHOP_ID")
	private ShopDetail shopDetail;

	@Column(name = "shop_status")
	private String status = "N";

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ShopDetail getShopDetail() {
		return shopDetail;
	}

	public void setShopDetail(ShopDetail shopDetail) {
		this.shopDetail = shopDetail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getValidIdDoc() {
		return validIdDoc;
	}

	public void setValidIdDoc(String validIdDoc) {
		this.validIdDoc = validIdDoc;
	}

	public String getPassportCountry() {
		return passportCountry;
	}

	public void setPassportCountry(String passportCountry) {
		this.passportCountry = passportCountry;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
