package com.fastshop.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "products")
public class Products {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "product_name")
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", referencedColumnName = "category_id")
	private Category category;

	@Column(name = "product_price")
	private double price;

	@Column(name = "product_image")
	private String imageName;

	@Column(name = "product_qty")
	private double qty;

	@Column(name = "product_remarks")
	private String remarks;

	@Column(name = "add_people")
	private String addPeople = "admin";

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "products")
	List<ProductsDetail> productsDetailList = new ArrayList<>();

	public Products() {
		super();
	}

	public List<ProductsDetail> getProductsDetailList() {
		return productsDetailList;
	}

	public String getAddPeople() {
		return addPeople;
	}

	public void setAddPeople(String addPeople) {
		this.addPeople = addPeople;
	}

	public void setProductsDetailList(List<ProductsDetail> productsDetailList) {
		this.productsDetailList = productsDetailList;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public int getCategoryId() {

		return 0;
	}

}