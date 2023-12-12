package com.fastshop.Repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastshop.model.ProductsDetail;
import com.fastshop.model.ShopDetail;

@Repository
public interface ProductDetailRepo extends JpaRepository<ProductsDetail, Long> {

	public List<ProductsDetail> findByActiveAndUserId(String active, Integer userId);

	@Query("SELECT pd.products.id FROM ProductsDetail pd WHERE pd.userId = :userId AND pd.products.id IN :productIds")
	List<Long> findExistingProductIds(@Param("userId") Integer userId, @Param("productIds") Long[] productIds);

	@Query("SELECT pd.productDetailId FROM ProductsDetail pd " + "WHERE pd.markupPercentage = :percentage "
			+ "AND pd.userId = :userId " + "AND pd.products.id IN :productIds")
	List<Long> findExistingIds(@Param("percentage") Long percentage, @Param("userId") Integer userId,
			@Param("productIds") List<Long> productIds);

	@Modifying
	@Query("update ProductsDetail p set p.active=:active where p.productDetailId=:productDetailId ")
	public void updatePublishCheckout(@Param("active") String publish, @Param("productDetailId") Long productDetailId);

	@Query("SELECT pd FROM ProductsDetail pd WHERE pd.products.category.name = :category")
	List<ProductsDetail> findByCategory(@Param("category") String category, Pageable pageable);

	@Query("SELECT pd FROM ProductsDetail pd WHERE pd.products.category.name = :category and pd.publish=:publish")
	List<ProductsDetail> findByCategoryAndPublish(@Param("category") String category, @Param("publish") int publish,
			Pageable pageable);

	@Query("select p from ProductsDetail p where p.shopDetail=:shop and p.productDetailId=:productDetailId ")
	public ProductsDetail findByShopDetailAndId(@Param("shop") ShopDetail shopDetail,
			@Param("productDetailId") Long productDetailId);

	@Query("SELECT pd FROM ProductsDetail pd WHERE pd.publish=:publish and (LOWER(pd.products.name) LIKE LOWER(CONCAT('%', :product, '%')) OR LOWER(pd.products.category.name) LIKE LOWER(CONCAT('%', :category, '%')) OR LOWER(pd.shopDetail.shopName) LIKE LOWER(CONCAT('%', :shopDetail, '%')))")
	List<ProductsDetail> findByPublishAndProductNameOrCategoryNameAndShopNameContaining(
			@Param("product") String productName, @Param("category") String categoryName,
			@Param("shopDetail") String shopName, @Param("publish") int publish);

//	@Modifying
//	@Query("update ProductsDetail pd set pd.discountPrice=?1 ,pd.unitPrice=?2,pd.estimatedProfit=?3 where pd.productDetailId=?4 and pd.shopDetail=?5")
//	public void updateAmountWithDiscountByProductDetailIdAndShopDetail(Long discountPrice, Long unitPrice,
//			Long estimatedProfit, Long productDetailId, ShopDetail shopDetail);

	@Modifying
	@Query("update ProductsDetail pd set pd.discountPrice=?1 ,pd.unitPrice=?2,pd.estimatedProfit=?3,pd.afterDiscProductPrice=?4 where pd.productDetailId=?5 and pd.shopDetail=?6")
	public void updateAmountWithDiscountByProductDetailIdAndShopDetail(Long discountPrice, Long unitPrice,
			Long estimatedProfit, Long afterDiscProductPrice, Long productDetailId, ShopDetail shopDetail);

	@Query("select count(pd.active) from ProductsDetail pd where pd.active=?1 and pd.shopDetail=?2")
	Long countOfProuducts(String active, ShopDetail shopDetail);

	@Query("SELECT p FROM ProductsDetail p WHERE (SELECT COUNT(p2) FROM ProductsDetail p2 WHERE p2.products.category = p.products.category AND p2.products.id >= p.products.id) <= 3")
	public List<ProductsDetail> findBytopProducts();

	@Query("SELECT p FROM ProductsDetail p WHERE p.publish = 1 AND (SELECT COUNT(p2) FROM ProductsDetail p2 WHERE p2.products.category = p.products.category AND p2.products.id >= p.products.id and p2.unitPrice >= p.unitPrice) <= 3")
	public List<ProductsDetail> findByTopMinPriceProducts();

//	new
	@Query("SELECT p FROM ProductsDetail p WHERE p.publish = 1 AND (SELECT COUNT(p2) FROM ProductsDetail p2 WHERE p2.products.category = p.products.category AND p2.products.id >= p.products.id and p2.unitPrice >= p.unitPrice) >= 3")
	public List<ProductsDetail> findByTopMaxPriceProducts();

	List<ProductsDetail> findAllByProducts_Category_Id(int id);

	@Transactional
	@Modifying
	@Query("update ProductsDetail pd set pd.unitPrice=?1,pd.estimatedProfit=?2 where pd.productDetailId=?3")
	public void updateAmountWithDiscountByProductDetailId(Long unitPrice, Long estimatedProfit, Long productDetailId);

	@Query("SELECT pd FROM ProductsDetail pd WHERE LOWER(pd.products.name) LIKE LOWER(CONCAT('%', :product, '%')) OR LOWER(pd.products.category.name) LIKE LOWER(CONCAT('%', :category, '%'))")
	List<ProductsDetail> findByProductNameOrCategoryNameContaining(@Param("product") String productName,
			@Param("category") String categoryName);

	@Query("SELECT pd FROM ProductsDetail pd WHERE pd.products.category.name = :category")
	List<ProductsDetail> getProductsDetailByCategory(@Param("category") String category);

	@Transactional
	@Modifying
	@Query("update ProductsDetail p set p.active=:active where p.productDetailId=:productDetailId and p.markupPercentage=:markupPercentage and p.userId=:userId ")
	public void updateExistingdata(@Param("active") String publish,
			@Param("productDetailId") List<Long> productDetailId, @Param("markupPercentage") Long markupPercentage,
			@Param("userId") Integer userId);

	@Query("select pd from ProductsDetail pd where  pd.unitPrice between :minPrice and :maxPrice")
	public List<ProductsDetail> findByPriceBetween(Long minPrice, Long maxPrice);

	@Query("SELECT MIN(p.unitPrice) FROM ProductsDetail p")
	Long findMinPrice();

	@Query("SELECT MAX(p.unitPrice) FROM ProductsDetail p")
	Long findMaxPrice();

	@Query(value = "SELECT * FROM fastshop_new.product_detail ORDER BY unit_price ASC LIMIT 5;", nativeQuery = true)
	List<ProductsDetail> minPriceTop5Products();

}
