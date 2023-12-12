package com.fastshop.Repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastshop.model.Products;

@Repository
public interface ProductsRepo extends JpaRepository<Products, Long> {

	List<Products> findAllByCategory_Id(int id);

	@Modifying
	@Query("update Products u set u.qty = ?1 , u.price=?2 where u.id=?3")
	void updateCartItemQuantity(double qty, double price, Long id);

//	double
	@Transactional
	@Modifying
	@Query("update Products u set u.qty = ?1 , u.price=?2,u.name=?3,u.remarks=?4 where u.id=?5")
	void updateProductByAdmin(double qty, double price, String name, String remarks, Long id);

	@Query("SELECT pd FROM Products pd WHERE LOWER(pd.name) LIKE LOWER(CONCAT('%', :product, '%')) OR LOWER(pd.category.name) LIKE LOWER(CONCAT('%', :category, '%'))")
	List<Products> findByProductNameOrCategoryNameContaining(@Param("product") String productName,
			@Param("category") String categoryName);

}
