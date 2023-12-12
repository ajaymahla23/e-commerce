package com.fastshop.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;

import com.fastshop.model.ProductsDetail;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.User;

public interface ProductDetailService {

	public List<ProductsDetail> updateStatus(String active, Long[] ids, User user, Long percentage);

	public List<ProductsDetail> getAllProductsDetail();

	public Optional<ProductsDetail> findProductDetailById(Long productDetailId);

	public ProductsDetail findByStoreDetailAndId(ShopDetail shop, Long id);

	List<ProductsDetail> getProductsDetailByCategory(String category, Pageable pageable);

//	public void updateAmountWithDiscountByProductDetailIdAndShopDetail(Long discountPrice, Long unitPrice,
//			Long estimatedProfit, Long productDetailId, ShopDetail shopDetail);

	public void updateAmountWithDiscountByProductDetailIdAndShopDetail(Long discountPrice, Long unitPrice,
			Long estimatedProfit, Long afterDiscProductPrice, Long productDetailId, ShopDetail shopDetail);

	public void updateProductPriceByAdmin(double price, Long id, Long productDetailId1, HttpServletRequest request);

	public void productViewOrNotInShop(Long productDetailId);

	public List<ProductsDetail> getProductsDetailByCategoryAndPublish(String category, int publish, Pageable pageable);

	public List<ProductsDetail> findByPublishAndProductNameOrCategoryNameAndShopNameContaining(String product,
			String category, String shopName, int publish);

	public List<ProductsDetail> getProductsByPriceRange(Long minPrice, Long maxPrice);
}
