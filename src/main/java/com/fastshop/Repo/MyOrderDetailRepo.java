package com.fastshop.Repo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastshop.model.MyOrderDetail;
import com.fastshop.model.ShopDetail;
import com.fastshop.model.User;

@Repository
public interface MyOrderDetailRepo extends JpaRepository<MyOrderDetail, Long> {

	public List<MyOrderDetail> findByShopDetail(ShopDetail shopDetail);

	@Query("select m from MyOrderDetail m where m.shopDetail=:shopDetail and m.myOrder.myOrderId=:myOrderId")
	public List<MyOrderDetail> findByShopDetailAndMyOrder(@Param("shopDetail") ShopDetail shopDetail,
			@Param("myOrderId") Long myOrderId);

	@Query("select m from MyOrderDetail m where  m.myOrder.user=:user")
	public List<MyOrderDetail> findByUser(@Param("user") User user);

	@Query("select sum(od.productPrice) from MyOrderDetail od where od.shopDetail=?1 and od.productReturnOrNot=?2")
	Long sumOfProuductPriceSaleByShop(ShopDetail shopDetail, String productReturnOrNot);

	@Query("select sum(od.productPrice) from MyOrderDetail od where od.shopDetail=:shopDetail and od.myOrder.date between :fromDate and :toDate")
	Long sumOfProuductPriceSaleByShopBetweenDate(ShopDetail shopDetail, Date fromDate, Date toDate);

	@Query("select count(od.productsDetail) from MyOrderDetail od where od.shopDetail=?1 and od.productReturnOrNot=?2")
	Long countOfSuccessfullOrderByShop(ShopDetail shopDetail, String productReturnOrNot);

	@Query("select sum(od.estimatedProfit) from MyOrderDetail od where od.shopDetail=:shopDetail and od.productReturnOrNot=:productReturnOrNot")
	public Long sumOfEstimatedProfitPrice(@Param("shopDetail") ShopDetail shopDetail,
			@Param("productReturnOrNot") String productReturnOrNot);

	@Query("select sum(od.estimatedProfit) from MyOrderDetail od where od.shopDetail=:shopDetail and od.myOrder.date between :fromDate and :toDate")
	public Long sumOfEstimatedProfitPriceBetweenDate(@Param("shopDetail") ShopDetail shopDetail, Date fromDate,
			Date toDate);

	@Query("SELECT pd FROM MyOrderDetail pd WHERE LOWER(pd.myOrder.orderId) LIKE LOWER(CONCAT('%', :orderId, '%'))")
	public List<MyOrderDetail> findByMyOrderDetailOrderIdContainingIgnoreCase(@Param("orderId") String query);

	@Query(value = "SELECT ro.return_order_status as return_order_status,o.oreder_detail_id as orderdetalid,ro.id as returnid FROM fastshop_new.order_detail AS o LEFT JOIN fastshop_new.return_order AS ro ON o.oreder_detail_id = ro.my_order_detail_id where o.product_det_id=:product_det_id", nativeQuery = true)
	List<Map<String, Object>> getReturnOrderStatus(@Param("product_det_id") Long product_det_id);

	@Transactional
	@Modifying
	@Query("update MyOrderDetail m set m.productReturnOrNot=:productReturnOrNot where m.orderDetailId=:orderDetailId")
	public void returnProductfromOrder(@Param("productReturnOrNot") String productReturnOrNot,
			@Param("orderDetailId") Long orderDetailId);

}
