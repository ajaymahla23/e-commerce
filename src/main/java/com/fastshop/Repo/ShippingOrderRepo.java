package com.fastshop.Repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fastshop.model.ShippingOrder;
import com.fastshop.model.User;

@Repository
public interface ShippingOrderRepo extends JpaRepository<ShippingOrder, Long> {

	public ShippingOrder findByUser(User user);

	@Transactional
	@Modifying
	@Query("update ShippingOrder s set s.shippingAddress=?1 ,s.state=?2,s.country=?3,s.pincode=?4,s.shippingPhoneNo=?5,s.remarks=?6 where s.user=?7")
	public void updateShippingAddress(String shippingAddress, String state, String country, String pincode,
			Long shippingPhoneNo, String remarks, User user);
}
