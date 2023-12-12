package com.fastshop.Repo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastshop.model.ShopDetail;
import com.fastshop.model.User;

@Repository
public interface ShopDetailRepo extends JpaRepository<ShopDetail, Long> {

	public ShopDetail findByUser(User user);

	@Query("select s,p from ShopDetail s inner JOIN ProductsDetail p on s.id=p.shopDetail.id where p.productDetailId=:productDetailId")
	public ShopDetail findShopByProductDetailId(@Param("productDetailId") Long id);

	@Transactional
	@Modifying
	@Query("update ShopDetail sd set sd.shopName=?1,sd.shopPhoneNo=?2,sd.shopAddress=?3,sd.introduction=?4 where sd.id=?5")
	void updateShopDetail(String shopName, String shopPhoneNo, String shopAddress, String introduction, Long id);

	@Transactional
	@Modifying
	@Query("update ShopDetail s set s.shopEmail = :shopEmail where s.user.id = :userId")
	public void updateEmailByUser(@Param("shopEmail") String shopEmail, @Param("userId") Integer userId);

	@Query(value = "SELECT s.shop_id as shopId, s.shop_name as shopName, s.shop_email as shop_email, s.shop_phone_no as phoneNumber, SUM(COALESCE(ap.product_price, 0)) as balance FROM fastshop_new.shop_detail s INNER JOIN fastshop_new.order_detail ap ON s.shop_id = ap.shop_id GROUP BY s.shop_id", nativeQuery = true)
	List<Map<String, Object>> findTotalBalanceByShop();

	@Query("select s,p from ShopDetail s inner JOIN ProductsDetail p on s.id=p.shopDetail.id where p.productDetailId IN :ids")
	public List<ShopDetail> findShopByProductDetail(@Param("ids") Collection<Long> ids);

}
