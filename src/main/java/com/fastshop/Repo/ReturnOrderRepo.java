package com.fastshop.Repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastshop.model.ReturnOrder;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.User;

@Repository
public interface ReturnOrderRepo extends JpaRepository<ReturnOrder, Long> {

	@Transactional
	@Modifying
	@Query("update ReturnOrder r set r.reasonStatus=?1 where r.id=?2")
	public void updateApprovalStatusForReturnOrder(String reasonStatus, Long id);

	public List<ReturnOrder> findByShopDetail(ShopDetail shopDetail);

	@Query("Select r from ReturnOrder r where r.user=:user and r.myOrderDetail.orderDetailId=:orderDetailId")
	public ReturnOrder findByUserAndMyOrderDetail(@Param("user") User user, @Param("orderDetailId") Long orderDetailId);

	public List<ReturnOrder> findByUser(User user);

	@Query("select count(ro) from ReturnOrder ro where ro.shopDetail=?1 and ro.reasonStatus=?2")
	public Long countOfReturnProduct(ShopDetail shopDetail, String reasonStatus);

}
