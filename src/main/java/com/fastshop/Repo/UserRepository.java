package com.fastshop.Repo;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastshop.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);

	public User findByMobileNumber(String mobileNumber);

	@Query("select u from User u where u.email = :email OR u.mobileNumber = :mobileNumber")
	public User getUserByUserName(@Param("email") String email, @Param("mobileNumber") String mobileNumber);

	@Modifying
	@Query("update User u set u.email=?1 ,u.mobileNumber=?2,u.password=?3 where u.id=?4")
	void updateEmailAndMobileNumber(String email, String mobileNumber, String password, Integer id);

	@Query("select u from User u where u.email = :email and u.mobileNumber = :mobileNumber ")
	public User findByEmailAndMobileNumber(@Param("email") String email, @Param("mobileNumber") String mobileNumber);

//	 FOR DASHBOARD	
	@Query("SELECT u, sd, w FROM User u INNER JOIN ShopDetail sd ON u.id = sd.user.id INNER JOIN Wallet w ON u.id = w.user.id")
	public List<User> findByUserAndShopDetailAndWallet();

	@Query(value = "SELECT u.user_id as userId, u.first_name as firstName, u.email as email, u.mobile_number as mobileNumber, sd.shop_logo as shopLogo, sd.shop_name as shopName, sd.SHOP_ID as shopId, sd.date as date, COALESCE(ap.wallet_amount, 0) as walletAmount FROM user u LEFT JOIN shop_detail sd ON u.user_id = sd.user_id LEFT JOIN admin_panel ap ON u.user_id = ap.user_id", nativeQuery = true)
	List<Map<String, Object>> findAllUsersWithWalletAmountAndShopDetails();

	@Query(value = "SELECT u.user_id as userId, u.first_name as firstName, u.email as email, u.mobile_number as phoneNumber, COALESCE(ap.wallet_amount, 0) as balance, sd.shop_logo as shopLogo, sd.SHOP_ID as shopId, sd.SHOP_EMAIL as shopEmail FROM user u LEFT JOIN admin_panel ap ON u.user_id = ap.user_id LEFT JOIN shop_detail sd ON u.user_id = sd.user_id", nativeQuery = true)
	List<Map<String, Object>> findAllUsersWithDetails();

	@Query("select count(u) from User u")
	public Long countByUser();

	@Query("select count(u) from User u where u.onlineStatus=:onlineStatus")
	public Long countByUserActive(String onlineStatus);

	@Transactional
	@Modifying
	@Query("update User u set u.onlineStatus=:onlineStatus where u.id=:id")
	public void updateOnlineStatus(@Param("onlineStatus") String onlineStatus, @Param("id") Integer id);

	public User findByIdAndRegisterStatus(Integer id, String registerStatus);

	@Transactional
	@Modifying
	@Query("update User u set u.email=:email where u.id=:id")
	public void updateEmail(@Param("email") String email, @Param("id") Integer id);

	public User findByResetPasswordToken(String token);

	public boolean existsByEmailOrMobileNumber(String email, String mobileNumber);

}
