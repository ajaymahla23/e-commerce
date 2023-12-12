package com.fastshop.Repo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastshop.model.CartItem;
import com.fastshop.model.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	public Optional<CartItem> findById(Long id);

	public List<CartItem> findByUser(User user);

	@Query("select c from CartItem c where c.user=:user and c.productsDetail.productDetailId=:productDetailId")
	List<CartItem> findByUserAndProductId(@Param("user") User user, @Param("productDetailId") Long productsDetail);

	@Modifying
	@Query("update CartItem u set u.quantity = ?1 , u.totalAmount=?2 where u.user = ?3 and u.productsDetail.productDetailId=?4")
	void updateCartItemQuantity(int quantity, double totalAmount, User user, Long productDetailId);

	@Query("select c from CartItem c where  c.productsDetail.productDetailId=:productDetailId")
	public CartItem findByProductsDetailId(@Param("productDetailId") Long productsDetail);

	@Query("select c from CartItem c where c.user=:user and c.productsDetail.productDetailId=:productDetailId")
	public CartItem findByUserAndProduct(@Param("user") User user, @Param("productDetailId") Long productsDetail);

	public CartItem findByProductsDetail_ProductDetailIdAndUser(Long productDetailId, User user);

	@Transactional
	@Modifying
	@Query("update CartItem u set u.wishlist = ?1 where u.user = ?2 and u.productsDetail.productDetailId=?3")
	public void updateWishlist(String wishlist, User user, Long productDetailId);

	public List<CartItem> findByWishlistAndUser(String wishlist, User user);

	public List<CartItem> findByUserAndWishlist(User user, String wishlist);
}
