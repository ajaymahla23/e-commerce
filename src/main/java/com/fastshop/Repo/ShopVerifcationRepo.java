package com.fastshop.Repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastshop.model.ShopDetail;
import com.fastshop.model.ShopVerification;
import com.fastshop.model.User;

@Repository
public interface ShopVerifcationRepo extends JpaRepository<ShopVerification, Long> {

	public ShopVerification findByStatusAndId(Long id, String status);

	@Modifying
	@Query("update ShopVerification s set s.status = ?1 where s.id=?2")
	void updateStatusByShopVerificationId(String status, Long shopVerificationId);

	public ShopVerification findByUserAndShopDetail(User user, ShopDetail shopDetail);

	public ShopVerification findByUser(User user);

	@Transactional
	@Modifying
	@Query(value = "UPDATE shop_verification s JOIN shop_detail d ON s.user_id = d.user_id SET d.shop_name = :shopName, d.shop_phone_no = :shopPhoneNo, d.shop_email = :shopEmail, d.introduction = :introduction WHERE s.id = :id", nativeQuery = true)
	public void updateStorDetail(@Param("shopName") String shopName,
			@Param("shopPhoneNo") String shopPhoneNo,
			@Param("shopEmail") String shopEmail,
			@Param("introduction") String introduction,
			@Param("id") Long id);

	@Query("SELECT pd FROM ShopVerification pd WHERE LOWER(pd.shopDetail.shopName) LIKE LOWER(CONCAT('%', :shopName, '%')) OR LOWER(pd.shopDetail.shopPhoneNo) LIKE LOWER(CONCAT('%', :shopPhoneNo, '%'))")
	public List<ShopVerification> findByShopDetail_ShopNameContainingIgnoreCase(@Param("shopName") String query,
			@Param("shopPhoneNo") String shopPhoneNo);

	@Query("select count(sv) from ShopVerification sv where sv.status=:status")
	public String listofverifyStore(@Param("status") String status);

	public List<ShopVerification> findByAddressContainingIgnoreCase(String address);
}
