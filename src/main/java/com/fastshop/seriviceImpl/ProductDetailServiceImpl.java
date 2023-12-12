package com.fastshop.seriviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastshop.Repo.ProductDetailRepo;
import com.fastshop.Repo.ProductsRepo;
import com.fastshop.Repo.UserRepository;
import com.fastshop.model.Products;
import com.fastshop.model.ProductsDetail;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.User;
import com.fastshop.service.ProductDetailService;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
	@Autowired
	ProductsRepo productsRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	ProductDetailRepo productDetailRepo;

	public List<ProductsDetail> updateStatus(String active, Long[] ids, User user, Long percentage) {
		if (!userRepo.existsById(user.getId())) {
			throw new EntityNotFoundException("User not found");
		}
		List<Products> products = productsRepo.findAllById(Arrays.asList(ids));
		List<ProductsDetail> productsDetail = new ArrayList<>();
		for (Products product : products) {
			for (ProductsDetail detail : product.getProductsDetailList()) {
				double price = detail.getProducts().getPrice();
				double estimatedProfit = (price * percentage) / 100;

				Long estProfit = (long) estimatedProfit;
				double unitPrice = price + estimatedProfit;
				Long unitPrice1 = (long) unitPrice;

				detail.setProducts(product);
				detail.setActive(active);
				detail.setMarkupPercentage(percentage);
				detail.setEstimatedProfit(estProfit);
				detail.setUnitPrice(unitPrice1);
				detail.setUserId(user.getId());
				productsDetail.add(detail);
			}
		}
//		return productsRepo.saveAll(products);
		return productDetailRepo.saveAll(productsDetail);
	}

	public List<ProductsDetail> getAllProductsDetail() {
		return productDetailRepo.findAll();
	}

	@Override
	public Optional<ProductsDetail> findProductDetailById(Long id) {
		return productDetailRepo.findById(id);
	}

	@Override
	public List<ProductsDetail> getProductsDetailByCategory(String category, Pageable pageable) {
		return productDetailRepo.findByCategory(category, pageable);
	}

	@Override
	public List<ProductsDetail> getProductsDetailByCategoryAndPublish(String category, int publish, Pageable pageable) {
		return productDetailRepo.findByCategoryAndPublish(category, publish, pageable);
	}

	@Override
	public ProductsDetail findByStoreDetailAndId(ShopDetail shop, Long id) {
		return productDetailRepo.findByShopDetailAndId(shop, id);
	}

	@Override
	public List<ProductsDetail> findByPublishAndProductNameOrCategoryNameAndShopNameContaining(String product,
			String category, String shopName, int publish) {
		return productDetailRepo.findByPublishAndProductNameOrCategoryNameAndShopNameContaining(product, category,
				shopName, publish);
	}

//	@Transactional
//	@Override
//	public void updateAmountWithDiscountByProductDetailIdAndShopDetail(Long discountPrice, Long unitPrice,
//			Long estimatedProfit, Long productDetailId, ShopDetail shopDetail) {
//		productDetailRepo.updateAmountWithDiscountByProductDetailIdAndShopDetail(discountPrice, unitPrice,
//				estimatedProfit, productDetailId, shopDetail);
//	}

	@Transactional
	@Override
	public void updateAmountWithDiscountByProductDetailIdAndShopDetail(Long discountPrice, Long unitPrice,
			Long estimatedProfit, Long afterDiscProductPrice, Long productDetailId, ShopDetail shopDetail) {
		productDetailRepo.updateAmountWithDiscountByProductDetailIdAndShopDetail(discountPrice, unitPrice,
				estimatedProfit, afterDiscProductPrice, productDetailId, shopDetail);
	}

	@Override
	public void updateProductPriceByAdmin(double price, Long id, Long productDetailId1, HttpServletRequest request) {
		ProductsDetail productDetail = productDetailRepo.findById(productDetailId1).get();
		Long percentage = productDetail.getMarkupPercentage();
		Long discount = productDetail.getDiscountPrice();

		double estimatedProfit = (price * percentage) / 100;
		Long estProfit = (long) estimatedProfit;

		double unitPrice = price + estimatedProfit;
		Long unitPrice1 = (long) unitPrice;
		Long unitPriceByDiscount = unitPrice1 - discount;
		productDetailRepo.updateAmountWithDiscountByProductDetailId(unitPriceByDiscount, estProfit, productDetailId1);
	}

//	0 means product hide on home
//	1 means product show on home
	@Override
	public void productViewOrNotInShop(Long productDetailId) {
		ProductsDetail productDetail = productDetailRepo.findById(productDetailId).get();
		if (productDetail.getPublish() == 1) {
			ProductsDetail productDetail1 = productDetailRepo.findById(productDetailId).orElse(null);
			if (productDetail1 != null) {
				productDetail1.setPublish(0);
				productDetailRepo.save(productDetail1);
			}
		} else {
			ProductsDetail productDetail2 = productDetailRepo.findById(productDetailId).orElse(null);
			if (productDetail2 != null) {
				productDetail2.setPublish(1);
				productDetailRepo.save(productDetail2);
			}
		}

	}

	@Override
	public List<ProductsDetail> getProductsByPriceRange(Long minPrice, Long maxPrice) {
		return productDetailRepo.findByPriceBetween(minPrice, maxPrice);
	}

}
