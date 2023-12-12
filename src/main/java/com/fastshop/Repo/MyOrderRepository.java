package com.fastshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fastshop.model.MyOrder;
import com.fastshop.model.User;

@Repository
public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {

	@Modifying
	@Query("update MyOrder u set u.amount = ?1 , u.user = ?2")
	MyOrder addAmountByUserId(String amount, User user);

	public long countByUser(User user);

	@Query("select sum(m.adminIncomeAmt) from MyOrder m ")
	public String sumOfTotalAmount();

	@Query(value = "SELECT SUM(o.admin_total_income_amt) as total_income FROM my_order as o  WHERE o.order_date >= DATE_ADD(CURDATE(), INTERVAL -1 MONTH) AND o.order_date < CURDATE() LIMIT 0, 1000;", nativeQuery = true)
	public double sumOfAmtLastOneMonth();

	@Query(value = "SELECT SUM(o.admin_total_income_amt) as total_income FROM my_order as o WHERE o.order_date >= DATE_ADD(CURDATE(), INTERVAL -1 WEEK) AND o.order_date < CURDATE() LIMIT 0, 1000;", nativeQuery = true)
	public double sumOfAmtLastOneWeek();

}
