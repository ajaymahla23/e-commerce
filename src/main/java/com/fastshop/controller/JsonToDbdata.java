package com.fastshop.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonToDbdata {

	public static void main(String[] args) {
		Path path = Paths.get("D:/Projects/json data/laptop.json");
		System.out.println("path :" + path);
		String json = null;
		try {
			json = new String(Files.readAllBytes(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, List<Products>>>() {
		}.getType();
		Map<String, List<Products>> data = gson.fromJson(json, type);
		List<Products> products = data.get("AppleLaptop");

		String url = "jdbc:mysql://localhost:3306/fastshop_new";
		String username = "root";
		String password = "root";

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			for (Products product : products) {
				String sql = "INSERT INTO products (id,category_id, product_image,product_name,product_price,product_qty,product_remarks) VALUES ( ?,?,?,?,?,?,?)";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setLong(1, product.getId());
					statement.setInt(2, product.getCategory());
					statement.setString(3, product.getImageName());
					statement.setString(4, product.getName());
					statement.setDouble(5, product.getPrice());
					statement.setDouble(6, product.getQty());
					statement.setString(7, product.getRemarks());
					statement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public class Products {
		private Long id;
		private String name;
		private int category = 1;
		private double price;
		private String imageName;
		private double qty;
		private String remarks;

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

		public int getCategory() {
			return category;
		}

		public void setCategory(int category) {
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

		public double getQty() {
			return qty;
		}

		public void setQty(double qty) {
			this.qty = qty;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

	}

}
