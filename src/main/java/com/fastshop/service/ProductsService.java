package com.fastshop.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastshop.Repo.ProductDetailRepo;
import com.fastshop.Repo.ProductsRepo;
import com.fastshop.Repo.ShopDetailRepo;
import com.fastshop.Repo.UserRepository;
import com.fastshop.model.Products;
import com.fastshop.model.ProductsDetail;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.User;

@Service
public class ProductsService {
	@Autowired
	ProductsRepo productsRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	ProductDetailRepo productDetailRepo;
	@Autowired
	ShopDetailRepo shopDetailRepo;

	public List<Products> getAllProducts() {
		return productsRepo.findAll();
	}

	public void addProduct(Products products) {
		productsRepo.save(products);
	}

	public void removeProductsById(long id) {
		productsRepo.deleteById(id);
	}

	public Optional<Products> getProductsById(Long id) {
		return productsRepo.findById(id);
	}

	public List<Products> getAllProductsByCategoryId(int id) {
		return productsRepo.findAllByCategory_Id(id);
	}

	public Products deleteById(Long id) {
		return this.deleteById(id);
	}

	@Transactional
	public void updateByUserAndProductId(double quantity, double totalAmount, Long productId) {
		productsRepo.updateCartItemQuantity(quantity, totalAmount, productId);
	}

	public List<Products> updateStatusAndCalculationOfProduct(String active, Long[] ids, User user, Long percentage) {
		if (!userRepo.existsById(user.getId())) {
			throw new EntityNotFoundException("User not found");
		}
		List<Products> products = productsRepo.findAllById(Arrays.asList(ids));
		List<ProductsDetail> productsDetails = new ArrayList<>();

		ShopDetail shopDetail = shopDetailRepo.findByUser(user);

		for (Products product : products) {
			double price = product.getPrice();

			double estimatedProfit = (price * percentage) / 100;
			Long estProfit = (long) estimatedProfit;

			double unitPrice = price + estimatedProfit;
			Long unitPrice1 = (long) unitPrice;

			int discount = 0;
			Long discount1 = Long.valueOf(discount);

			ProductsDetail productsDetail = new ProductsDetail();
			productsDetail.setProducts(product);
			productsDetail.setActive(active);
			productsDetail.setMarkupPercentage(percentage);
			productsDetail.setEstimatedProfit(estProfit);
			productsDetail.setUnitPrice(unitPrice1);
			productsDetail.setUserId(user.getId());
			productsDetail.setDiscountPrice(discount1);
			productsDetail.setPublish(1);
			productsDetail.setShopDetail(shopDetail);
			productsDetail.setAfterDiscProductPrice(0L);
			productsDetails.add(productsDetail);
		}
		productDetailRepo.saveAll(productsDetails);
		return productsRepo.saveAll(products);
	}

}
